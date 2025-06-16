package com.example.common.security;

import lombok.Data;

@Data
public class JwtProperties {
    private String secret;
    private int lifetimeMinutes;
    private int refreshExpirationDays;
}