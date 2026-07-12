package com.quizmaster.analytics.repository;

import com.quizmaster.analytics.entity.AttemptRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttemptRecordRepository extends JpaRepository<AttemptRecord, Long> {

    Optional<AttemptRecord> findByAttemptUuid(String attemptUuid);

    long countByGradedAtAfter(Instant instant);

    long countByIsPassedTrue();

    List<AttemptRecord> findTop10ByOrderByGradedAtDesc();

    List<AttemptRecord> findByQuizUuid(String quizUuid);

    /** Paged attempts for one quiz (admin quiz-attempts table). */
    Page<AttemptRecord> findByQuizUuid(String quizUuid, Pageable pageable);

    List<AttemptRecord> findByStudentUserId(Long studentUserId);

    List<AttemptRecord> findTop5ByStudentUserIdOrderByGradedAtDesc(Long studentUserId);

    @Query("SELECT COUNT(DISTINCT r.studentUserId) FROM AttemptRecord r")
    long countDistinctStudents();

    @Query("SELECT COUNT(DISTINCT r.quizUuid) FROM AttemptRecord r")
    long countDistinctQuizzes();

    /** Rows: [quizUuid(String), quizTitle(String), attemptCount(Long)] desc by count. */
    @Query("""
            SELECT r.quizUuid, r.quizTitle, COUNT(r)
            FROM AttemptRecord r
            GROUP BY r.quizUuid, r.quizTitle
            ORDER BY COUNT(r) DESC
            """)
    List<Object[]> findTopQuizzesByAttemptCount(Pageable pageable);

    /** Per-student aggregate across all their graded attempts (admin table). */
    @Query(value = """
            SELECT r.studentUserId                                     AS studentUserId,
                   r.studentEmail                                      AS studentEmail,
                   COUNT(r)                                            AS totalAttempts,
                   SUM(CASE WHEN r.isPassed = true  THEN 1 ELSE 0 END) AS passCount,
                   SUM(CASE WHEN r.isPassed = false THEN 1 ELSE 0 END) AS failCount,
                   AVG(r.marksObtained)                                AS averageScore,
                   MAX(r.marksObtained)                                AS bestScore,
                   MAX(r.gradedAt)                                     AS lastAttemptAt
            FROM AttemptRecord r
            GROUP BY r.studentUserId, r.studentEmail
            ORDER BY COUNT(r) DESC
            """,
            countQuery = "SELECT COUNT(DISTINCT r.studentUserId) FROM AttemptRecord r")
    Page<StudentPerformanceProjection> findStudentPerformance(Pageable pageable);
}
