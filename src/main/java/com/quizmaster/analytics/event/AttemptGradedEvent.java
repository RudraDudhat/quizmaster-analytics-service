package com.quizmaster.analytics.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/** Consumed from {@code quiz.attempt.graded}. Mirror of grading-service's event. */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttemptGradedEvent {
    private String attemptUuid;
    private String quizUuid;
    private Long studentUserId;
    private String studentEmail;
    private String quizTitle;
    private boolean autoSubmitted;
    private BigDecimal marksObtained;
    private BigDecimal positiveMarks;
    private BigDecimal negativeMarksDeducted;
    private BigDecimal totalMarksPossible;
    private BigDecimal percentage;
    private Boolean isPassed;
    private int correctCount;
    private int wrongCount;
    private int skippedCount;
    private int pendingReviewCount;
}
