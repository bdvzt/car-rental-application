package com.example.carservice.mappers;

import com.example.carservice.dtos.requests.CarModelRequest;
import dtos.responses.CarModelDTO;
import com.example.carservice.entities.CarModel;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CarModelMapper {

    public CarModelDTO toDto(CarModel entity) {
        return new CarModelDTO(
                entity.getId(),
                entity.getBrand(),
                entity.getModel(),
                entity.getYear(),
                entity.getColor()
        );
    }

    public void updateEntity(CarModel entity, CarModelRequest request) {
        entity.setBrand(request.getBrand());
        entity.setModel(request.getModel());
        entity.setYear(request.getYear());
        entity.setColor(request.getColor());
        entity.setUpdatedAt(LocalDateTime.now());
    }
}

