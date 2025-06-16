package com.example.carservice.controllers;

import com.example.carservice.dtos.requests.CarModelRequest;
import com.example.carservice.dtos.responses.CarModelDTO;
import com.example.carservice.services.CarModelService;
import dtos.responses.ResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "создание новой модели машины")
    @PostMapping
    public ResponseEntity<ResponseDTO> createCarModel(@Valid @RequestBody CarModelRequest request) {
        carModelService.createCarModel(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDTO(HttpStatus.CREATED.value(), "Модель машины успешно создана"));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "обновление данных модели машины")
    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO> updateCarModel(@PathVariable UUID id, @Valid @RequestBody CarModelRequest request) {
        carModelService.updateCarModel(id, request);
        return ResponseEntity.ok(new ResponseDTO(HttpStatus.OK.value(), "Модель машины успешно обновлена"));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "удаление модели машины")
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO> deleteCarModel(@PathVariable UUID id) {
        carModelService.deleteCarModel(id);
        return ResponseEntity.ok(new ResponseDTO(HttpStatus.OK.value(), "Модель машины успешно удалена"));
    }
}
