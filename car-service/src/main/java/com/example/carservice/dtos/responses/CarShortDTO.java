package com.example.carservice.dtos.responses;

import com.example.carservice.entities.enums.CarStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarShortDTO {
    private UUID id;
    private String carNumber;
    private CarModelDTO carModel;
    private BigDecimal pricePerDay;
    private CarStatus status;
}
