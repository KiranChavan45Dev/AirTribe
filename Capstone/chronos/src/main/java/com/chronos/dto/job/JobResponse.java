package com.chronos.dto.job;

import com.chronos.entity.enums.JobStatus;
import com.chronos.entity.enums.ScheduleType;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JobResponse {

    private UUID id;

    private String jobName;

    private String jobType;

    private JsonNode payload;

    private JobStatus status;

    private ScheduleType scheduleType;

    private String cronExpression;

    private LocalDateTime runAt;

    private LocalDateTime nextRunAt;

    private Integer retryCount;

    private Integer maxRetries;

    private Integer priority;

    private String lastError;

    private Boolean recurring;

    private LocalDateTime createdAt;
}