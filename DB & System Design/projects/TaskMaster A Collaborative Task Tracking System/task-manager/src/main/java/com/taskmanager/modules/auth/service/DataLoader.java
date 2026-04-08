package com.taskmanager.modules.auth.service;

import com.taskmanager.modules.auth.entity.Role;
import com.taskmanager.modules.auth.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) {
        if(roleRepository.count() == 0) {
            roleRepository.save(Role.builder().name("USER").build());
            roleRepository.save(Role.builder().name("ADMIN").build());
        }
    }
}