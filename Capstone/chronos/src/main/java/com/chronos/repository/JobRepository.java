package com.chronos.repository;

import com.chronos.entity.Job;
import com.chronos.entity.enums.JobStatus;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface JobRepository extends JpaRepository<Job, UUID> {

    List<Job> findByUserId(UUID userId);

    // SAFE CLAIMING QUERY (ATOMIC)
    @Modifying
    @Query(value = """
        WITH cte AS (
            SELECT id
            FROM jobs
            WHERE status IN ('PENDING', 'SCHEDULED', 'RETRYING')
              AND next_run_at <= :now
            ORDER BY priority DESC, next_run_at ASC
            FOR UPDATE SKIP LOCKED
            LIMIT :limit
        )
        UPDATE jobs j
        SET status = 'RUNNING'
        FROM cte
        WHERE j.id = cte.id
        RETURNING j.*
        """, nativeQuery = true)
    List<Job> claimJobs(
            @Param("now") LocalDateTime now,
            @Param("limit") int limit
    );

    long countByStatus(JobStatus status);
}