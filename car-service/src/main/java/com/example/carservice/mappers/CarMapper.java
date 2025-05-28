package com.example.carservice.mappers;

import com.example.carservice.dtos.responses.CarDetailDTO;
import com.example.carservice.dtos.responses.CarModelDTO;
import com.example.carservice.dtos.responses.CarShortDTO;
import com.example.carservice.entities.Car;
import org.springframework.stereotype.Component;

@Component
public class CarMapper {

    public CarShortDTO toShortDto(Car car) {
        return new CarShortDTO(
                car.getId(),
                car.getCarNumber(),
                toModelDto(car),
                car.getPricePerDay(),
                car.getStatus()
        );
    }

    public CarDetailDTO toDetailDto(Car car) {
        return new CarDetailDTO(
                car.getId(),
                car.getCarNumber(),
                toModelDto(car),
                car.getPricePerDay(),
                car.getDescription(),
                car.getStatus(),
                car.getCreatedBy(),
                car.getCreatedAt(),
                car.getUpdatedAt()
        );
    }

    private CarModelDTO toModelDto(Car car) {
        return new CarModelDTO(
                car.getCarModel().getId(),
                car.getCarModel().getBrand(),
                car.getCarModel().getModel(),
                car.getCarModel().getYear(),
                car.getCarModel().getColor()
        );
    }
}
