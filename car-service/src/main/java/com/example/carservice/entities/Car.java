package com.example.carservice.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Entity
@Table(name = "cars")
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class Car {

    @Id
    private UUID id;

    // я ориентируюсь только на русские номера)
    @Pattern(
            regexp = "^[АВЕКМНОРСТУХ]{1}\\d{3}[АВЕКМНОРСТУХ]{2}\\d{2,3}$",
            message = "Номер машины должен быть в формате А001АА01"
    )
    @Column(name = "car_number", unique = true, nullable = false)
    private String carNumber;

    @Column(name = "car_number", unique = true, nullable = false)
    private String carNumber;

    @Column(name = "engine_number", unique = true, nullable = false)
    private String engineNumber;

    @Column(name = "brand", nullable = false)
    private String brand;

    @Column(name = "model", nullable = false)
    private String model;

    @Column(name = "year")
    private Integer year;

    @Column(name = "color")
    private String color;

    @Column(name = "price_per_day", nullable = false)
    private BigDecimal pricePerDay;

    @Column(name = "description", length = 1000)
    private String description;
}
