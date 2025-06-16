package com.example.bookingservice.kafka.sender;

import dtos.kafka.BookingCreatedEvent;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class KafkaSender {

    private final KafkaTemplate<String, BookingCreatedEvent> kafkaTemplate;

    public void sendBookingCreatedEvent(BookingCreatedEvent bookingCreatedEvent) {
        kafkaTemplate.send("booking-event", bookingCreatedEvent);
    }
}
