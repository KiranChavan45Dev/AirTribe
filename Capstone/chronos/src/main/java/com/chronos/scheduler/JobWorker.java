package com.chronos.scheduler;

import com.chronos.entity.Job;
import com.chronos.entity.JobExecutionLog;
import com.chronos.entity.enums.JobStatus;
import com.chronos.repository.JobExecutionLogRepository;
import com.chronos.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JobWorker {

    private final JobRescheduler jobRescheduler;
    private final JobRepository jobRepository;
    private final JobExecutionLogRepository logRepository;

    public void execute(Job job) {

        if (job.getStatus() == JobStatus.CANCELLED ||
                job.getStatus() == JobStatus.DEAD) {
            return;
        }

        LocalDateTime start = LocalDateTime.now();

        if (job.getNextRunAt() != null &&
                job.getNextRunAt().isBefore(start)) {

            job.setNextRunAt(start.plusSeconds(1));
        }

        // FIXED: no DB race condition anymore
        int executionNumber = job.getRetryCount() + 1;

        JobExecutionLog log = JobExecutionLog.builder()
                .job(job)
                .executionNumber(executionNumber)
                .status(JobStatus.RUNNING)
                .startedAt(start)
                .workerInstance("worker-1")
                .build();

        log = logRepository.save(log);

        try {

            performJob(job);

            LocalDateTime end = LocalDateTime.now();

            job.setStatus(JobStatus.SUCCESS);
            job.setLastError(null);

            log.setStatus(JobStatus.SUCCESS);
            log.setCompletedAt(end);
            log.setExecutionTimeMs(Duration.between(start, end).toMillis());

            jobRescheduler.reschedule(job, end);

        } catch (Exception e) {

            handleFailure(job, log, e);
        }

        jobRepository.save(job);
        logRepository.save(log);
    }

    private void performJob(Job job) {

        if ("FAIL_TEST".equals(job.getJobType())) {
            throw new RuntimeException("Simulated failure");
        }

        System.out.println("Executing job: " + job.getJobName());
    }

    private void handleFailure(Job job,
                               JobExecutionLog log,
                               Exception e) {

        job.setRetryCount(job.getRetryCount() + 1);
        job.setLastError(e.getMessage());

        log.setStatus(JobStatus.FAILED);
        log.setErrorMessage(e.getMessage());
        log.setCompletedAt(LocalDateTime.now());

        if (job.getRetryCount() < job.getMaxRetries()) {

            long delay = Math.min((long) Math.pow(2, job.getRetryCount()), 300);

            job.setStatus(JobStatus.RETRYING);
            job.setNextRunAt(LocalDateTime.now().plusSeconds(delay));

        } else {

            if (Boolean.TRUE.equals(job.getRecurring())) {
                jobRescheduler.reschedule(job, LocalDateTime.now());
            } else {
                job.setStatus(JobStatus.DEAD);
            }
        }
    }
}