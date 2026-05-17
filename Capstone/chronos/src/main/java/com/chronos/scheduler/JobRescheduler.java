package com.chronos.scheduler;

import com.chronos.entity.Job;
import com.chronos.entity.enums.JobStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class JobRescheduler {

    @Transactional
    public void reschedule(Job job, LocalDateTime baseTime) {

        log.info(
                "Job reschedule initiated | jobId={} | baseTime={}",
                job.getId(),
                baseTime
        );

        if (!Boolean.TRUE.equals(job.getRecurring())) {

            log.debug(
                    "Skipping reschedule for non-recurring job | jobId={}",
                    job.getId()
            );

            return;
        }

        if (job.getCronExpression() == null) {

            log.error(
                    "Recurring job missing cron expression | jobId={}",
                    job.getId()
            );

            job.setStatus(JobStatus.DEAD);
            job.setLastError("Missing cron expression for recurring job");

            return;
        }

        try {

            log.debug(
                    "Calculating next run time | jobId={} | cronExpression={}",
                    job.getId(),
                    job.getCronExpression()
            );

            LocalDateTime nextRun =
                    CronUtils.next(job.getCronExpression(), baseTime);

            if (nextRun == null || nextRun.isBefore(baseTime)) {

                log.error(
                        "Invalid nextRun calculated for job | jobId={} | nextRun={}",
                        job.getId(),
                        nextRun
                );

                job.setStatus(JobStatus.DEAD);
                job.setLastError("Invalid cron nextRun calculation");

                return;
            }

            job.setNextRunAt(nextRun);
            job.setStatus(JobStatus.SCHEDULED);

            log.info(
                    "Job rescheduled successfully | jobId={} | nextRunAt={}",
                    job.getId(),
                    nextRun
            );

        } catch (Exception e) {

            log.error(
                    "Failed to reschedule job due to cron parsing error | jobId={} | error={}",
                    job.getId(),
                    e.getMessage(),
                    e
            );

            job.setStatus(JobStatus.DEAD);
            job.setLastError("Cron parsing failed: " + e.getMessage());
        }
    }
}