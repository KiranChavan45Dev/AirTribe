package com.taskmanager.modules.attachment.repository;

import com.taskmanager.modules.attachment.entity.Attachment;
import com.taskmanager.modules.task.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    List<Attachment> findByTask(Task task);
}
