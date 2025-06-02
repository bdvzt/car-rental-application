package com.example.carservice.controllers;

import com.example.carservice.dtos.requests.CarModelRequest;
import com.example.carservice.dtos.responses.CarModelDTO;
import com.example.carservice.services.CarModelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/car-model")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "управление моделями машин", description = "крудилка для моделей машин")
public class CarModelController {

    private final CarModelService carModelService;

    @Operation(summary = "получение списка моделей машин")
    @GetMapping
    public List<CarModelDTO> getAllCarModels() {
        return carModelService.getAllCarModels();
    }

    @Operation(summary = "получение конкретной модели машины")
    @GetMapping("/{id}")
    public CarModelDTO getCarModelById(@PathVariable UUID id) {
        return carModelService.getCarModelById(id);
    }

    @Operation(summary = "создание новой модели машины")
    @PostMapping
    public ResponseEntity<CarModelDTO> createCarModel(@Valid @RequestBody CarModelRequest request) {
        CarModelDTO created = carModelService.createCarModel(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "обновление данных модели машины")
    @PutMapping("/{id}")
    public ResponseEntity<CarModelDTO> updateCarModel(@PathVariable UUID id, @Valid @RequestBody CarModelRequest request) {
        CarModelDTO updated = carModelService.updateCarModel(id, request);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "удаление модели машины")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteCarModel(@PathVariable UUID id) {
        carModelService.deleteCarModel(id);
        return ResponseEntity.noContent().build();
    }
}
