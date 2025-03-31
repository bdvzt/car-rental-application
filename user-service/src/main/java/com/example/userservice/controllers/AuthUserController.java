package com.example.userservice.controllers;

import com.example.userservice.dtos.requests.LoginUserRequest;
import com.example.userservice.dtos.requests.RegisterUserRequest;
import com.example.userservice.dtos.responses.MessageResponse;
import com.example.userservice.dtos.responses.TokenResponse;
import com.example.userservice.services.AuthUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthUserController {

    private static final Logger logger = LoggerFactory.getLogger(AuthUserController.class);
    private final AuthUserService authUserService;

    @PostMapping("/register")
    public ResponseEntity<MessageResponse> register(@Valid @RequestBody RegisterUserRequest request) {
        logger.info("📥 POST /auth/register - register method called");
        MessageResponse response = authUserService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginUserRequest request) {
        logger.info("📥 POST /auth/login - login method called");
        TokenResponse jwtResponse = authUserService.login(request);
        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/test")
    public ResponseEntity<String> test() {
        logger.info("📥 POST /auth/test - test method called");
        return ResponseEntity.ok("OK");
    }
}
