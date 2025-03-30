package com.example.userservice.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class User {
    @Id
    private UUID id;

    @Size(min = 1, max = 50, message = "Имя должно содержать от 1 до 50 символов.")
    @NotBlank(message = "Поле имя не должно быть пустым.")
    @Column(name = "user_name")
    private String name;

    @Size(min = 1, max = 50, message = "Фамилия должна содержать от 1 до 50 символов.")
    @NotBlank(message = "Поле фамилия не должно быть пустым.")
    @Column(name = "user_surname")
    private String surname;


    @Column(name = "user_email", unique = true)
    @Size(min = 5, max = 255, message = "Почта должна содержать от 5 до 255 символов.")
    @NotBlank(message = "Поле почта не должно быть пустым.")
    @Email(message = "Ввели невалидную почту.")
    private String email;

    @Size(min = 6, max = 255, message = "Пароль состоит минимум из 6 и максимум из 255 символов.")
    @Column(name = "user_password")
    private String password;

    @Column(name = "user_phone", unique = true)
    private Long phone;

    @Column(nullable = false)
    private LocalDateTime registrationDate = LocalDateTime.now();
}
