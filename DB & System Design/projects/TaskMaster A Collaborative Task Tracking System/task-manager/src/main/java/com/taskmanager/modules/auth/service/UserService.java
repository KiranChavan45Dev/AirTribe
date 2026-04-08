package com.taskmanager.modules.auth.service;

import com.taskmanager.modules.auth.dto.UpdateUserRequest;
import com.taskmanager.modules.auth.dto.UserProfileResponse;
import com.taskmanager.modules.auth.entity.User;
import com.taskmanager.modules.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails userDetails) {
            username = userDetails.getUsername();
        } else {
            username = principal.toString();
        }
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }
    @Transactional(readOnly = true)
    public UserProfileResponse getProfile() {
        User user = getCurrentUser();
        return new UserProfileResponse(user.getUsername(), user.getEmail(), user.getFullName());
    }

    @Transactional
    public UserProfileResponse updateProfile(UpdateUserRequest request) {
        User user = getCurrentUser();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());

        userRepository.save(user);
        return new UserProfileResponse(user.getUsername(), user.getEmail(), user.getFullName());
    }
}
