package com.chronos.repository;

import com.chronos.entity.Job;
import com.chronos.entity.enums.JobStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface JobRepository extends JpaRepository<Job, UUID> {
    List<Job> findByUserId(UUID userId);

    @Query(value = """
            SELECT * FROM jobs
            WHERE status IN ('SCHEDULED', 'RETRYING')
            AND next_run_at <= :now
            ORDER BY priority DESC, next_run_at ASC
            LIMIT :limit
            FOR UPDATE SKIP LOCKED
            """, nativeQuery = true)
    List<Job> findJobsToExecute(
            @Param("now") LocalDateTime now,
            @Param("limit") int limit
    );

    @Modifying
    @Query(value = """
            UPDATE jobs
            SET status = 'RUNNING'
            WHERE id IN (
                SELECT id FROM jobs
                WHERE status IN ('SCHEDULED', 'RETRYING')
                  AND next_run_at <= :now
                ORDER BY priority DESC, next_run_at ASC
                LIMIT :limit
                FOR UPDATE SKIP LOCKED
            )
            RETURNING *
            """, nativeQuery = true)
    List<Job> claimJobs(LocalDateTime now, int limit);
    long countByStatus(JobStatus status);
}
