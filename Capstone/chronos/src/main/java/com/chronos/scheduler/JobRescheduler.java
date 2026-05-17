package com.chronos.scheduler;

import com.chronos.entity.Job;
import com.chronos.entity.enums.JobStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class JobRescheduler {

    @Transactional
    public void reschedule(Job job, LocalDateTime baseTime) {

        if (!Boolean.TRUE.equals(job.getRecurring())) return;

        if (job.getCronExpression() == null) {
            job.setStatus(JobStatus.DEAD);
            job.setLastError("Missing cron expression for recurring job");
            return;
        }

        try {
            LocalDateTime nextRun =
                    CronUtils.next(job.getCronExpression(), baseTime);

            if (nextRun == null || nextRun.isBefore(baseTime)) {
                job.setStatus(JobStatus.DEAD);
                job.setLastError("Invalid cron nextRun calculation");
                return;
            }

            job.setNextRunAt(nextRun);
            job.setStatus(JobStatus.SCHEDULED);

        } catch (Exception e) {
            job.setStatus(JobStatus.DEAD);
            job.setLastError("Cron parsing failed: " + e.getMessage());
        }
    }
}