package com.chronos.service;

import com.chronos.dto.job.*;
import com.chronos.entity.Job;
import com.chronos.entity.User;
import com.chronos.entity.enums.JobStatus;
import com.chronos.repository.JobRepository;
import com.chronos.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.security.access.AccessDeniedException;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JobServiceTest {

    @Mock
    JobRepository jobRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    ObjectMapper objectMapper;

    @InjectMocks
    JobService jobService;

    private User mockUser() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setUsername("test");
        return user;
    }

    @Test
    void shouldThrowIfJobNotFound() {
        when(jobRepository.findById(any()))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                jobService.getJob(UUID.randomUUID()));
    }

    @Test
    void shouldCancelJobSuccessfully() {

        User user = mockUser();

        Job job = new Job();
        job.setUser(user);
        job.setStatus(JobStatus.SCHEDULED);

        when(jobRepository.findById(any()))
                .thenReturn(Optional.of(job));

        // simulate current user via reflection/mocking SecurityUtils if needed

        jobService.cancelJob(UUID.randomUUID());

        assertEquals(JobStatus.CANCELLED, job.getStatus());
        verify(jobRepository).save(job);
    }

    @Test
    void shouldThrowWhenUnauthorized() {

        User owner = mockUser();
        owner.setId(UUID.randomUUID());

        Job job = new Job();
        job.setUser(owner);

        when(jobRepository.findById(any()))
                .thenReturn(Optional.of(job));

        assertThrows(AccessDeniedException.class, () ->
                jobService.getJob(UUID.randomUUID()));
    }

    @Test
    void shouldRescheduleJob() {

        User user = mockUser();

        Job job = new Job();
        job.setUser(user);
        job.setStatus(JobStatus.SCHEDULED);

        when(jobRepository.findById(any()))
                .thenReturn(Optional.of(job));
        when(jobRepository.save(any())).thenReturn(job);

        RescheduleJobRequest req = new RescheduleJobRequest();
        req.setNextRunAt(LocalDateTime.now().plusMinutes(10));

        jobService.rescheduleJob(UUID.randomUUID(), req);

        assertEquals(JobStatus.SCHEDULED, job.getStatus());
    }
}