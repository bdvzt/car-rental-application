package com.example.userservice.dtos.responses;

import com.example.common.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private UUID id;
    private String name;
    private String surname;
    private String email;
    private Role role;
    private LocalDateTime registrationDate;
}