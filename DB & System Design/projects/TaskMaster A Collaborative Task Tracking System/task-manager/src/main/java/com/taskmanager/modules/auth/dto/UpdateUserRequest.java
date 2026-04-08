package com.taskmanager.modules.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateUserRequest {
    @NotBlank(message = "Full name cannot be blank")
    private String fullName;

    @Email(message = "Email should be valid")
    private String email;
}
