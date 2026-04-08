package com.taskmanager.modules.attachment.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.taskmanager.modules.task.entity.Task;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "attachments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Attachment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String filePath;

    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    @JsonIgnore
    private Task task;

    @Column(nullable = false)
    private LocalDateTime uploadedAt;
}
