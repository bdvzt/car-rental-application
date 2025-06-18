package com.example.carservice.controllers;

import com.example.carservice.dtos.requests.CreateCarRequest;
import com.example.carservice.dtos.requests.UpdateCarRequest;
import com.example.carservice.dtos.requests.UpdateCarStatusRequest;
import dtos.responses.CarDetailDTO;
import com.example.carservice.dtos.responses.CarShortDTO;
import dtos.responses.CarStatus;
import com.example.carservice.services.CarService;
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
@RequestMapping("/car")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "управление машинами", description = "крудилка для машин")
public class CarController {

    private final CarService carService;

    @Operation(summary = "получение списка машин", description = "есть фильтрация по статусу")
    @GetMapping
    public ResponseEntity<List<CarShortDTO>> getСarsByStatus(
            @RequestParam(name = "status", required = false) CarStatus status) {
        return ResponseEntity.ok(carService.getCarsByStatus(status));
    }

    @Operation(summary = "получение инфы о конкретной машине")
    @GetMapping("/{id}")
    public ResponseEntity<CarDetailDTO> getCarById(@PathVariable UUID id) {
        CarDetailDTO car = carService.getCarDetails(id);
        return ResponseEntity.ok(car);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "добавление новой машины")
    @PostMapping
    public ResponseEntity<ResponseDTO> createCar(@Valid @RequestBody CreateCarRequest request) {
        carService.createCar(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDTO(HttpStatus.CREATED.value(), "Машина успешно добавлена"));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "обновить данные у машины")
    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO> updateCar(@PathVariable UUID id, @Valid @RequestBody UpdateCarRequest request) {
        carService.updateCar(id, request);
        return ResponseEntity.ok(new ResponseDTO(HttpStatus.OK.value(), "Данные машины успешно обновлены"));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "удалить машину")
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO> deleteCar(@PathVariable UUID id) {
        carService.deleteCar(id);
        return ResponseEntity.ok(new ResponseDTO(HttpStatus.OK.value(), "Машина успешно удалена"));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "изменить статус машины")
    @PatchMapping("/{id}/status")
    public ResponseEntity<ResponseDTO> updateCarStatus(@PathVariable UUID id, @Valid @RequestBody UpdateCarStatusRequest request) {
        carService.updateCarStatus(id, request);
        return ResponseEntity.ok(new ResponseDTO(HttpStatus.OK.value(), "Статус машины успешно изменен"));
    }
}
