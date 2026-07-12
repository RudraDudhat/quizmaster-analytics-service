package com.quizmaster.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

/** A single graded attempt as it appears in dashboards / recent lists. */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttemptSummaryDto {
    private String attemptUuid;
    private String quizUuid;
    private String quizTitle;
    private Long studentUserId;
    private String studentEmail;
    private BigDecimal marksObtained;
    private BigDecimal totalMarksPossible;
    private BigDecimal percentage;
    private Boolean isPassed;
    private Instant gradedAt;
}
