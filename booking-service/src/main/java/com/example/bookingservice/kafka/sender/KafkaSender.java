package com.example.bookingservice.kafka.sender;

import dtos.kafka.BookingEvent;
import dtos.kafka.PaymentEvent;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class KafkaSender {

    private final KafkaTemplate<String, BookingEvent> kafkaTemplateBooking;
    private final KafkaTemplate<String, PaymentEvent> kafkaTemplatePayment;

    public void sendBookingCreatedEvent(BookingEvent event) {
        kafkaTemplateBooking.send("booking-event", event);
    }

    public void sendBookingCompletedEvent(BookingEvent event) {
        kafkaTemplateBooking.send("booking-completed-event", event);
    }

    public void sendPayingEvent(PaymentEvent event) {
        kafkaTemplatePayment.send("paying-event", event);
    }
}
