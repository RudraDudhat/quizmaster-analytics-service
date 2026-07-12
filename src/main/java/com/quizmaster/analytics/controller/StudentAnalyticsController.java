package com.quizmaster.analytics.controller;

import com.quizmaster.analytics.dto.ApiResponse;
import com.quizmaster.analytics.dto.StudentAnalyticsResponse;
import com.quizmaster.analytics.service.AnalyticsQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/student/analytics")
@PreAuthorize("hasRole('STUDENT')")
@RequiredArgsConstructor
public class StudentAnalyticsController {

    private final AnalyticsQueryService analyticsQueryService;

    /** The signed-in student's own performance summary. */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<StudentAnalyticsResponse>> getMyStats(
            @RequestHeader("X-User-Id") Long userId) {
        return ResponseEntity.ok(ApiResponse.success("Student analytics retrieved",
                analyticsQueryService.studentAnalytics(userId)));
    }
}
