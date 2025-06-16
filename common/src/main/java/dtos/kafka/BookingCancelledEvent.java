package dtos.kafka;

import java.time.LocalDateTime;
import java.util.UUID;

public class BookingCancelledEvent {
    private UUID bookingId;
    private String reason;
}
