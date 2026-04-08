package com.taskmanager.modules.task.dto;


import com.taskmanager.modules.task.entity.Task;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskUpdateRequest {
    private String title;
    private String description;
    private LocalDateTime dueDate;
    private Task.TaskStatus status;
    private Long assignedToUserId;
}