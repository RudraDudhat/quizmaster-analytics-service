package com.quizmaster.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentAnalyticsResponse {
    private long totalQuizzesTaken;
    private long totalQuizzesPassed;
    private BigDecimal averageScore;
    private BigDecimal bestScore;
    private BigDecimal passRate;
    private List<AttemptSummaryDto> recentAttempts;
}
