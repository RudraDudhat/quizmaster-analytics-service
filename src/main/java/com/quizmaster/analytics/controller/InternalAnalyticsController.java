package com.quizmaster.analytics.controller;

import com.quizmaster.analytics.dto.StudentAnalyticsResponse;
import com.quizmaster.analytics.service.AnalyticsQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * INTERNAL service-to-service API. Consumed by auth-service to embed a
 * student's performance stats in the admin student-detail view. NOT
 * gateway-routed — trusted network only. Keyed by studentUserId because that
 * is what analytics stores (it never sees user UUIDs); auth-service, which
 * owns users, does the uuid → id resolution before calling.
 */
@RestController
@RequestMapping("/api/internal")
@RequiredArgsConstructor
public class InternalAnalyticsController {

    private final AnalyticsQueryService analyticsQueryService;

    @GetMapping("/students/{userId}/stats")
    public ResponseEntity<StudentAnalyticsResponse> getStudentStats(@PathVariable Long userId) {
        return ResponseEntity.ok(analyticsQueryService.studentAnalytics(userId));
    }
}
