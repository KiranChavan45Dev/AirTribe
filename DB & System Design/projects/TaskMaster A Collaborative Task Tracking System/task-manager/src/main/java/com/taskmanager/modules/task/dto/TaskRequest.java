package com.taskmanager.modules.task.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskRequest {
    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    private LocalDateTime dueDate;

    private Long assignedToUserId; // Optional: assign to a user

    private Long teamId; // Optional: assign task within a team
}