package com.chronos.dto.job;

import com.chronos.entity.enums.ScheduleType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateJobRequest {

    private String jobName;

    private String jobType;

    private String payload;

    private ScheduleType scheduleType;

    private String cronExpression;

    private LocalDateTime runAt;

    private Integer maxRetries = 3;

    private Integer priority = 1;
}