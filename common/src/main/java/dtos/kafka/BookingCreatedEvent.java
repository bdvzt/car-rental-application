package dtos.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingCreatedEvent {
    private UUID bookingId;
    private UUID carId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
