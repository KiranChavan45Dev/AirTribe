package com.chronos.scheduler;

import com.chronos.entity.Job;
import com.chronos.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutorService;

@Service
@RequiredArgsConstructor
public class JobScheduler {

    private final JobService jobService;
    private final JobWorker jobWorker;
    private final ExecutorService jobExecutor;

    @Scheduled(fixedDelayString = "${scheduler.job-poll-interval}")
    public void pollAndExecuteJobs() {

        List<Job> jobs = jobService.claimJobs(
                LocalDateTime.now(),
                10
        );

        for (Job job : jobs) {

            jobExecutor.submit(() -> jobWorker.execute(job));
        }
    }
}