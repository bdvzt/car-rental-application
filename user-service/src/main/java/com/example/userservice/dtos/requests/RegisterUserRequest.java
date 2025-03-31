package com.example.userservice.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class RegisterUserRequest {

    @NotBlank(message = "Имя обязательно")
    @Size(min = 1, max = 50, message = "Имя должно содержать от 1 до 50 символов.")
    private String name;

    @NotBlank(message = "Фамилия обязательна")
    @Size(min = 1, max = 50, message = "Фамилия должна содержать от 1 до 50 символов.")
    private String surname;

    @NotBlank(message = "Email обязателен")
    @Email(message = "Невалидный email")
    @Size(min = 5, max = 255)
    private String email;

    @NotBlank(message = "Пароль обязателен")
    @Size(min = 6, max = 255, message = "Пароль должен быть от 6 до 255 символов.")
    private String password;
}
