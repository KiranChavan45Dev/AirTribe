package com.taskmanager.modules.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentRequest {
    @NotNull(message = "Task ID is required")
    private Long taskId;

    @NotBlank(message = "Content cannot be empty")
    @Size(max = 1000, message = "Content is too long")
    private String content;
}