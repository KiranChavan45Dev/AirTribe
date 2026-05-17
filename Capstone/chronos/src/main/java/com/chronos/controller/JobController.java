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
    public ApiResponse<JobResponse> createJob(
            @Valid @RequestBody CreateJobRequest request
    ) {

        log.info(
                "API request received: createJob | jobName={} | jobType={}",
                request.getJobName(),
                request.getJobType()
        );

        ApiResponse<JobResponse> response =
                ApiResponse.ok(
                        "Job created",
                        jobService.createJob(request)
                );

        log.info(
                "API response sent: createJob completed | jobName={}",
                request.getJobName()
        );

        return response;
    }

    @Operation(summary = "Get all jobs for current user")
    @GetMapping
    public ApiResponse<List<JobResponse>> getMyJobs() {

        log.info("API request received: getMyJobs");

        ApiResponse<List<JobResponse>> response =
                ApiResponse.ok(jobService.getMyJobs());

        log.info("API response sent: getMyJobs completed");

        return response;
    }

    @Operation(summary = "Get job by ID")
    @GetMapping("/{id}")
    public ApiResponse<JobResponse> getJob(
            @PathVariable UUID id
    ) {

        log.info(
                "API request received: getJob | jobId={}",
                id
        );

        ApiResponse<JobResponse> response =
                ApiResponse.ok(jobService.getJob(id));

        log.info(
                "API response sent: getJob completed | jobId={}",
                id
        );

        return response;
    }

    @Operation(summary = "Cancel a job")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> cancelJob(
            @PathVariable UUID id
    ) {

        log.info(
                "API request received: cancelJob | jobId={}",
                id
        );

        jobService.cancelJob(id);

        log.info(
                "API response sent: cancelJob completed | jobId={}",
                id
        );

        return ApiResponse.ok("Job cancelled", null);
    }

    @Operation(summary = "Reschedule a job")
    @PutMapping("/{id}/reschedule")
    public ApiResponse<JobResponse> rescheduleJob(
            @PathVariable UUID id,
            @RequestBody RescheduleJobRequest request
    ) {

        log.info(
                "API request received: rescheduleJob | jobId={} | nextRunAt={}",
                id,
                request.getNextRunAt()
        );

        ApiResponse<JobResponse> response =
                ApiResponse.ok(
                        "Job rescheduled",
                        jobService.rescheduleJob(id, request)
                );

        log.info(
                "API response sent: rescheduleJob completed | jobId={}",
                id
        );

        return response;
    }
}