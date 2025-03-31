package com.example.userservice.security.jwt.services;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "spring.jwt")
@Component
@Data
public class JwtProperties {
    private String secret;
    private String lifetime;
    private String refreshExpiration;
}
