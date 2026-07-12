package com.quizmaster.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * One row in the admin per-student performance table, aggregated from the
 * AttemptRecord read model. studentName/uuid are auth-service data — only
 * studentUserId + studentEmail are available here.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentPerformanceResponse {
    private Long studentUserId;
    private String studentEmail;
    private long totalAttempts;
    private long passCount;
    private long failCount;
    private BigDecimal averageScore;
    private BigDecimal bestScore;
    private BigDecimal passRate;
    private Instant lastAttemptAt;
}
