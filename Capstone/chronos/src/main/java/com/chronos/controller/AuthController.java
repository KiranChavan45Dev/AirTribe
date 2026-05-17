package com.chronos.controller;

import com.chronos.dto.auth.AuthResponse;
import com.chronos.dto.auth.LoginRequest;
import com.chronos.dto.auth.RegisterRequest;
import com.chronos.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Auth Controller", description = "APIs for authentication and authorization")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Register a new user")
    @PostMapping("/register")
    public AuthResponse register(
            @Valid @RequestBody RegisterRequest request
    ) {

        log.info(
                "API request received: register | username={} | email={}",
                request.getUsername(),
                request.getEmail()
        );

        AuthResponse response =
                authService.register(request);

        log.info(
                "API response sent: register successful | username={}",
                request.getUsername()
        );

        return response;
    }

    @Operation(summary = "Authenticate user and generate JWT token")
    @PostMapping("/login")
    public AuthResponse login(
            @Valid @RequestBody LoginRequest request
    ) {

        log.info(
                "API request received: login | username={}",
                request.getUsername()
        );

        AuthResponse response =
                authService.login(request);

        log.info(
                "API response sent: login successful | username={}",
                request.getUsername()
        );

        return response;
    }
}