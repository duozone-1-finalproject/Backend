package com.example.finalproject.kafka.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class AIKafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final String topic = "ai-request-topic";

    public AIKafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String message) {
        kafkaTemplate.send(topic, message);
    }
}
