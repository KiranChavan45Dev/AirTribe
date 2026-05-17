package com.chronos.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Chronos Scheduler API",
                version = "1.0",
                description = "Distributed Job Scheduler API"
        )
)
public class OpenApiConfig {
}