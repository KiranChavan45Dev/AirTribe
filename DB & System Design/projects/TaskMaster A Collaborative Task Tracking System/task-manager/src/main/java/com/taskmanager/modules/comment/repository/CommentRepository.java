package com.taskmanager.modules.comment.repository;

import com.taskmanager.modules.comment.entity.Comment;
import com.taskmanager.modules.task.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByTask(Task task);
}