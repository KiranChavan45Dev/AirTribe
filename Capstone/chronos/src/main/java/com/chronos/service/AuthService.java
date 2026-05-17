package com.chronos.service;

import com.chronos.dto.auth.*;
import com.chronos.entity.User;
import com.chronos.entity.enums.UserRole;
import com.chronos.repository.UserRepository;
import com.chronos.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    public AuthResponse register(
            RegisterRequest request
    ) {

        log.info(
                "User registration started | username={} | email={}",
                request.getUsername(),
                request.getEmail()
        );

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .passwordHash(
                        passwordEncoder.encode(
                                request.getPassword()
                        )
                )
                .role(UserRole.USER)
                .build();

        log.debug(
                "User entity created | username={}",
                request.getUsername()
        );

        userRepository.save(user);

        log.info(
                "User saved successfully | userId={} | username={}",
                user.getId(),
                user.getUsername()
        );

        String token =
                jwtService.generateToken(user.getUsername());

        log.debug(
                "JWT token generated for registered user | username={}",
                user.getUsername()
        );

        return new AuthResponse(token);
    }

    public AuthResponse login(
            LoginRequest request
    ) {

        log.info(
                "Authentication request received | username={}",
                request.getUsername()
        );

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        log.info(
                "Authentication successful | username={}",
                request.getUsername()
        );

        String token =
                jwtService.generateToken(request.getUsername());

        log.debug(
                "JWT token generated after login | username={}",
                request.getUsername()
        );

        return new AuthResponse(token);
    }
}