package com.taskmanager.modules.team.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TeamRequest {
    @NotBlank(message = "Team name is required")
    private String name;
    private String description;
}