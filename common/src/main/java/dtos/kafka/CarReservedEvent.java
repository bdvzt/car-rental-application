package dtos.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarReservedEvent {
    private UUID bookingId;
    private UUID carId;
    private BigDecimal pricePerDay;
    private boolean success;
    private String message;
}
