package com.chronos.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.junit.jupiter.Container;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
class JobIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16")
                    .withDatabaseName("chronos")
                    .withUsername("test")
                    .withPassword("test");

    @Autowired
    MockMvc mockMvc;

    @DynamicPropertySource
    static void props(DynamicPropertyRegistry r) {
        r.add("spring.datasource.url", postgres::getJdbcUrl);
        r.add("spring.datasource.username", postgres::getUsername);
        r.add("spring.datasource.password", postgres::getPassword);
    }

    @Test
    void shouldCreateJob() throws Exception {

        mockMvc.perform(post("/jobs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                      "jobName": "test-job",
                                      "jobType": "EMAIL",
                                      "scheduleType": "ONCE",
                                      "runAt": "2026-01-01T10:00:00",
                                      "maxRetries": 3,
                                      "priority": 1
                                    }
                                """))
                .andExpect(status().isOk());
    }
}