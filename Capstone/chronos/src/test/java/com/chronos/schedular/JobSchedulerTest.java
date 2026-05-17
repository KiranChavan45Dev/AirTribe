package com.chronos.schedular;

import com.chronos.entity.Job;
import com.chronos.scheduler.JobScheduler;
import com.chronos.scheduler.JobWorker;
import com.chronos.service.JobService;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutorService;

import static org.mockito.Mockito.*;

class JobSchedulerTest {

    @Mock
    JobService jobService;
    @Mock
    JobWorker jobWorker;
    @Mock
    ExecutorService executor;

    @InjectMocks
    JobScheduler scheduler;

    @Test
    void shouldClaimAndDispatchJobs() {

        Job job = new Job();
        job.setJobName("test");

        when(jobService.claimJobs(any(), anyInt()))
                .thenReturn(List.of(job));

        scheduler.pollAndExecuteJobs();

        verify(executor, times(1))
                .submit(any(Runnable.class));
    }
}