package com.taskmanager.modules.auth.service;

import com.taskmanager.modules.auth.dto.AuthResponse;
import com.taskmanager.modules.auth.dto.LoginRequest;
import com.taskmanager.modules.auth.dto.RegisterRequest;
import com.taskmanager.modules.auth.entity.Role;
import com.taskmanager.modules.auth.entity.User;
import com.taskmanager.modules.auth.repository.RoleRepository;
import com.taskmanager.modules.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username is already taken");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email is already in use");
        }

        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("Default USER role not found"));

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .roles(Set.of(userRole))
                .build();

        userRepository.save(user);

        String token = jwtService.generateToken(user.getUsername(),user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()));

        return new AuthResponse(token, "Bearer");
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsernameOrEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByUsernameOrEmail(
                        request.getUsernameOrEmail(),
                        request.getUsernameOrEmail()
                )
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        String token = jwtService.generateToken(user.getUsername(),user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()));

        return new AuthResponse(token, "Bearer");
    }
}
