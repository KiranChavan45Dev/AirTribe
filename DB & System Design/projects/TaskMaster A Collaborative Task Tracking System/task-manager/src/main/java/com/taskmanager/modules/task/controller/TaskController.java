package com.taskmanager.modules.task.controller;


import com.taskmanager.modules.attachment.dto.AttachmentResponse;
import com.taskmanager.modules.attachment.entity.Attachment;
import com.taskmanager.modules.attachment.service.AttachmentService;
import com.taskmanager.modules.task.dto.TaskRequest;
import com.taskmanager.modules.task.dto.TaskResponse;
import com.taskmanager.modules.task.dto.TaskUpdateRequest;
import com.taskmanager.modules.task.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final AttachmentService attachmentService;

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody TaskRequest request) {
        return ResponseEntity.ok(taskService.createTask(request));
    }

    @GetMapping
    public ResponseEntity<List<TaskResponse>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable Long id, @RequestBody TaskUpdateRequest request) {
        return ResponseEntity.ok(taskService.updateTask(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    // Upload attachment to a task
    @PostMapping("/{taskId}/attachments")
    public ResponseEntity<String> uploadAttachment(
            @PathVariable Long taskId,
            @RequestParam("file") MultipartFile file) throws IOException {

        attachmentService.saveAttachment(taskId, file);
        return ResponseEntity.ok("File uploaded successfully");
    }

    // List attachments of a task
    @GetMapping("/{taskId}/attachments")
    public ResponseEntity<List<AttachmentResponse>> getAttachments(@PathVariable Long taskId) {
        List<AttachmentResponse> attachments = attachmentService.getAttachmentsByTask(taskId).stream()
                .map(a -> new AttachmentResponse(a.getId(), a.getFileName(), a.getFilePath(), a.getUploadedAt()))
                .toList();
        return ResponseEntity.ok(attachments);
    }
}
