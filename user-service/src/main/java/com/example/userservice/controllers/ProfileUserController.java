package com.example.userservice.controllers;

import com.example.userservice.dtos.requests.UpdateUserProfileRequest;
import com.example.userservice.dtos.responses.UserProfileResponse;
import com.example.userservice.security.services.UserDetailsImpl;
import com.example.userservice.services.AdminUserService;
import com.example.userservice.services.AuthUserService;
import com.example.userservice.services.ProfileUserService;
import dtos.responses.ResponseDTO;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "управление профилем", description = "крудилка для профиля")
public class ProfileUserController {

    private final ProfileUserService profileUserService;
    private final AuthUserService authUserService;

    @GetMapping
    public ResponseEntity<UserProfileResponse> getProfile(Authentication authentication) {
        return ResponseEntity.ok(profileUserService.getProfile(authentication));
    }

    @PatchMapping
    public ResponseEntity<ResponseDTO> updateProfile(
            Authentication authentication,
            @Valid @RequestBody UpdateUserProfileRequest request
    ) {
        profileUserService.updateProfile(authentication, request);
        return ResponseEntity.ok(new ResponseDTO(200, "Профиль успешно обновлен"));
    }

    @DeleteMapping
    public ResponseEntity<ResponseDTO> deactivateProfile(Authentication authentication) {
        profileUserService.deactivateProfile(authentication);
        return ResponseEntity.ok(new ResponseDTO(200, "Профиль успешно деактивирован"));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        authUserService.logout(userDetails.getId());
        return ResponseEntity.ok(new ResponseDTO(200, "Вы успешно вышли из системы"));
    }
}
