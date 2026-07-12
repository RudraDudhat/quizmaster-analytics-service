package com.quizmaster.analytics.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Denormalized read-model row, one per graded attempt. Built from
 * {@code quiz.attempt.graded} events and keyed by attemptUuid so an
 * essay-regrade (which re-emits the event) updates the same row.
 */
@Entity
@Table(name = "attempt_records", indexes = {
        @Index(name = "idx_ar_quiz", columnList = "quiz_uuid"),
        @Index(name = "idx_ar_student", columnList = "student_user_id"),
        @Index(name = "idx_ar_graded_at", columnList = "graded_at")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttemptRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "attempt_uuid", nullable = false, unique = true)
    private String attemptUuid;

    @Column(name = "quiz_uuid", nullable = false)
    private String quizUuid;

    @Column(name = "quiz_title")
    private String quizTitle;

    @Column(name = "student_user_id", nullable = false)
    private Long studentUserId;

    @Column(name = "student_email")
    private String studentEmail;

    @Column(name = "marks_obtained", precision = 8, scale = 2)
    private BigDecimal marksObtained;

    @Column(name = "total_marks_possible", precision = 8, scale = 2)
    private BigDecimal totalMarksPossible;

    @Column(precision = 5, scale = 2)
    private BigDecimal percentage;

    @Column(name = "is_passed")
    private Boolean isPassed;

    @Column(name = "auto_submitted", nullable = false)
    private boolean autoSubmitted;

    @Column(name = "correct_count")
    private Integer correctCount;

    @Column(name = "wrong_count")
    private Integer wrongCount;

    @Column(name = "skipped_count")
    private Integer skippedCount;

    @Column(name = "pending_review_count")
    private Integer pendingReviewCount;

    @Column(name = "graded_at", nullable = false)
    private Instant gradedAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
}
