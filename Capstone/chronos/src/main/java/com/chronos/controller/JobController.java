package com.chronos.controller;

import com.chronos.dto.job.*;
import com.chronos.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    @PostMapping
    public JobResponse createJob(
            @RequestBody CreateJobRequest request
    ) {
        return jobService.createJob(request);
    }

    @GetMapping
    public List<JobResponse> getMyJobs() {
        return jobService.getMyJobs();
    }

    @GetMapping("/{id}")
    public JobResponse getJob(
            @PathVariable UUID id
    ) {
        return jobService.getJob(id);
    }

    @DeleteMapping("/{id}")
    public void cancelJob(
            @PathVariable UUID id
    ) {
        jobService.cancelJob(id);
    }

    @PutMapping("/{id}/reschedule")
    public JobResponse rescheduleJob(
            @PathVariable UUID id,
            @RequestBody RescheduleJobRequest request
    ) {
        return jobService.rescheduleJob(id, request);
    }
}