package com.chronos.scheduler;

import com.chronos.entity.Job;
import com.chronos.entity.JobExecutionLog;
import com.chronos.entity.enums.JobStatus;
import com.chronos.repository.JobExecutionLogRepository;
import com.chronos.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class JobWorker {

    private static final String WORKER_INSTANCE = "worker-1";

    private final JobRescheduler jobRescheduler;
    private final JobRepository jobRepository;
    private final JobExecutionLogRepository logRepository;

    public void execute(Job job) {

        log.info(
                "Job execution started | jobId={} | jobType={} | status={} | retryCount={}",
                job.getId(),
                job.getJobType(),
                job.getStatus(),
                job.getRetryCount()
        );

        if (job.getStatus() == JobStatus.CANCELLED ||
                job.getStatus() == JobStatus.DEAD) {

            log.warn(
                    "Skipping job execution due to terminal state | jobId={} | status={}",
                    job.getId(),
                    job.getStatus()
            );

            return;
        }

        LocalDateTime start = LocalDateTime.now();

        if (job.getNextRunAt() != null &&
                job.getNextRunAt().isBefore(start)) {

            log.debug(
                    "Adjusting stale nextRunAt timestamp | jobId={} | previousNextRunAt={}",
                    job.getId(),
                    job.getNextRunAt()
            );

            job.setNextRunAt(start.plusSeconds(1));
        }

        // FIXED: no DB race condition anymore
        int executionNumber = job.getRetryCount() + 1;

        JobExecutionLog logEntity = JobExecutionLog.builder()
                .job(job)
                .executionNumber(executionNumber)
                .status(JobStatus.RUNNING)
                .startedAt(start)
                .workerInstance(WORKER_INSTANCE)
                .build();

        logEntity = logRepository.save(logEntity);

        log.info(
                "Execution log created | jobId={} | executionNumber={} | worker={}",
                job.getId(),
                executionNumber,
                WORKER_INSTANCE
        );

        try {

            performJob(job);

            LocalDateTime end = LocalDateTime.now();
            long executionTime = Duration.between(start, end).toMillis();

            job.setStatus(JobStatus.SUCCESS);
            job.setLastError(null);

            logEntity.setStatus(JobStatus.SUCCESS);
            logEntity.setCompletedAt(end);
            logEntity.setExecutionTimeMs(executionTime);

            jobRescheduler.reschedule(job, end);

            log.info(
                    "Job executed successfully | jobId={} | executionNumber={} | executionTimeMs={}",
                    job.getId(),
                    executionNumber,
                    executionTime
            );

        } catch (Exception e) {

            log.error(
                    "Job execution failed | jobId={} | executionNumber={} | error={}",
                    job.getId(),
                    executionNumber,
                    e.getMessage(),
                    e
            );

            handleFailure(job, logEntity, e);
        }

        jobRepository.save(job);
        logRepository.save(logEntity);

        log.info(
                "Job persistence completed | jobId={} | finalStatus={} | nextRunAt={}",
                job.getId(),
                job.getStatus(),
                job.getNextRunAt()
        );
    }

    private void performJob(Job job) {

        log.debug(
                "Performing job logic | jobId={} | jobType={}",
                job.getId(),
                job.getJobType()
        );

        if ("FAIL_TEST".equals(job.getJobType())) {
            throw new RuntimeException("Simulated failure");
        }

        log.debug(
                "Job logic execution completed | jobId={}",
                job.getId()
        );
    }

    private void handleFailure(Job job,
                               JobExecutionLog logEntity,
                               Exception e) {

        job.setRetryCount(job.getRetryCount() + 1);
        job.setLastError(e.getMessage());

        logEntity.setStatus(JobStatus.FAILED);
        logEntity.setErrorMessage(e.getMessage());
        logEntity.setCompletedAt(LocalDateTime.now());

        if (job.getRetryCount() < job.getMaxRetries()) {

            long delay = Math.min((long) Math.pow(2, job.getRetryCount()), 300);

            job.setStatus(JobStatus.RETRYING);
            job.setNextRunAt(LocalDateTime.now().plusSeconds(delay));

            log.warn(
                    "Job scheduled for retry | jobId={} | retryCount={} | maxRetries={} | retryDelaySeconds={}",
                    job.getId(),
                    job.getRetryCount(),
                    job.getMaxRetries(),
                    delay
            );

        } else {

            if (Boolean.TRUE.equals(job.getRecurring())) {

                log.warn(
                        "Max retries exhausted for recurring job, rescheduling | jobId={}",
                        job.getId()
                );

                jobRescheduler.reschedule(job, LocalDateTime.now());

            } else {

                job.setStatus(JobStatus.DEAD);

                log.error(
                        "Job marked as DEAD after exhausting retries | jobId={} | retryCount={}",
                        job.getId(),
                        job.getRetryCount()
                );
            }
        }
    }
}