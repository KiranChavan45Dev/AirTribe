package com.taskmanager.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "jwt")
@Data
public class JwtConfig {
    private String secret;          // Secret key for signing
    private long expirationMs;      // Token expiration in milliseconds
    private String tokenPrefix;     // e.g., "Bearer "
    private String header;          // e.g., "Authorization"
}
