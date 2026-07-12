package com.quizmaster.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** One "top quiz by attempts" row. */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuizCountDto {
    private String quizUuid;
    private String quizTitle;
    private long attemptCount;
}
