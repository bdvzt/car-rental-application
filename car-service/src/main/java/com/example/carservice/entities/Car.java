package com.example.carservice.entities;

import com.example.carservice.entities.enums.CarStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "cars")
@NoArgsConstructor
public class Car {

    @Id
    private UUID id;

    // я ориентируюсь только на русские номера
    @Pattern(
            regexp = "^[АВЕКМНОРСТУХ]{1}\\d{3}[АВЕКМНОРСТУХ]{2}\\d{2,3}$",
            message = "Номер машины должен быть в формате А001АА01"
    )
    @Column(name = "car_number", unique = true, nullable = false)
    private String carNumber;

    @ManyToOne(optional = false)
    @JoinColumn(name = "car_model_id")
    private CarModel carModel;

    @Column(name = "price_per_day", nullable = false)
    private BigDecimal pricePerDay;

    @Column(name = "description", length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CarStatus status;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_by", nullable = false, updatable = false)
    private UUID createdBy;
}