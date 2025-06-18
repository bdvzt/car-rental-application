package com.example.carservice.kafka.sender;

import dtos.kafka.CarEvent;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class KafkaCarSender {

    private final KafkaTemplate<String, CarEvent> kafkaTemplate;

    public void sendCarReservedEvent(CarEvent event) {
        kafkaTemplate.send("car-reserved-event", event);
    }

    public void sendCarFreedEvent(CarEvent event) {
        kafkaTemplate.send("car-freed-event", event);
    }
}
