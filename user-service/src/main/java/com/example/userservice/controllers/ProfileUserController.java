package com.example.userservice.controllers;

import com.example.userservice.dtos.requests.UpdateUserProfileRequest;
import com.example.userservice.dtos.responses.UserProfileResponse;
import com.example.userservice.services.ProfileUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileUserController {

    private final ProfileUserService profileUserService;

    @GetMapping("/{email}")
    public ResponseEntity<UserProfileResponse> getProfile(@PathVariable String email) {
        return ResponseEntity.ok(profileUserService.getProfileByEmail(email));
    }

    @PatchMapping("/{email}")
    public ResponseEntity<UserProfileResponse> updateProfile(
            @PathVariable String email,
            @Valid @RequestBody UpdateUserProfileRequest request
    ) {
        return ResponseEntity.ok(profileUserService.updateProfile(email, request));
    }
}
