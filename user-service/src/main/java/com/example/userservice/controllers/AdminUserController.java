package com.example.userservice.controllers;

import com.example.userservice.dtos.responses.UserProfileResponse;
import com.example.userservice.services.AdminUserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@PreAuthorize("hasRole('ROLE_ADMIN')")
@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "управление пользователями", description = "для админа")
public class AdminUserController {

    private final AdminUserService adminUserService;

    @GetMapping
    public ResponseEntity<List<UserProfileResponse>> getAllUsers() {
        return ResponseEntity.ok(adminUserService.getAllUsers());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserProfileResponse> getUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(adminUserService.getUserById(userId));
    }

    @PatchMapping("/{userId}/active")
    public ResponseEntity<Void> updateActiveStatus(
            @PathVariable UUID userId,
            @RequestParam boolean active
    ) {
        adminUserService.setUserActiveStatus(userId, active);
        return ResponseEntity.noContent().build();
    }
}
