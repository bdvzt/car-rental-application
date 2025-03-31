package com.example.userservice.dtos.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginUserRequest {

    @NotBlank(message = "Email обязателен")
    @Email(message = "Невалидный email")
    private String email;

    @NotBlank(message = "Пароль обязателен")
    private String password;
}
