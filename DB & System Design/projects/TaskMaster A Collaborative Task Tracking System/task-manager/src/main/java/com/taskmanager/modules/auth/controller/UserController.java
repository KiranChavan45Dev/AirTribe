package com.taskmanager.modules.auth.controller;

import com.taskmanager.modules.auth.dto.UpdateUserRequest;
import com.taskmanager.modules.auth.dto.UserProfileResponse;
import com.taskmanager.modules.auth.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getProfile() {
        return ResponseEntity.ok(userService.getProfile());
    }

    @PutMapping("/me")
    public ResponseEntity<UserProfileResponse> updateProfile(@Valid @RequestBody UpdateUserRequest request) {
        return ResponseEntity.ok(userService.updateProfile(request));
    }
}
