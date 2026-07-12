package com.quizmaster.analytics.controller;

import com.quizmaster.analytics.dto.AdminDashboardResponse;
import com.quizmaster.analytics.dto.ApiResponse;
import com.quizmaster.analytics.dto.QuizAnalyticsResponse;
import com.quizmaster.analytics.dto.QuizAttemptRowResponse;
import com.quizmaster.analytics.dto.StudentPerformanceResponse;
import com.quizmaster.analytics.service.AnalyticsQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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

    /** Paged attempts for one quiz. */
    @GetMapping("/quiz/{quizUuid}/attempts")
    public ResponseEntity<ApiResponse<Page<QuizAttemptRowResponse>>> getQuizAttempts(
            @PathVariable String quizUuid,
            @PageableDefault(size = 20, sort = "gradedAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success("Quiz attempts retrieved",
                analyticsQueryService.quizAttempts(quizUuid, pageable)));
    }

    /** Paged per-student performance aggregate. */
    @GetMapping("/students")
    public ResponseEntity<ApiResponse<Page<StudentPerformanceResponse>>> getStudentsPerformance(
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success("Student performance retrieved",
                analyticsQueryService.studentPerformance(pageable)));
    }
}
