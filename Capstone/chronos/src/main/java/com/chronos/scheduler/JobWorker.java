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

@Component
@RequiredArgsConstructor
public class JobWorker {

    private final JobRepository jobRepository;

    private final JobExecutionLogRepository logRepository;

    public void execute(Job job) {

        LocalDateTime start = LocalDateTime.now();

        JobExecutionLog log = JobExecutionLog.builder()
                .job(job)
                .executionNumber(job.getRetryCount() + 1)
                .status(JobStatus.RUNNING)
                .startedAt(start)
                .workerInstance("worker-1")
                .build();

        try {

            performJob(job);

            LocalDateTime end = LocalDateTime.now();

            log.setStatus(JobStatus.SUCCESS);
            log.setCompletedAt(end);
            log.setExecutionTimeMs(Duration.between(start, end).toMillis());

            job.setStatus(JobStatus.SUCCESS);
            job.setLastError(null);

            // only mark success here (NO scheduling logic here)
            if (Boolean.TRUE.equals(job.getRecurring())) {
                job.setStatus(JobStatus.SCHEDULED);
            }

        } catch (Exception e) {
            handleFailure(job, log, e);
        }

        jobRepository.save(job);
        logRepository.save(log);
    }

    private void performJob(Job job) {

        // Simulate execution logic

        if (job.getJobType().equals("FAIL_TEST")) {
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

            job.setStatus(JobStatus.RETRYING);
            job.setNextRunAt(
                    LocalDateTime.now()
                            .plusSeconds((long) Math.pow(2, job.getRetryCount()))
            );

        } else {
            job.setStatus(JobStatus.DEAD);
        }
    }

    private void reschedule(Job job) {

        if (job.getCronExpression() != null) {

            job.setNextRunAt(
                    CronUtils.next(job.getCronExpression(), LocalDateTime.now())
            );

            job.setStatus(JobStatus.SCHEDULED);
        }
    }

    private void rescheduleRecurringJob(Job job) {

        job.setStatus(JobStatus.SCHEDULED);

        if (job.getCronExpression() != null) {
            job.setNextRunAt(
                    LocalDateTime.now().plusMinutes(1) // simplified cron
            );
        }
    }
}