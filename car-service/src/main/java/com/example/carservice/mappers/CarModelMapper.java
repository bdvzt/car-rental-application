package com.example.carservice.mappers;

import com.example.carservice.dtos.requests.CarModelRequest;
import com.example.carservice.dtos.responses.CarModelDTO;
import com.example.carservice.entities.CarModel;
import lombok.Data;
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

    public CarModel toEntity(CarModelRequest request, String createdBy) {
        CarModel model = new CarModel();
        model.setBrand(request.getBrand());
        model.setModel(request.getModel());
        model.setYear(request.getYear());
        model.setColor(request.getColor());
        model.setCreatedAt(LocalDateTime.now());
        model.setCreatedBy(createdBy);
        return model;
    }

    public void updateEntity(CarModel entity, CarModelRequest request) {
        entity.setBrand(request.getBrand());
        entity.setModel(request.getModel());
        entity.setYear(request.getYear());
        entity.setColor(request.getColor());
        entity.setUpdatedAt(LocalDateTime.now());
    }
}

