package com.example.common.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
public class JwtProperties {
    private String secret;
    private int lifetimeMinutes;
    private int refreshExpirationDays;
}