package dtos.kafka;

import java.util.UUID;

public class BookingPaymentFailedEvent {
    private UUID bookingId;
    private String reason;
}
