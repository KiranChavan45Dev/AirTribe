    package com.taskmanager.modules.attachment.controller;

    import com.taskmanager.modules.attachment.entity.Attachment;
    import com.taskmanager.modules.attachment.service.AttachmentService;
    import lombok.RequiredArgsConstructor;
    import org.springframework.core.io.FileSystemResource;
    import org.springframework.core.io.Resource;
    import org.springframework.http.HttpHeaders;
    import org.springframework.http.MediaType;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;
    import org.springframework.web.multipart.MultipartFile;

    import java.io.File;
    import java.io.IOException;
    import java.util.List;

    @RestController
    @RequestMapping("/api/tasks/attachments")
    @RequiredArgsConstructor
    public class AttachmentController {

        private final AttachmentService attachmentService;

        @PostMapping("/{taskId}")
        public Attachment uploadAttachment(@PathVariable Long taskId, @RequestParam("file") MultipartFile file) throws IOException {
            return attachmentService.saveAttachment(taskId, file);
        }

        @GetMapping("/{taskId}")
        public List<Attachment> getAttachments(@PathVariable Long taskId) {
            return attachmentService.getAttachmentsByTask(taskId);
        }

        @GetMapping("/download/{attachmentId}")
        public ResponseEntity<Resource> downloadAttachment(@PathVariable Long attachmentId) {
            Attachment attachment = attachmentService.getAttachmentsByTask(attachmentId)
                    .stream()
                    .filter(a -> a.getId().equals(attachmentId))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Attachment not found"));

            File file = new File(attachment.getFilePath());
            if (!file.exists()) throw new RuntimeException("File not found");

            Resource resource = new FileSystemResource(file);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + attachment.getFileName() + "\"")
                    .body(resource);
        }

    }
