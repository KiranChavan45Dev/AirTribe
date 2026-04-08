package com.taskmanager.modules.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserProfileResponse {
    private String username;
    private String email;
    private String fullName;
}
