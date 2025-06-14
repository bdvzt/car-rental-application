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

import java.time.LocalDateTime;
import java.util.List;
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
                .orElseThrow(() -> new EntityNotFoundException("модель машины не найдена!(((("));
        return carModelMapper.toDto(model);
    }

    @Transactional
    public void createCarModel(CarModelRequest request) {
        String token = jwtUtils.getCurrentToken();
        UUID userId = jwtUtils.getUserIdFromJwtToken(token);

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
        CarModel model = carModelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("модель машины не найдена!(((("));
        carModelMapper.updateEntity(model, request);
        carModelRepository.save(model);
    }

    @Transactional
    public void deleteCarModel(UUID id) {
        if (!carModelRepository.existsById(id)) {
            throw new EntityNotFoundException("модель машины не найдена!((((");
        }
        carModelRepository.deleteById(id);
    }
}
