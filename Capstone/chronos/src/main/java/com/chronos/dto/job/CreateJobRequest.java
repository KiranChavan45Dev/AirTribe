package com.chronos.dto.job;

import com.chronos.entity.enums.ScheduleType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDateTime;

@Data
public class CreateJobRequest {

    @NotBlank
    private String jobName;

    @NotBlank
    private String jobType;

    private String payload;

    @NonNull
    private ScheduleType scheduleType;

    private String cronExpression;

    private LocalDateTime runAt;

    @Min(1)
    private Integer maxRetries = 3;

    @Min(1)
    @Max(10)
    private Integer priority = 1;
}