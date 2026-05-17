package com.chronos.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank
    @Size(min = 5, max = 40)
    private String username;

    @Email
    private String email;

    @NotBlank
    @Size(min = 6, max = 100)
    private String password;
}
