package com.taskmanager.modules.task.dto;


import com.taskmanager.modules.attachment.dto.AttachmentResponse;
import com.taskmanager.modules.comment.dto.CommentResponse;
import com.taskmanager.modules.task.entity.Task;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class TaskResponse {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime dueDate;
    private Task.TaskStatus status;
    private Long assignedToUserId;
    private String assignedToUsername;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<AttachmentResponse> attachments;
    private List<CommentResponse> comments;
}