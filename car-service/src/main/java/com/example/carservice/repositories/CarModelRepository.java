package com.example.carservice.repositories;


import com.example.carservice.entities.CarModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CarModelRepository extends JpaRepository<CarModel, UUID> {

    List<CarModel> findByBrandIgnoreCase(String brand);
    List<CarModel> findByModelIgnoreCase(String model);
    boolean existsByBrandAndModelAndYear(String brand, String model, Integer year);
}
