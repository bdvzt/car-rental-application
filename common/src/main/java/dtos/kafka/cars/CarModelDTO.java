package dtos.kafka.cars;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarModelDTO {
    private UUID id;
    private String brand;
    private String model;
    private int year;
    private String color;
}