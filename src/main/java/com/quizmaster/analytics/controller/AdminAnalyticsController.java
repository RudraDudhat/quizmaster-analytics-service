package com.quizmaster.analytics.controller;

import com.quizmaster.analytics.dto.AdminDashboardResponse;
import com.quizmaster.analytics.dto.ApiResponse;
import com.quizmaster.analytics.dto.QuizAnalyticsResponse;
import com.quizmaster.analytics.service.AnalyticsQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/analytics")
@PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
@RequiredArgsConstructor
public class AdminAnalyticsController {

    private final AnalyticsQueryService analyticsQueryService;

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<AdminDashboardResponse>> getDashboard() {
        return ResponseEntity.ok(ApiResponse.success("Dashboard retrieved",
                analyticsQueryService.adminDashboard()));
    }

    @GetMapping("/quiz/{quizUuid}")
    public ResponseEntity<ApiResponse<QuizAnalyticsResponse>> getQuizAnalytics(
            @PathVariable String quizUuid) {
        return ResponseEntity.ok(ApiResponse.success("Quiz analytics retrieved",
                analyticsQueryService.quizAnalytics(quizUuid)));
    }
}
