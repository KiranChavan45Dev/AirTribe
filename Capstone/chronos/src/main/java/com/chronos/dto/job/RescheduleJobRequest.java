package com.chronos.dto.job;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RescheduleJobRequest {

    private LocalDateTime nextRunAt;
}