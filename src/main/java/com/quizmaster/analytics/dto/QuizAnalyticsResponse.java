package com.quizmaster.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuizAnalyticsResponse {
    private String quizUuid;
    private String quizTitle;
    private long totalAttempts;
    private BigDecimal averageScore;
    private BigDecimal highestScore;
    private BigDecimal lowestScore;
    private BigDecimal averagePercentage;
    private long passCount;
    private long failCount;
    private BigDecimal passRate;
    /** Percentage buckets, e.g. "0-20": 3, "21-40": 5, ... */
    private Map<String, Integer> scoreDistribution;
}
