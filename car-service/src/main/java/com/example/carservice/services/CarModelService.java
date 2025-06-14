package com.example.carservice.services;

import com.example.carservice.dtos.requests.CarModelRequest;
import com.example.carservice.dtos.requests.CarModelRequest;
import com.example.carservice.dtos.responses.CarModelDTO;
import com.example.carservice.entities.CarModel;
import com.example.carservice.mappers.CarModelMapper;
import com.example.carservice.repositories.CarModelRepository;
import com.example.carservice.security.JwtUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CarModelService {

    private final CarModelRepository carModelRepository;
    private final CarModelMapper carModelMapper;

    private final JwtUtils jwtUtils;

    public List<CarModelDTO> getAllCarModels() {
        return carModelRepository.findAll().stream()
                .map(carModelMapper::toDto)
                .toList();
    }

    public CarModelDTO getCarModelById(UUID id) {
        CarModel model = carModelRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Модель машины не найдена"));
        return carModelMapper.toDto(model);
    }

    @Transactional
    public void createCarModel(CarModelRequest request) {
        validateCarModelRequest(request);

        UUID userId = jwtUtils.getCurrentUserId();
        if (userId == null) {
            throw new IllegalStateException("Не удалось получить ID текущего пользователя");
        }

        CarModel model = new CarModel();
        model.setBrand(request.getBrand());
        model.setModel(request.getModel());
        model.setYear(request.getYear());
        model.setColor(request.getColor());
        model.setCreatedBy(userId);

        carModelRepository.save(model);
    }

    @Transactional
    public void updateCarModel(UUID id, CarModelRequest request) {
        if (id == null) throw new IllegalArgumentException("ID модели не может быть null");

        validateCarModelRequest(request);

        CarModel model = carModelRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Модель машины не найдена"));

        carModelMapper.updateEntity(model, request);
        carModelRepository.save(model);
    }

    @Transactional
    public void deleteCarModel(UUID id) {
        if (!carModelRepository.existsById(id)) {
            throw new NoSuchElementException("Модель машины не найдена");
        }
        carModelRepository.deleteById(id);
    }

    private void validateCarModelRequest(CarModelRequest request) {
        if (request.getBrand() == null || request.getBrand().isBlank()) {
            throw new IllegalArgumentException("Бренд обязателен");
        }
        if (request.getModel() == null || request.getModel().isBlank()) {
            throw new IllegalArgumentException("Модель обязательна");
        }
        if (request.getYear() == null || request.getYear() < 1900 || request.getYear() > LocalDate.now().getYear()) {
            throw new IllegalArgumentException("Год указан некорректно");
        }
        if (request.getColor() == null || request.getColor().isBlank()) {
            throw new IllegalArgumentException("Цвет обязателен");
        }
    }
}
