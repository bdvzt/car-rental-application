package com.example.carservice.services;

import com.example.carservice.dtos.requests.CarModelRequest;
import com.example.carservice.dtos.requests.CarModelRequest;
import com.example.carservice.dtos.responses.CarModelDTO;
import com.example.carservice.entities.CarModel;
import com.example.carservice.repositories.CarModelRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CarModelService {

    private final CarModelRepository carModelRepository;

    public List<CarModelDTO> getAllCarModels() {
        return carModelRepository.findAll().stream()
                .map(this::mapToDTO)
                .toList();
    }

    public CarModelDTO getCarModelById(UUID id) {
        CarModel model = carModelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Car model not found"));
        return mapToDTO(model);
    }

    public CarModelDTO createCarModel(CarModelRequest request) {
        CarModel model = new CarModel();
        model.setBrand(request.getBrand());
        model.setModel(request.getModel());
        model.setYear(request.getYear());
        model.setColor(request.getColor());
        model.setCreatedAt(LocalDateTime.now());
        model.setCreatedBy("system"); // или получить из securityContext
        return mapToDTO(carModelRepository.save(model));
    }

    public CarModelDTO updateCarModel(UUID id, CarModelRequest request) {
        CarModel model = carModelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Car model not found"));
        model.setBrand(request.getBrand());
        model.setModel(request.getModel());
        model.setYear(request.getYear());
        model.setColor(request.getColor());
        model.setUpdatedAt(LocalDateTime.now());
        return mapToDTO(carModelRepository.save(model));
    }

    public void deleteCarModel(UUID id) {
        carModelRepository.deleteById(id);
    }

    private CarModelDTO mapToDTO(CarModel model) {
        return new CarModelDTO(
                model.getId(),
                model.getBrand(),
                model.getModel(),
                model.getYear(),
                model.getColor()
        );
    }
}

