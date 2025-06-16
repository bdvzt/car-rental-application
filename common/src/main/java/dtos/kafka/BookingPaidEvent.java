package dtos.kafka;

import java.math.BigDecimal;
import java.util.UUID;

public class BookingPaidEvent {
    private UUID bookingId;
    private UUID paymentId;
    private BigDecimal amount;
}
