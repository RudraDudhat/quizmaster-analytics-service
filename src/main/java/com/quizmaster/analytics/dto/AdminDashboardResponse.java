package com.quizmaster.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * Platform overview, computed entirely from analytics' own attempt read model.
 * Counts of students/quizzes here are "distinct seen in graded attempts" — the
 * authoritative totals live in auth-/quiz-service and could be layered in later
 * via their internal APIs.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminDashboardResponse {
    private long totalAttempts;
    private long attemptsToday;
    private long distinctStudents;
    private long distinctQuizzes;
    private BigDecimal platformPassRate;
    private List<AttemptSummaryDto> recentAttempts;
    private List<QuizCountDto> topQuizzes;
}
