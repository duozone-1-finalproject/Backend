package com.example.finalproject.kafka.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class AlkafkaConsumer {

    @KafkaListener(topics = "ai-request-topic", groupId = "ai-group")
    public void consume(String message) {
        System.out.println("Kafka Consumer received message: " + message);
        // 실제 로직 필요 시 서비스 호출 가능
    }
}
