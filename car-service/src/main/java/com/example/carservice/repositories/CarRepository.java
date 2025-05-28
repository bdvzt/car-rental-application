package com.example.carservice.repositories;

import com.example.carservice.entities.Car;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CarRepository extends JpaRepository<Car, UUID> {

    Optional<Car> findByCarNumber(String carNumber);
    boolean existsByCarNumber(String carNumber);
}
