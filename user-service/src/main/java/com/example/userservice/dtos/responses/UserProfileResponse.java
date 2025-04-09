package com.example.userservice.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileResponse {
    private UUID id;
    private String name;
    private String surname;
    private String email;
    private Set<String> roles;
    private LocalDateTime registrationDate;
    private boolean isActive;
}