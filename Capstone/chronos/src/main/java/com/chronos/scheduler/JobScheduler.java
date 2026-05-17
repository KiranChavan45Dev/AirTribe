package com.chronos.scheduler;

import com.chronos.entity.Job;
import com.chronos.entity.enums.JobStatus;
import com.chronos.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutorService;

@Service
@RequiredArgsConstructor
public class JobScheduler {

    private final JobRepository jobRepository;
    private final JobWorker jobWorker;
    private final ExecutorService jobExecutor;

    @Value("${scheduler.job-poll-interval}")
    private long jobPollInterval;

    @Transactional
    @Scheduled(fixedDelayString = "${scheduler.job-poll-interval}")
    public void pollAndExecuteJobs() {

        List<Job> jobs = jobRepository.findJobsToExecute(
                LocalDateTime.now(),
                10
        );

        for (Job job : jobs) {

            job.setStatus(JobStatus.RUNNING);
            jobRepository.save(job);

            jobExecutor.submit(() -> jobWorker.execute(job));
        }
    }
}