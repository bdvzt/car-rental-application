package com.example.carservice.config;

import dtos.kafka.BookingEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public ConsumerFactory<String, BookingEvent> bookingConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                bootstrapServers);
        props.put(
                ConsumerConfig.GROUP_ID_CONFIG,
                "car-group");
        props.put(
                JsonDeserializer.TRUSTED_PACKAGES,
                "com.example.common.dtos.kafka");
        props.put(
                JsonDeserializer.VALUE_DEFAULT_TYPE,
                BookingEvent.class.getName());
        props.put(
                JsonDeserializer.USE_TYPE_INFO_HEADERS,
                false);

        return new DefaultKafkaConsumerFactory<>(
                props,
                new StringDeserializer(),
                new JsonDeserializer<>(BookingEvent.class)
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, BookingEvent> bookingListenerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, BookingEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(bookingConsumerFactory());
        return factory;
    }
}
