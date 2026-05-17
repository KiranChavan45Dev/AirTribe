package com.chronos.service;

import com.chronos.dto.job.*;
import com.chronos.entity.Job;
import com.chronos.entity.User;
import com.chronos.entity.enums.JobStatus;
import com.chronos.entity.enums.ScheduleType;
import com.chronos.exception.ResourceNotFoundException;
import com.chronos.repository.JobRepository;
import com.chronos.repository.UserRepository;
import com.chronos.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;

    private final UserRepository userRepository;

    public JobResponse createJob(
            CreateJobRequest request
    ) {

        String username =
                SecurityUtils.getCurrentUsername();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        boolean recurring =
                request.getScheduleType() != ScheduleType.ONCE;

        Job job = Job.builder()
                .user(user)
                .jobName(request.getJobName())
                .jobType(request.getJobType())
                .payload(request.getPayload())
                .status(JobStatus.SCHEDULED)
                .scheduleType(request.getScheduleType())
                .cronExpression(request.getCronExpression())
                .runAt(request.getRunAt())
                .nextRunAt(resolveNextRunAt(request))
                .retryCount(0)
                .maxRetries(request.getMaxRetries())
                .priority(request.getPriority())
                .recurring(recurring)
                .build();
        validateJob(job);
        Job savedJob = jobRepository.save(job);

        return mapToResponse(savedJob);
    }

    private void validateJob(Job job) {

        if (job.getNextRunAt() == null) {
            throw new IllegalStateException("nextRunAt is required but missing");
        }

        if (job.getRunAt() == null) {
            throw new IllegalStateException("runAt is required");
        }
    }

    public List<JobResponse> getMyJobs() {

        String username =
                SecurityUtils.getCurrentUsername();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        return jobRepository.findByUserId(user.getId())
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public JobResponse getJob(UUID jobId) {

        Job job = getOwnedJob(jobId);

        return mapToResponse(job);
    }

    public void cancelJob(UUID jobId) {

        Job job = getOwnedJob(jobId);

        job.setStatus(JobStatus.CANCELLED);

        jobRepository.save(job);
    }

    public JobResponse rescheduleJob(UUID jobId, RescheduleJobRequest request) {

        Job job = getOwnedJob(jobId);

        LocalDateTime nextRun = request.getNextRunAt();

        if (nextRun == null) {
            throw new IllegalArgumentException("nextRunAt cannot be null");
        }

        job.setNextRunAt(nextRun);
        job.setStatus(JobStatus.SCHEDULED);

        Job updated = jobRepository.save(job);

        return mapToResponse(updated);
    }

    private Job getOwnedJob(UUID jobId) {

        String username =
                SecurityUtils.getCurrentUsername();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Job not found"));

        if (!job.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized access");
        }

        return job;
    }

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

    private LocalDateTime resolveNextRunAt(CreateJobRequest request) {

        if (request.getRunAt() != null) {
            return request.getRunAt();
        }

        throw new IllegalArgumentException("runAt is required for schedule type " + request.getScheduleType());
    }
}