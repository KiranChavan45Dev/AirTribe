package com.taskmanager.modules.comment.controller;

import com.taskmanager.modules.auth.entity.User;
import com.taskmanager.modules.comment.dto.CommentRequest;
import com.taskmanager.modules.comment.dto.CommentResponse;
import com.taskmanager.modules.comment.entity.Comment;
import com.taskmanager.modules.comment.service.CommentService;
import com.taskmanager.modules.auth.repository.UserRepository; // Add the UserRepository import
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tasks/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final UserRepository userRepository; // Inject UserRepository

    @PostMapping
    public CommentResponse addComment(@RequestBody CommentRequest request,
                                      @AuthenticationPrincipal UserDetails userDetails) {
        // Check if the user is authenticated
        if (userDetails == null) {
            throw new IllegalArgumentException("User not authenticated");
        }

        // Fetch the User entity from the database using the username
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Add the comment
        Comment comment = commentService.addComment(request.getTaskId(), user, request.getContent());

        // Return the comment response
        return CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .authorName(comment.getAuthor().getUsername())
                .createdAt(comment.getCreatedAt())
                .build();
    }

    @GetMapping("/{taskId}")
    public List<CommentResponse> getComments(@PathVariable Long taskId) {
        // Fetch and return the list of comments for the given task
        return commentService.getCommentsByTask(taskId)
                .stream()
                .map(comment -> CommentResponse.builder()
                        .id(comment.getId())
                        .content(comment.getContent())
                        .authorName(comment.getAuthor().getUsername())
                        .createdAt(comment.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }
}