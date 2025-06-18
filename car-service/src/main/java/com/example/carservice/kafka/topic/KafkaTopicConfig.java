package com.example.carservice.kafka.topic;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic carReservedTopic() {
        return TopicBuilder.name("car-reserved-event").build();
    }

    @Bean
    public NewTopic carFreedTopic() {
        return TopicBuilder.name("car-freed-event").build();
    }
}
