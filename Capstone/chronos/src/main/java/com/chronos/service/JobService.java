package com.chronos.service;

import com.chronos.dto.job.*;
import com.chronos.entity.Job;
import com.chronos.entity.User;
import com.chronos.entity.enums.JobStatus;
import com.chronos.entity.enums.ScheduleType;
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

        log.info(
                "Job creation requested | jobName={} | jobType={} | scheduleType={}",
                request.getJobName(),
                request.getJobType(),
                request.getScheduleType()
        );

        User user = getCurrentUser();

        log.debug(
                "Authenticated user resolved for job creation | userId={} | username={}",
                user.getId(),
                user.getUsername()
        );

        if (request.getCronExpression() != null) {

            log.debug(
                    "Validating cron expression | expression={}",
                    request.getCronExpression()
            );

            CronExpression.parse(request.getCronExpression());
        }

        JsonNode payloadNode = null;

        if (request.getPayload() != null) {

            log.debug(
                    "Converting payload to JsonNode | jobName={}",
                    request.getJobName()
            );

            try {
                payloadNode = objectMapper.valueToTree(request.getPayload());

            } catch (Exception e) {

                log.error(
                        "Failed to serialize job payload | jobName={} | error={}",
                        request.getJobName(),
                        e.getMessage(),
                        e
                );

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

        log.info(
                "Job created successfully | jobId={} | userId={} | nextRunAt={}",
                saved.getId(),
                user.getId(),
                saved.getNextRunAt()
        );

        return mapToResponse(saved);
    }

    // ---------------- GET MY JOBS ----------------

    public List<JobResponse> getMyJobs() {

        User user = getCurrentUser();

        log.info(
                "Fetching jobs for user | userId={} | username={}",
                user.getId(),
                user.getUsername()
        );

        List<JobResponse> jobs = jobRepository.findByUserId(user.getId())
                .stream()
                .map(this::mapToResponse)
                .toList();

        log.info(
                "Jobs fetched successfully | userId={} | totalJobs={}",
                user.getId(),
                jobs.size()
        );

        return jobs;
    }

    // ---------------- GET JOB ----------------

    public JobResponse getJob(UUID jobId) {

        log.info("Fetching job details | jobId={}", jobId);

        JobResponse response = mapToResponse(getOwnedJob(jobId));

        log.debug(
                "Job fetched successfully | jobId={} | status={}",
                response.getId(),
                response.getStatus()
        );

        return response;
    }

    // ---------------- CANCEL JOB ----------------

    public void cancelJob(UUID jobId) {

        Job job = getOwnedJob(jobId);

        assertCancelable(job);

        log.info(
                "Cancelling job | jobId={} | currentStatus={}",
                jobId,
                job.getStatus()
        );

        job.setStatus(JobStatus.CANCELLED);

        jobRepository.save(job);

        log.info(
                "Job cancelled successfully | jobId={}",
                jobId
        );
    }

    // ---------------- RESCHEDULE ----------------

    public JobResponse rescheduleJob(UUID jobId, RescheduleJobRequest request) {

        Job job = getOwnedJob(jobId);

        if (request.getNextRunAt() == null) {

            log.warn(
                    "Reschedule request rejected due to missing nextRunAt | jobId={}",
                    jobId
            );

            throw new IllegalArgumentException("nextRunAt cannot be null");
        }

        log.info(
                "Rescheduling job | jobId={} | previousNextRunAt={} | newNextRunAt={}",
                jobId,
                job.getNextRunAt(),
                request.getNextRunAt()
        );

        job.setNextRunAt(request.getNextRunAt());
        job.setStatus(JobStatus.SCHEDULED);

        Job updatedJob = jobRepository.save(job);

        log.info(
                "Job rescheduled successfully | jobId={} | nextRunAt={}",
                updatedJob.getId(),
                updatedJob.getNextRunAt()
        );

        return mapToResponse(updatedJob);
    }

    // ---------------- VALIDATION ----------------

    private void validateJob(Job job) {

        log.debug(
                "Validating job entity | jobName={} | scheduleType={}",
                job.getJobName(),
                job.getScheduleType()
        );

        if (job.getNextRunAt() == null) {

            log.error(
                    "Job validation failed: nextRunAt missing | jobName={}",
                    job.getJobName()
            );

            throw new IllegalStateException("nextRunAt is required");
        }

        if (job.getScheduleType() == ScheduleType.ONCE &&
                job.getRunAt() == null) {

            log.error(
                    "Job validation failed: runAt missing for ONCE job | jobName={}",
                    job.getJobName()
            );

            throw new IllegalStateException("runAt required for ONCE jobs");
        }

        if (job.getMaxRetries() == null) {

            log.error(
                    "Job validation failed: maxRetries missing | jobName={}",
                    job.getJobName()
            );

            throw new IllegalStateException("maxRetries is required");
        }

        if (job.getPriority() == null) {

            log.error(
                    "Job validation failed: priority missing | jobName={}",
                    job.getJobName()
            );

            throw new IllegalStateException("priority is required");
        }

        log.debug(
                "Job validation completed successfully | jobName={}",
                job.getJobName()
        );
    }

    private LocalDateTime resolveNextRunAt(CreateJobRequest request) {

        log.debug(
                "Resolving nextRunAt | jobName={} | scheduleType={}",
                request.getJobName(),
                request.getScheduleType()
        );

        if (request.getScheduleType() == ScheduleType.ONCE) {

            if (request.getRunAt() == null) {

                log.error(
                        "runAt missing for ONCE job | jobName={}",
                        request.getJobName()
                );

                throw new IllegalArgumentException("runAt required for ONCE jobs");
            }

            return request.getRunAt();
        }

        if (request.getCronExpression() == null) {

            log.error(
                    "cronExpression missing for recurring job | jobName={}",
                    request.getJobName()
            );

            throw new IllegalArgumentException("cronExpression required for recurring jobs");
        }

        LocalDateTime nextRun =
                CronUtils.next(request.getCronExpression(), LocalDateTime.now());

        log.debug(
                "Resolved nextRunAt using cron | jobName={} | nextRunAt={}",
                request.getJobName(),
                nextRun
        );

        return nextRun;
    }

    // ---------------- OWNERSHIP ----------------

    private Job getOwnedJob(UUID jobId) {

        User user = getCurrentUser();

        log.debug(
                "Validating job ownership | jobId={} | userId={}",
                jobId,
                user.getId()
        );

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> {

                    log.warn(
                            "Requested job not found | jobId={}",
                            jobId
                    );

                    return new ResourceNotFoundException("Job not found");
                });

        if (!job.getUser().getId().equals(user.getId())) {

            log.warn(
                    "Unauthorized job access attempt | jobId={} | requestedByUserId={} | ownerUserId={}",
                    jobId,
                    user.getId(),
                    job.getUser().getId()
            );

            throw new AccessDeniedException("Unauthorized access");
        }

        return job;
    }

    private User getCurrentUser() {

        String username = SecurityUtils.getCurrentUsername();

        log.debug(
                "Resolving current authenticated user | username={}",
                username
        );

        return userRepository.findByUsername(username)
                .orElseThrow(() -> {

                    log.error(
                            "Authenticated user not found in database | username={}",
                            username
                    );

                    return new ResourceNotFoundException("User not found");
                });
    }

    private void assertCancelable(Job job) {

        if (job.getStatus() == JobStatus.RUNNING) {

            log.warn(
                    "Attempt to cancel running job rejected | jobId={}",
                    job.getId()
            );

            throw new IllegalStateException("Cannot cancel running job");
        }
    }

    // ---------------- MAPPER ----------------

    private JobResponse mapToResponse(Job job) {

        return JobResponse.builder()
                .id(job.getId())
                .jobName(job.getJobName())
                .jobType(job.getJobType())
                .payload(job.getPayload())
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

        log.debug(
                "Claiming jobs for execution | timestamp={} | limit={}",
                now,
                limit
        );

        List<UUID> ids = jobRepository.claimJobIds(now, limit);

        if (ids.isEmpty()) {

            log.debug(
                    "No jobs available for claiming | timestamp={}",
                    now
            );

            return List.of();
        }

        log.info(
                "Jobs claimed successfully | claimedCount={}",
                ids.size()
        );

        return jobRepository.findAllByIds(ids);
    }
}