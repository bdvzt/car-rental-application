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

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthUserController {

    private final AuthUserService authUserService;
    private final RefreshTokenService refreshTokenService;

    private final JwtUtils jwtUtils;

    @PostMapping("/register")
    public ResponseEntity<MessageResponse> register(@Valid @RequestBody RegisterUserRequest request) {
        MessageResponse response = authUserService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginUserRequest request) {
        TokenResponse jwtResponse = authUserService.login(request);
        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtUtils.generateRefreshToken(user.getEmail());
                    return ResponseEntity.ok(new TokenRefreshResponse());
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                        "Refresh token is not in database!"));
    }
}
