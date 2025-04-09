package com.example.userservice.controllers;

import com.example.userservice.dtos.requests.LoginUserRequest;
import com.example.userservice.dtos.requests.RegisterUserRequest;
import com.example.userservice.dtos.requests.TokenRefreshRequest;
import com.example.userservice.dtos.responses.MessageResponse;
import com.example.userservice.dtos.responses.TokenRefreshResponse;
import com.example.userservice.dtos.responses.TokenResponse;
import com.example.userservice.entities.RefreshToken;
import com.example.userservice.exeptions.TokenRefreshException;
import com.example.userservice.security.jwt.services.JwtUtils;
import com.example.userservice.services.AuthUserService;
import com.example.userservice.services.RefreshTokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthUserController {

    private final AuthUserService authUserService;
    private final RefreshTokenService refreshTokenService;

    private final JwtUtils jwtUtils;

    @Operation(summary = "Registration",
            description = "Регистрация пользователя",
            security = @SecurityRequirement(name = ""))
    @PostMapping("/register")
    public ResponseEntity<MessageResponse> register(@Valid @RequestBody RegisterUserRequest request) {
        MessageResponse response = authUserService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Login",
            description = "Авторизация пользователя",
            security = @SecurityRequirement(name = ""))
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginUserRequest request) {
        TokenResponse jwtResponse = authUserService.login(request);
        return ResponseEntity.ok(jwtResponse);
    }

    @Operation(summary = "Get access token",
            description = "Using refresh token, user gets access token",
            security = @SecurityRequirement(name = ""))
    @PostMapping("/refreshtoken")
    public ResponseEntity<TokenRefreshResponse> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
        return ResponseEntity.ok(
                refreshTokenService.generateAccessTokenFromRefreshToken(request.getRefreshToken())
        );
    }
}
