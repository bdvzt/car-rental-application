package com.example.bookingservice.kafka.sender;

import dtos.kafka.BookingEvent;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class KafkaSender {

    private final KafkaTemplate<String, BookingEvent> kafkaTemplate;

    public void sendBookingCreatedEvent(BookingEvent event) {
        sendEvent("booking-event", event);
    }

    public void sendBookingCompletedEvent(BookingEvent event) {
        sendEvent("booking-completed-event", event);
    }

    private void sendEvent(String topic, BookingEvent event) {
        kafkaTemplate.send(topic, event);
    }
}
