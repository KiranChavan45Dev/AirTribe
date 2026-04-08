package com.taskmanager.modules.comment.service;

import com.taskmanager.modules.auth.entity.User;
import com.taskmanager.modules.comment.entity.Comment;
import com.taskmanager.modules.comment.repository.CommentRepository;
import com.taskmanager.modules.task.entity.Task;
import com.taskmanager.modules.task.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;

    public Comment addComment(Long taskId, User author, String content) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        Comment comment = Comment.builder()
                .task(task)
                .author(author)
                .content(content)
                .createdAt(LocalDateTime.now())
                .build();
        return commentRepository.save(comment);
    }

    public List<Comment> getCommentsByTask(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        List<Comment> comments = commentRepository.findByTask(task);
        if (comments.isEmpty()) {
            log.warn("No comments found for task with id: {}", taskId);
        }
        return comments;
    }
}
