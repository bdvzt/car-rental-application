package dtos.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentEvent {
    private UUID bookingId;
    private UUID paymentId;
    private UUID userId;
    private BigDecimal pricePerDay;
}
