package com.example.carservice.mappers;

import com.example.carservice.dtos.responses.CarDetailDTO;
import com.example.carservice.dtos.responses.CarModelDTO;
import com.example.carservice.dtos.responses.CarShortDTO;
import com.example.carservice.entities.Car;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CarMapper {

    private final CarModelMapper carModelMapper;

    public CarShortDTO toShortDto(Car car) {
        return new CarShortDTO(
                car.getId(),
                car.getCarNumber(),
                carModelMapper.toDto(car.getCarModel()),
                car.getPricePerDay(),
                car.getStatus()
        );
    }

    public CarDetailDTO toDetailDto(Car car) {
        return new CarDetailDTO(
                car.getId(),
                car.getCarNumber(),
                carModelMapper.toDto(car.getCarModel()),
                car.getPricePerDay(),
                car.getDescription(),
                car.getStatus(),
                car.getCreatedBy(),
                car.getCreatedAt(),
                car.getUpdatedAt()
        );
    }
}
