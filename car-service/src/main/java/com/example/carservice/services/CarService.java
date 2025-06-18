package com.example.carservice.services;

import com.example.carservice.dtos.requests.CreateCarRequest;
import com.example.carservice.dtos.requests.UpdateCarRequest;
import com.example.carservice.dtos.requests.UpdateCarStatusRequest;
import com.example.carservice.dtos.responses.CarDetailDTO;
import com.example.carservice.dtos.responses.CarShortDTO;
import com.example.carservice.entities.Car;
import com.example.carservice.entities.CarModel;
import com.example.carservice.entities.enums.CarStatus;
import com.example.carservice.mappers.CarMapper;
import com.example.carservice.repositories.CarModelRepository;
import com.example.carservice.repositories.CarRepository;
import com.example.carservice.security.JwtUtils;
import dtos.kafka.CarEvent;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CarService {

    private final KafkaTemplate<String, CarEvent> kafkaTemplate;

    private final CarRepository carRepository;
    private final CarModelRepository carModelRepository;
    private final CarMapper carMapper;

    private final JwtUtils jwtUtils;

    public List<CarShortDTO> getCarsByStatus(CarStatus status) {
        List<Car> cars;

        if (status == null) {
            cars = carRepository.findAll();
        } else {
            cars = carRepository.findByStatus(status);
        }

        return cars.stream()
                .map(carMapper::toShortDto)
                .toList();
    }

    public CarDetailDTO getCarDetails(UUID carId) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new EntityNotFoundException("машина не найдена:("));
        return carMapper.toDetailDto(car);
    }

    @Transactional
    public void createCar(CreateCarRequest request) {
        if (request.getCarModel() == null) {
            throw new IllegalArgumentException("Модель машины не указана");
        }

        if (request.getCarNumber() == null || request.getCarNumber().isBlank()) {
            throw new IllegalArgumentException("Номер машины обязателен");
        }

        if (request.getPricePerDay() == null || request.getPricePerDay().doubleValue() <= 0) {
            throw new IllegalArgumentException("Цена за день должна быть положительной");
        }

        CarModel model = carModelRepository.findById(request.getCarModel())
                .orElseThrow(() -> new NoSuchElementException("Модель машины не найдена"));

        UUID userId = jwtUtils.getCurrentUserId();
        if (userId == null) {
            throw new IllegalStateException("Не удалось получить ID текущего пользователя");
        }

        Car car = new Car();
        car.setId(UUID.randomUUID());
        car.setCarNumber(request.getCarNumber());
        car.setCarModel(model);
        car.setPricePerDay(request.getPricePerDay());
        car.setDescription(request.getDescription());
        car.setStatus(CarStatus.AVAILABLE);
        car.setCreatedBy(userId);

        carRepository.save(car);
    }

    @Transactional
    public void updateCar(UUID id, UpdateCarRequest request) {
        if (id == null) throw new IllegalArgumentException("ID машины не может быть null");

        Car car = carRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Машина не найдена"));

        if (request.getCarNumber() == null || request.getCarNumber().isBlank()) {
            throw new IllegalArgumentException("Номер машины обязателен");
        }

        if (request.getPricePerDay() == null || request.getPricePerDay().doubleValue() <= 0) {
            throw new IllegalArgumentException("Цена за день должна быть положительной");
        }

        CarModel model = carModelRepository.findById(request.getCarModel())
                .orElseThrow(() -> new NoSuchElementException("Модель машины не найдена"));

        car.setCarNumber(request.getCarNumber());
        car.setCarModel(model);
        car.setPricePerDay(request.getPricePerDay());
        car.setDescription(request.getDescription());

        carRepository.save(car);
    }

    @Transactional
    public void deleteCar(UUID id) {
        if (!carRepository.existsById(id)) {
            throw new NoSuchElementException("Машина не найдена");
        }
        carRepository.deleteById(id);
    }

    @Transactional
    public void updateCarStatus(UUID id, UpdateCarStatusRequest request) {
        if (id == null) throw new IllegalArgumentException("ID машины не может быть null");

        Car car = carRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Машина не найдена"));

        if (request.getStatus() == null) {
            throw new IllegalArgumentException("Новый статус не указан");
        }

        if (car.getStatus() == request.getStatus()) {
            throw new IllegalStateException("Статус уже установлен: " + request.getStatus());
        }

        car.setStatus(request.getStatus());
        carRepository.save(car);
    }
}

