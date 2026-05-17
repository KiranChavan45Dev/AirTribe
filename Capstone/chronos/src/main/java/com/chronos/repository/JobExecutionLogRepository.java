package com.chronos.repository;

import com.chronos.entity.JobExecutionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JobExecutionLogRepository extends JpaRepository<JobExecutionLog, UUID> {
    List<JobExecutionLog> findByJobId(UUID jobId);

    Optional<JobExecutionLog> findTopByJobIdOrderByStartedAtDesc(UUID jobId);

    @Query("SELECT COALESCE(MAX(l.executionNumber), 0) FROM JobExecutionLog l WHERE l.job.id = :jobId")
    int getMaxExecutionNumber(@Param("jobId") UUID jobId);
}
