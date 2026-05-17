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

        if (!Boolean.TRUE.equals(job.getRecurring())) {
            return;
        }

        LocalDateTime nextRun;

        if (job.getCronExpression() != null) {
            try {
                nextRun = CronUtils.next(job.getCronExpression(), baseTime);
            } catch (Exception e) {
                job.setStatus(JobStatus.DEAD);
                job.setLastError("Invalid cron: " + e.getMessage());
                return;
            }
        } else {
            nextRun = baseTime.plusMinutes(1);
        }

        job.setNextRunAt(nextRun);
        job.setStatus(JobStatus.SCHEDULED);
    }
}