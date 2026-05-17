package com.chronos.controller;

import com.chronos.dto.response.ApiResponse;
import com.chronos.dto.job.*;
import com.chronos.service.JobService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/jobs")
@RequiredArgsConstructor
@Tag(name = "Job Controller", description = "APIs for managing scheduled jobs")
public class JobController {

    private final JobService jobService;

    @Operation(summary = "Create a new job")
    @PostMapping
    public ApiResponse<JobResponse> createJob(@Valid @RequestBody CreateJobRequest request) {
        log.info("API: createJob");
        return ApiResponse.ok("Job created", jobService.createJob(request));
    }

    @Operation(summary = "Get all jobs for current user")
    @GetMapping
    public ApiResponse<List<JobResponse>> getMyJobs() {
        log.info("API: getMyJobs");
        return ApiResponse.ok(jobService.getMyJobs());
    }

    @Operation(summary = "Get job by ID")
    @GetMapping("/{id}")
    public ApiResponse<JobResponse> getJob(@PathVariable UUID id) {
        log.info("API: getJob {}", id);
        return ApiResponse.ok(jobService.getJob(id));
    }

    @Operation(summary = "Cancel a job")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> cancelJob(@PathVariable UUID id) {
        log.info("API: cancelJob {}", id);
        jobService.cancelJob(id);
        return ApiResponse.ok("Job cancelled", null);
    }

    @Operation(summary = "Reschedule a job")
    @PutMapping("/{id}/reschedule")
    public ApiResponse<JobResponse> rescheduleJob(
            @PathVariable UUID id,
            @RequestBody RescheduleJobRequest request
    ) {
        log.info("API: rescheduleJob {}", id);
        return ApiResponse.ok("Job rescheduled", jobService.rescheduleJob(id, request));
    }
}