package com.quizmaster.analytics.repository;

import com.quizmaster.analytics.entity.AttemptRecord;
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
}
