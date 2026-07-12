package com.quizmaster.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

/** One row in the admin "attempts for a quiz" table. */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuizAttemptRowResponse {
    private String attemptUuid;
    private Long studentUserId;
    private String studentEmail;
    private BigDecimal marksObtained;
    private BigDecimal totalMarksPossible;
    private BigDecimal percentage;
    private Boolean isPassed;
    private Integer correctCount;
    private Integer wrongCount;
    private Integer skippedCount;
    private Boolean autoSubmitted;
    private Instant gradedAt;
}
