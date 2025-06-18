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

                .route("car-service", r -> r
                        .path("/car",
                                "/car/**",
                                "/car-model",
                                "/car-model/**")
                        .filters(f -> f
                                .filter(jwtAuthenticationFilter)
                                .stripPrefix(0)
                        )
                        .uri("http://localhost:8082"))
                .route("booking-service", r -> r
                        .path("/bookings",
                                "/bookings/**")
                        .filters(f -> f
                                .filter(jwtAuthenticationFilter)
                                .stripPrefix(0)
                        )
                        .uri("http://localhost:8083"))
                .route("payment-service", r -> r
                        .path("/payment/**")
                        .filters(f -> f
                                .filter(jwtAuthenticationFilter)
                                .stripPrefix(0)
                        )
                        .uri("http://localhost:8084"))
                .build();
    }
}

