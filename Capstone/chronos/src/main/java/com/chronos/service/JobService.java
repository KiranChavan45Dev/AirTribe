package com.chronos.service;

import com.chronos.dto.job.*;
import com.chronos.entity.Job;
import com.chronos.entity.User;
import com.chronos.entity.enums.JobStatus;
import com.chronos.entity.enums.ScheduleType;
import com.chronos.exception.InvalidCronExpressionException;
import com.chronos.exception.ResourceNotFoundException;
import com.chronos.repository.JobRepository;
import com.chronos.repository.UserRepository;
import com.chronos.scheduler.CronUtils;
import com.chronos.security.SecurityUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.support.CronExpression;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    // ---------------- CREATE JOB ----------------

    public JobResponse createJob(CreateJobRequest request) {

        log.info("Creating job: {}", request.getJobName());

        User user = getCurrentUser();

        if (request.getCronExpression() != null) {
            CronExpression.parse(request.getCronExpression());
        }

        JsonNode payloadNode = null;


        if (request.getPayload() != null) {
            try {
                payloadNode = objectMapper.valueToTree(request.getPayload());
            } catch (Exception e) {
                throw new IllegalArgumentException("Invalid JSON payload", e);
            }
        }

        Job job = Job.builder()
                .user(user)
                .jobName(request.getJobName())
                .jobType(request.getJobType())
                .payload(payloadNode)
                .status(JobStatus.SCHEDULED)
                .scheduleType(request.getScheduleType())
                .cronExpression(request.getCronExpression())
                .runAt(request.getRunAt())
                .nextRunAt(resolveNextRunAt(request))
                .retryCount(0)
                .maxRetries(request.getMaxRetries())
                .priority(request.getPriority())
                .recurring(request.getScheduleType() != ScheduleType.ONCE)
                .build();

        validateJob(job);

        Job saved = jobRepository.save(job);

        log.info("Job created with id={}", saved.getId());

        return mapToResponse(saved);
    }

    // ---------------- GET MY JOBS ----------------

    public List<JobResponse> getMyJobs() {

        User user = getCurrentUser();

        log.info("Fetching jobs for user={}", user.getUsername());

        return jobRepository.findByUserId(user.getId())
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // ---------------- GET JOB ----------------

    public JobResponse getJob(UUID jobId) {
        return mapToResponse(getOwnedJob(jobId));
    }

    // ---------------- CANCEL JOB ----------------

    public void cancelJob(UUID jobId) {

        Job job = getOwnedJob(jobId);

        assertCancelable(job);

        log.info("Cancelling job id={} status={}", jobId, job.getStatus());

        job.setStatus(JobStatus.CANCELLED);

        jobRepository.save(job);
    }

    // ---------------- RESCHEDULE ----------------

    public JobResponse rescheduleJob(UUID jobId, RescheduleJobRequest request) {

        Job job = getOwnedJob(jobId);

        if (request.getNextRunAt() == null) {
            throw new IllegalArgumentException("nextRunAt cannot be null");
        }

        log.info("Rescheduling job id={} to {}", jobId, request.getNextRunAt());

        job.setNextRunAt(request.getNextRunAt());
        job.setStatus(JobStatus.SCHEDULED);

        return mapToResponse(jobRepository.save(job));
    }

    // ---------------- VALIDATION ----------------

    private void validateJob(Job job) {

        if (job.getNextRunAt() == null) {
            throw new IllegalStateException("nextRunAt is required");
        }

        if (job.getScheduleType() == ScheduleType.ONCE && job.getRunAt() == null) {
            throw new IllegalStateException("runAt required for ONCE jobs");
        }

        if (job.getMaxRetries() == null) {
            throw new IllegalStateException("maxRetries is required");
        }

        if (job.getPriority() == null) {
            throw new IllegalStateException("priority is required");
        }
    }

    private LocalDateTime resolveNextRunAt(CreateJobRequest request) {

        if (request.getScheduleType() == ScheduleType.ONCE) {
            if (request.getRunAt() == null) {
                throw new IllegalArgumentException("runAt required for ONCE jobs");
            }
            return request.getRunAt();
        }

        if (request.getCronExpression() == null) {
            throw new IllegalArgumentException("cronExpression required for recurring jobs");
        }

        return CronUtils.next(request.getCronExpression(), LocalDateTime.now());

    }

    // ---------------- OWNERSHIP ----------------

    private Job getOwnedJob(UUID jobId) {

        User user = getCurrentUser();

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));

        if (!job.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("Unauthorized access");
        }

        return job;
    }

    private User getCurrentUser() {
        String username = SecurityUtils.getCurrentUsername();

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private void assertCancelable(Job job) {
        if (job.getStatus() == JobStatus.RUNNING) {
            throw new IllegalStateException("Cannot cancel running job");
        }
    }

    // ---------------- MAPPER ----------------

    private JobResponse mapToResponse(Job job) {

        return JobResponse.builder()
                .id(job.getId())
                .jobName(job.getJobName())
                .jobType(job.getJobType())
                .payload(job.getPayload()) // FIXED
                .status(job.getStatus())
                .scheduleType(job.getScheduleType())
                .cronExpression(job.getCronExpression())
                .runAt(job.getRunAt())
                .nextRunAt(job.getNextRunAt())
                .retryCount(job.getRetryCount())
                .maxRetries(job.getMaxRetries())
                .priority(job.getPriority())
                .lastError(job.getLastError())
                .recurring(job.getRecurring())
                .createdAt(job.getCreatedAt())
                .build();
    }

    // ---------------- SCHEDULER SUPPORT ----------------

    @Transactional
    public List<Job> claimJobs(LocalDateTime now, int limit) {

        List<UUID> ids = jobRepository.claimJobIds(now, limit);

        if (ids.isEmpty()) return List.of();

        return jobRepository.findAllByIds(ids);
    }
}