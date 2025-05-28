package com.example.carservice.services;

import com.example.carservice.dtos.requests.CarModelRequest;
import com.example.carservice.dtos.requests.CarModelRequest;
import com.example.carservice.dtos.responses.CarModelDTO;
import com.example.carservice.entities.CarModel;
import com.example.carservice.mappers.CarModelMapper;
import com.example.carservice.repositories.CarModelRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CarModelService {

    private final CarModelRepository carModelRepository;
    private final CarModelMapper carModelMapper;

    public List<CarModelDTO> getAllCarModels() {
        return carModelRepository.findAll().stream()
                .map(carModelMapper::toDto)
                .toList();
    }

    public CarModelDTO getCarModelById(UUID id) {
        CarModel model = carModelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("модель машины не найдена!(((("));
        return carModelMapper.toDto(model);
    }

    @Transactional
    public CarModelDTO createCarModel(CarModelRequest request) {
        // TODO: заменить на почту пользователя
        CarModel model = carModelMapper.toEntity(request, "админ");
        return carModelMapper.toDto(carModelRepository.save(model));
    }

    @Transactional
    public CarModelDTO updateCarModel(UUID id, CarModelRequest request) {
        CarModel model = carModelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("модель машины не найдена!(((("));
        carModelMapper.updateEntity(model, request);
        return carModelMapper.toDto(carModelRepository.save(model));
    }

    @Transactional
    public void deleteCarModel(UUID id) {
        if (!carModelRepository.existsById(id)) {
            throw new EntityNotFoundException("модель машины не найдена!((((");
        }
        carModelRepository.deleteById(id);
    }
}
