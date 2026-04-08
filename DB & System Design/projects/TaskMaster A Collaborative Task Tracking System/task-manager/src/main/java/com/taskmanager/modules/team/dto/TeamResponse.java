package com.taskmanager.modules.team.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
public class TeamResponse {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private Set<UserSummary> members;

    @Data
    @AllArgsConstructor
    public static class UserSummary {
        private Long id;
        private String username;
        private String email;
    }
}