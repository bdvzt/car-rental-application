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
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CarService {

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
    public Car createCar(CreateCarRequest request) {
        CarModel model = carModelRepository.findById(request.getCarModel())
                .orElseThrow(() -> new EntityNotFoundException("модель машины не найдена"));

        String token = jwtUtils.getCurrentToken();
        UUID userId = jwtUtils.getUserIdFromJwtToken(token);

        Car car = new Car();
        car.setId(UUID.randomUUID());
        car.setCarNumber(request.getCarNumber());
        car.setCarModel(model);
        car.setPricePerDay(request.getPricePerDay());
        car.setDescription(request.getDescription());
        car.setStatus(CarStatus.AVAILABLE);
        car.setCreatedBy(userId);

        return carRepository.save(car);
    }

    @Transactional
    public Car updateCar(UUID id, UpdateCarRequest request) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("машина не найдена!"));

        CarModel model = carModelRepository.findById(request.getCarModel())
                .orElseThrow(() -> new EntityNotFoundException("модель машины не найдена!"));

        car.setCarNumber(request.getCarNumber());
        car.setCarModel(model);
        car.setPricePerDay(request.getPricePerDay());
        car.setDescription(request.getDescription());

        return carRepository.save(car);
    }

    @Transactional
    public void deleteCar(UUID id) {
        if (!carRepository.existsById(id)) {
            throw new EntityNotFoundException("машина не найдена");
        }
        carRepository.deleteById(id);
    }

    @Transactional
    public void updateCarStatus(UUID id, UpdateCarStatusRequest request) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("машина не найдена"));

        car.setStatus(request.getStatus());
        carRepository.save(car);
    }
}

