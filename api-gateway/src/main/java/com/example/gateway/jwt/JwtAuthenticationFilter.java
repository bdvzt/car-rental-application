package com.example.gateway.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements GatewayFilter {

    private final JwtUtils jwtUtils;
    private static final List<String> endpoints = List.of(
            "/auth/register",
            "/auth/login",
            "/auth/refresh",
            "/swagger",
            "/v3/api-docs");

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        boolean isPublic = endpoints
                .stream()
                .anyMatch(endpoint -> request.getURI().getPath().contains(endpoint));

        if (isPublic) {
            return chain.filter(exchange);
        }

        String token = request.getHeaders().getFirst("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            return onError(exchange, "нет заголовка авторизации");
        }

        token = token.replace("Bearer ", "");
        if (!jwtUtils.validateJwtToken(token)) {
            return onError(exchange, "невалидный токен");
        }

        return chain.filter(exchange);
    }

    private Mono<Void> onError(ServerWebExchange exchange, String message) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }
}
