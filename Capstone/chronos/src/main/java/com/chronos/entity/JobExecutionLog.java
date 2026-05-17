package com.chronos.entity;

import com.chronos.entity.enums.JobStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "job_execution_logs")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobExecutionLog {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id")
    private Job job;

    @Column(name = "execution_number")
    private Integer executionNumber;

    @Enumerated(EnumType.STRING)
    private JobStatus status;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "execution_time_ms")
    private Long executionTimeMs;

    @Column(name = "error_message")
    private String errorMessage;

    @Column(name = "worker_instance")
    private String workerInstance;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}