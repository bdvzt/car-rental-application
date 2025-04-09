package com.example.userservice;

import com.example.userservice.entities.ERole;
import com.example.userservice.entities.Role;
import com.example.userservice.entities.User;
import com.example.userservice.repositories.RoleRepository;
import com.example.userservice.repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DataSeeder {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void seed() {
        seedRoles();
        seedAdmins();
    }

    private void seedRoles() {
        if (roleRepository.findByName(ERole.ROLE_USER).isEmpty()) {
            roleRepository.save(new Role(null, ERole.ROLE_USER));
        }
        if (roleRepository.findByName(ERole.ROLE_ADMIN).isEmpty()) {
            roleRepository.save(new Role(null, ERole.ROLE_ADMIN));
        }
    }

    private void seedAdmins() {
        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                .orElseThrow(() -> new RuntimeException("Admin role not found"));

        List<User> existingAdmins = userRepository.findAll().stream()
                .filter(user -> user.getRoles().contains(adminRole))
                .toList();

        int toCreate = 3 - existingAdmins.size();

        for (int i = 0; i < toCreate; i++) {
            String email = "admin" + (i + 1) + "@example.com";
            if (userRepository.findByEmail(email).isEmpty()) {
                User user = User.of(
                        UUID.randomUUID(),
                        "Admin",
                        "User" + (i + 1),
                        email,
                        passwordEncoder.encode("Password123"),
                        true,
                        LocalDateTime.now(),
                        Set.of(adminRole)
                );
                userRepository.save(user);
            }
        }
    }
}
