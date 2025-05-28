package com.example.carservice.services;

import com.example.carservice.dtos.requests.CreateCarRequest;
import com.example.carservice.dtos.requests.UpdateCarRequest;
import com.example.carservice.dtos.responses.CarDetailDTO;
import com.example.carservice.dtos.responses.CarModelDTO;
import com.example.carservice.dtos.responses.CarShortDTO;
import com.example.carservice.entities.Car;
import com.example.carservice.entities.CarModel;
import com.example.carservice.entities.enums.CarStatus;
import com.example.carservice.repositories.CarModelRepository;
import com.example.carservice.repositories.CarRepository;
import jakarta.persistence.EntityNotFoundException;
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

    public List<CarShortDTO> getAllCars() {
        return carRepository.findAll().stream()
                .map(this::mapToShortDTO)
                .toList();
    }

    public List<CarShortDTO> getAvailableCars() {
        return carRepository.findAll().stream()
                .filter(car -> car.getStatus() == CarStatus.AVAILABLE)
                .map(this::mapToShortDTO)
                .toList();
    }

    public CarDetailDTO getCarDetails(UUID carId) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new EntityNotFoundException("Car not found"));
        return mapToDetailDTO(car);
    }

    public CarDetailDTO createCar(CreateCarRequest request) {
        CarModel model = carModelRepository.findById(request.getCarModel())
                .orElseThrow(() -> new EntityNotFoundException("Car model not found"));

        Car car = Car.of(
                UUID.randomUUID(),
                request.getCarNumber(),
                model,
                request.getPricePerDay(),
                request.getDescription(),
                CarStatus.AVAILABLE
        );
        car.setCreatedAt(LocalDateTime.now());
        car.setCreatedBy("system");
        return mapToDetailDTO(carRepository.save(car));
    }

    public CarDetailDTO updateCar(UpdateCarRequest request) {
        Car car = carRepository.findById(request.getId())
                .orElseThrow(() -> new EntityNotFoundException("Car not found"));

        CarModel model = carModelRepository.findById(request.getCarModel())
                .orElseThrow(() -> new EntityNotFoundException("Car model not found"));

        car.setCarNumber(request.getCarNumber());
        car.setCarModel(model);
        car.setPricePerDay(request.getPricePerDay());
        car.setDescription(request.getDescription());
        car.setUpdatedAt(LocalDateTime.now());

        return mapToDetailDTO(carRepository.save(car));
    }

    public void deleteCar(UUID id) {
        carRepository.deleteById(id);
    }

    public CarDetailDTO updateCarStatus(UUID id, CarStatus status) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Car not found"));

        car.setStatus(status);
        car.setUpdatedAt(LocalDateTime.now());

        return mapToDetailDTO(carRepository.save(car));
    }

    private CarShortDTO mapToShortDTO(Car car) {
        return new CarShortDTO(
                car.getId(),
                car.getCarNumber(),
                new CarModelDTO(
                        car.getCarModel().getId(),
                        car.getCarModel().getBrand(),
                        car.getCarModel().getModel(),
                        car.getCarModel().getYear(),
                        car.getCarModel().getColor()
                ),
                car.getPricePerDay()
        );
    }

    private CarDetailDTO mapToDetailDTO(Car car) {
        return new CarDetailDTO(
                car.getId(),
                car.getCarNumber(),
                new CarModelDTO(
                        car.getCarModel().getId(),
                        car.getCarModel().getBrand(),
                        car.getCarModel().getModel(),
                        car.getCarModel().getYear(),
                        car.getCarModel().getColor()
                ),
                car.getPricePerDay(),
                car.getDescription(),
                car.getStatus()
        );
    }
}
