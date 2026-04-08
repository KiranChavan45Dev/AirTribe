package com.taskmanager.modules.task.service;

import com.taskmanager.modules.attachment.dto.AttachmentResponse;
import com.taskmanager.modules.auth.entity.User;
import com.taskmanager.modules.auth.repository.UserRepository;
import com.taskmanager.modules.comment.dto.CommentResponse;
import com.taskmanager.modules.task.dto.TaskRequest;
import com.taskmanager.modules.task.dto.TaskResponse;
import com.taskmanager.modules.task.dto.TaskUpdateRequest;
import com.taskmanager.modules.task.entity.Task;
import com.taskmanager.modules.task.repository.TaskRepository;
import com.taskmanager.modules.team.entity.Team;
import com.taskmanager.modules.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;

    /**
     * Map Task entity to TaskResponse DTO including attachments and comments
     */
    private TaskResponse mapToResponse(Task task) {
        Long assignedId = task.getAssignedTo() != null ? task.getAssignedTo().getId() : null;
        String assignedUsername = task.getAssignedTo() != null ? task.getAssignedTo().getUsername() : null;

        List<AttachmentResponse> attachments = task.getAttachments() != null
                ? task.getAttachments().stream()
                .map(a -> new AttachmentResponse(a.getId(), a.getFileName(), a.getFilePath(), a.getUploadedAt()))
                .collect(Collectors.toList())
                : List.of();

        List<CommentResponse> comments = task.getComments() != null
                ? task.getComments().stream()
                .map(c -> {
                    // Local variable ensures effectively final for lambda
                    String authorName = c.getAuthor() != null ? c.getAuthor().getUsername() : "Unknown";
                    return CommentResponse.builder()
                            .id(c.getId())
                            .content(c.getContent())
                            .authorName(authorName)
                            .createdAt(c.getCreatedAt())
                            .build();
                })
                .collect(Collectors.toList())
                : List.of();

        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .dueDate(task.getDueDate())
                .status(task.getStatus())
                .assignedToUserId(assignedId)
                .assignedToUsername(assignedUsername)
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .attachments(attachments)
                .comments(comments)
                .build();
    }

    @Transactional(readOnly = true)
    public List<TaskResponse> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TaskResponse getTaskById(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));
        return mapToResponse(task);
    }

    @Transactional
    public TaskResponse updateTask(Long taskId, TaskUpdateRequest request) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));

        if (request.getTitle() != null) task.setTitle(request.getTitle());
        if (request.getDescription() != null) task.setDescription(request.getDescription());
        if (request.getDueDate() != null) task.setDueDate(request.getDueDate());
        if (request.getStatus() != null) task.setStatus(request.getStatus());

        if (request.getAssignedToUserId() != null) {
            User user = userRepository.findById(request.getAssignedToUserId())
                    .orElseThrow(() -> new IllegalArgumentException("Assigned user not found"));
            task.setAssignedTo(user);
        }

        return mapToResponse(taskRepository.save(task));
    }

    @Transactional
    public void deleteTask(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));
        taskRepository.delete(task);
    }

    @Transactional
    public TaskResponse createTask(TaskRequest request) {
        User assignedUser = null;

        if (request.getAssignedToUserId() != null) {
            assignedUser = userRepository.findById(request.getAssignedToUserId())
                    .orElseThrow(() -> new IllegalArgumentException("Assigned user not found"));

            if (request.getTeamId() != null) {
                Team team = teamRepository.findById(request.getTeamId())
                        .orElseThrow(() -> new IllegalArgumentException("Team not found"));

                final Long assignedUserId = assignedUser.getId();

                boolean isMember = team.getMembers() != null
                        && team.getMembers().stream()
                        .anyMatch(m -> m.getUser() != null && m.getUser().getId().equals(assignedUserId));

                if (!isMember) {
                    throw new IllegalArgumentException("User is not a member of the team");
                }
            }
        }

        Task task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .dueDate(request.getDueDate())
                .assignedTo(assignedUser)
                .build();

        return mapToResponse(taskRepository.save(task));
    }
}