package com.quizmaster.analytics.service;

import com.quizmaster.analytics.entity.AttemptRecord;
import com.quizmaster.analytics.event.AttemptGradedEvent;
import com.quizmaster.analytics.repository.AttemptRecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

/**
 * Applies graded events to the read model. Upserts by attemptUuid so an
 * essay-regrade (which re-emits quiz.attempt.graded) updates the same row
 * rather than duplicating it.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AnalyticsIngestService {

    private final AttemptRecordRepository repository;

    @Transactional
    public void ingest(AttemptGradedEvent e) {
        AttemptRecord record = repository.findByAttemptUuid(e.getAttemptUuid())
                .orElseGet(AttemptRecord::new);

        record.setAttemptUuid(e.getAttemptUuid());
        record.setQuizUuid(e.getQuizUuid());
        record.setQuizTitle(e.getQuizTitle());
        record.setStudentUserId(e.getStudentUserId());
        record.setStudentEmail(e.getStudentEmail());
        record.setMarksObtained(e.getMarksObtained());
        record.setTotalMarksPossible(e.getTotalMarksPossible());
        record.setPercentage(e.getPercentage());
        record.setIsPassed(e.getIsPassed());
        record.setAutoSubmitted(e.isAutoSubmitted());
        record.setCorrectCount(e.getCorrectCount());
        record.setWrongCount(e.getWrongCount());
        record.setSkippedCount(e.getSkippedCount());
        record.setPendingReviewCount(e.getPendingReviewCount());
        if (record.getGradedAt() == null) {
            record.setGradedAt(Instant.now());
        }

        repository.save(record);
        log.debug("Ingested attempt {} into analytics read model", e.getAttemptUuid());
    }
}
