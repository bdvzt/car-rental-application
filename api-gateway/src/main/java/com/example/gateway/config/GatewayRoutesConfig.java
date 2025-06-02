package com.example.gateway.config;

import com.example.gateway.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class GatewayRoutesConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("user-service", r -> r
                        .path("/profile",
                                "/auth/**",
                                "/admin/**")
                        .filters(f -> f
                                .filter(jwtAuthenticationFilter)
                                .stripPrefix(0)
                        )
                        .uri("http://localhost:8081"))
                .build();
    }
}

