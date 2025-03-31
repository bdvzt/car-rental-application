package com.example.userservice.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class UserRegisterRequest {

    @NotBlank(message = "Имя обязательно")
    @Size(min = 1, max = 50)
    private String name;

    @NotBlank(message = "Фамилия обязательна")
    @Size(min = 1, max = 50)
    private String surname;

    @NotBlank(message = "Email обязателен")
    @Email(message = "Невалидный email")
    @Size(min = 5, max = 255)
    private String email;

    @NotBlank(message = "Пароль обязателен")
    @Size(min = 6, max = 255)
    private String password;
}
