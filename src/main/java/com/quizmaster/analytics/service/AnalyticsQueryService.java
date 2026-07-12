package com.quizmaster.analytics.service;

import com.quizmaster.analytics.dto.*;
import com.quizmaster.analytics.entity.AttemptRecord;
import com.quizmaster.analytics.repository.AttemptRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnalyticsQueryService {

    private static final BigDecimal HUNDRED = new BigDecimal("100");

    private final AttemptRecordRepository repository;

    // ═══════════════════════════════════════════════════════
    // ADMIN DASHBOARD
    // ═══════════════════════════════════════════════════════

    public AdminDashboardResponse adminDashboard() {
        long total = repository.count();
        var todayStart = LocalDate.now(ZoneOffset.UTC).atStartOfDay().toInstant(ZoneOffset.UTC);
        long attemptsToday = repository.countByGradedAtAfter(todayStart);
        long passed = repository.countByIsPassedTrue();

        BigDecimal passRate = total > 0
                ? BigDecimal.valueOf(passed).multiply(HUNDRED).divide(BigDecimal.valueOf(total), 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        List<QuizCountDto> topQuizzes = repository.findTopQuizzesByAttemptCount(PageRequest.of(0, 5)).stream()
                .map(row -> QuizCountDto.builder()
                        .quizUuid((String) row[0])
                        .quizTitle((String) row[1])
                        .attemptCount(((Number) row[2]).longValue())
                        .build())
                .toList();

        List<AttemptSummaryDto> recent = repository.findTop10ByOrderByGradedAtDesc().stream()
                .map(this::toSummary)
                .toList();

        return AdminDashboardResponse.builder()
                .totalAttempts(total)
                .attemptsToday(attemptsToday)
                .distinctStudents(repository.countDistinctStudents())
                .distinctQuizzes(repository.countDistinctQuizzes())
                .platformPassRate(passRate)
                .recentAttempts(recent)
                .topQuizzes(topQuizzes)
                .build();
    }

    // ═══════════════════════════════════════════════════════
    // PER-QUIZ ANALYTICS
    // ═══════════════════════════════════════════════════════

    public QuizAnalyticsResponse quizAnalytics(String quizUuid) {
        List<AttemptRecord> records = repository.findByQuizUuid(quizUuid);

        QuizAnalyticsResponse.QuizAnalyticsResponseBuilder builder = QuizAnalyticsResponse.builder()
                .quizUuid(quizUuid)
                .totalAttempts(records.size());

        if (records.isEmpty()) {
            return builder
                    .averageScore(BigDecimal.ZERO).highestScore(BigDecimal.ZERO).lowestScore(BigDecimal.ZERO)
                    .averagePercentage(BigDecimal.ZERO).passCount(0).failCount(0).passRate(BigDecimal.ZERO)
                    .scoreDistribution(emptyDistribution())
                    .build();
        }

        builder.quizTitle(records.get(0).getQuizTitle());

        BigDecimal sumMarks = BigDecimal.ZERO;
        BigDecimal sumPct = BigDecimal.ZERO;
        BigDecimal high = null, low = null;
        long passCount = 0;
        Map<String, Integer> distribution = emptyDistribution();

        for (AttemptRecord r : records) {
            BigDecimal marks = nz(r.getMarksObtained());
            BigDecimal pct = nz(r.getPercentage());
            sumMarks = sumMarks.add(marks);
            sumPct = sumPct.add(pct);
            high = (high == null || marks.compareTo(high) > 0) ? marks : high;
            low = (low == null || marks.compareTo(low) < 0) ? marks : low;
            if (Boolean.TRUE.equals(r.getIsPassed())) passCount++;
            distribution.merge(bucket(pct), 1, Integer::sum);
        }

        int n = records.size();
        BigDecimal avgMarks = sumMarks.divide(BigDecimal.valueOf(n), 2, RoundingMode.HALF_UP);
        BigDecimal avgPct = sumPct.divide(BigDecimal.valueOf(n), 2, RoundingMode.HALF_UP);
        long failCount = n - passCount;
        BigDecimal passRate = BigDecimal.valueOf(passCount).multiply(HUNDRED)
                .divide(BigDecimal.valueOf(n), 2, RoundingMode.HALF_UP);

        return builder
                .averageScore(avgMarks).highestScore(high).lowestScore(low)
                .averagePercentage(avgPct).passCount(passCount).failCount(failCount).passRate(passRate)
                .scoreDistribution(distribution)
                .build();
    }

    // ═══════════════════════════════════════════════════════
    // PER-STUDENT ANALYTICS
    // ═══════════════════════════════════════════════════════

    public StudentAnalyticsResponse studentAnalytics(Long studentUserId) {
        List<AttemptRecord> records = repository.findByStudentUserId(studentUserId);

        if (records.isEmpty()) {
            return StudentAnalyticsResponse.builder()
                    .totalQuizzesTaken(0).totalQuizzesPassed(0)
                    .averageScore(BigDecimal.ZERO).bestScore(BigDecimal.ZERO).passRate(BigDecimal.ZERO)
                    .recentAttempts(List.of())
                    .build();
        }

        BigDecimal sumMarks = BigDecimal.ZERO;
        BigDecimal best = null;
        long passed = 0;
        for (AttemptRecord r : records) {
            BigDecimal marks = nz(r.getMarksObtained());
            sumMarks = sumMarks.add(marks);
            best = (best == null || marks.compareTo(best) > 0) ? marks : best;
            if (Boolean.TRUE.equals(r.getIsPassed())) passed++;
        }
        int n = records.size();
        BigDecimal avg = sumMarks.divide(BigDecimal.valueOf(n), 2, RoundingMode.HALF_UP);
        BigDecimal passRate = BigDecimal.valueOf(passed).multiply(HUNDRED)
                .divide(BigDecimal.valueOf(n), 2, RoundingMode.HALF_UP);

        List<AttemptSummaryDto> recent = repository
                .findTop5ByStudentUserIdOrderByGradedAtDesc(studentUserId).stream()
                .map(this::toSummary)
                .toList();

        return StudentAnalyticsResponse.builder()
                .totalQuizzesTaken(n)
                .totalQuizzesPassed(passed)
                .averageScore(avg)
                .bestScore(best)
                .passRate(passRate)
                .recentAttempts(recent)
                .build();
    }

    // ═══════════════════════════════════════════════════════
    // HELPERS
    // ═══════════════════════════════════════════════════════

    private AttemptSummaryDto toSummary(AttemptRecord r) {
        return AttemptSummaryDto.builder()
                .attemptUuid(r.getAttemptUuid())
                .quizUuid(r.getQuizUuid())
                .quizTitle(r.getQuizTitle())
                .studentUserId(r.getStudentUserId())
                .studentEmail(r.getStudentEmail())
                .marksObtained(r.getMarksObtained())
                .totalMarksPossible(r.getTotalMarksPossible())
                .percentage(r.getPercentage())
                .isPassed(r.getIsPassed())
                .gradedAt(r.getGradedAt())
                .build();
    }

    private static Map<String, Integer> emptyDistribution() {
        Map<String, Integer> m = new LinkedHashMap<>();
        m.put("0-20", 0);
        m.put("21-40", 0);
        m.put("41-60", 0);
        m.put("61-80", 0);
        m.put("81-100", 0);
        return m;
    }

    private static String bucket(BigDecimal pct) {
        double p = pct.doubleValue();
        if (p <= 20) return "0-20";
        if (p <= 40) return "21-40";
        if (p <= 60) return "41-60";
        if (p <= 80) return "61-80";
        return "81-100";
    }

    private static BigDecimal nz(BigDecimal v) {
        return v == null ? BigDecimal.ZERO : v;
    }
}
