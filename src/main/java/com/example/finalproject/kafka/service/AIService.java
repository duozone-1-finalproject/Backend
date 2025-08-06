package com.example.finalproject.kafka.service;

import com.example.finalproject.kafka.producer.AIKafkaProducer;
import org.springframework.stereotype.Service;

@Service
public class AIService {

    private final AIKafkaProducer producer;

    public AIService(AIKafkaProducer producer) {
        this.producer = producer;
    }

    public void processRequest(String message) {
        // 필요하면 비즈니스 로직 추가
        producer.sendMessage(message);
    }
}
