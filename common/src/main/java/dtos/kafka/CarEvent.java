package dtos.kafka;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarEvent {
    private UUID bookingId;
    private UUID carId;
    private BigDecimal pricePerDay;
    private boolean success;
    private String message;
    private String email;
}
