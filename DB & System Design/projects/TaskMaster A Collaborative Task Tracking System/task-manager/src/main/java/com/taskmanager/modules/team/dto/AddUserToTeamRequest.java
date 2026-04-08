package com.taskmanager.modules.team.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddUserToTeamRequest {
    @NotNull(message = "User ID is required")
    private Long userId;
}