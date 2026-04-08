package com.taskmanager.modules.attachment.service;

import com.taskmanager.modules.attachment.entity.Attachment;
import com.taskmanager.modules.attachment.repository.AttachmentRepository;
import com.taskmanager.modules.task.entity.Task;
import com.taskmanager.modules.task.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AttachmentService {

    private final AttachmentRepository attachmentRepository;
    private final TaskRepository taskRepository;

    @Value("${attachments.upload-dir:uploads}")
    private String uploadDir;

    private static final Logger logger = LoggerFactory.getLogger(AttachmentService.class);
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB limit, change as needed

    public Attachment saveAttachment(Long taskId, MultipartFile file) throws IOException {
        // Validate file size
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new RuntimeException("File size exceeds the maximum limit of 5MB.");
        }

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        // Ensure upload directory exists
        File dir = new File(uploadDir);
        if (!dir.exists()) dir.mkdirs();

        // Generate a unique filename using UUID to avoid conflicts
        String filePath = uploadDir + File.separator + UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        File dest = new File(filePath);

        // Save the file to disk
        file.transferTo(dest);

        // Save attachment metadata in DB
        Attachment attachment = Attachment.builder()
                .task(task)
                .fileName(file.getOriginalFilename())
                .filePath(filePath)
                .uploadedAt(LocalDateTime.now())
                .build();

        logger.info("File uploaded successfully: {}", file.getOriginalFilename());

        return attachmentRepository.save(attachment);
    }

    public List<Attachment> getAttachmentsByTask(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        return attachmentRepository.findByTask(task);
    }
}