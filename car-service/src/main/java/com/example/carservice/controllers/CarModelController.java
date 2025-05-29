package com.example.carservice.controllers;

import com.example.carservice.dtos.requests.CarModelRequest;
import com.example.carservice.dtos.responses.CarModelDTO;
import com.example.carservice.services.CarModelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/car-models")
@RequiredArgsConstructor
public class CarModelController {

    private final CarModelService carModelService;

    @GetMapping
    public List<CarModelDTO> getAllCarModels() {
        return carModelService.getAllCarModels();
    }

    @GetMapping("/{id}")
    public CarModelDTO getCarModelById(@PathVariable UUID id) {
        return carModelService.getCarModelById(id);
    }

    @PostMapping
    public ResponseEntity<CarModelDTO> createCarModel(@Valid @RequestBody CarModelRequest request) {
        CarModelDTO created = carModelService.createCarModel(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CarModelDTO> updateCarModel(@PathVariable UUID id, @Valid @RequestBody CarModelRequest request) {
        CarModelDTO updated = carModelService.updateCarModel(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteCarModel(@PathVariable UUID id) {
        carModelService.deleteCarModel(id);
        return ResponseEntity.noContent().build();
    }
}
