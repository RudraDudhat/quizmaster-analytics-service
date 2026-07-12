package com.quizmaster.analytics.repository;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Aggregate projection for the per-student performance query. Property names
 * match the SELECT aliases in {@code findStudentPerformance}.
 */
public interface StudentPerformanceProjection {
    Long getStudentUserId();
    String getStudentEmail();
    Long getTotalAttempts();
    Long getPassCount();
    Long getFailCount();
    Double getAverageScore();   // AVG() → Double in JPQL
    BigDecimal getBestScore();  // MAX(BigDecimal) preserves type
    Instant getLastAttemptAt();
}
