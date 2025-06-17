package com.example.carservice.kafka.sender;

import dtos.kafka.CarReservedEvent;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class KafkaCarSender {

    private static final Logger logger = LoggerFactory.getLogger(KafkaCarSender.class);

    private final KafkaTemplate<String, CarReservedEvent> kafkaTemplate;

    public void sendCarReservedEvent(CarReservedEvent event) {
        logger.info("Подготовка к отправке события CarReservedEvent: {}", event);

        kafkaTemplate.send("car-reserved-event", event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        logger.error("Ошибка при отправке события CarReservedEvent в Kafka", ex);
                    } else if (result != null) {
                        logger.info("Событие успешно отправлено в Kafka. Topic: {}, Partition: {}, Offset: {}",
                                result.getRecordMetadata().topic(),
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset());
                    } else {
                        logger.warn("Kafka send result is null");
                    }
                });
    }
}