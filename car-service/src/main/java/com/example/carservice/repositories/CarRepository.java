package com.example.carservice.repositories;

import com.example.carservice.entities.Car;
import dtos.responses.CarStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CarRepository extends JpaRepository<Car, UUID> {

    List<Car> findByStatus(CarStatus status);
}
