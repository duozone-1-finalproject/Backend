package com.example.finalproject.kafka.controller;

import com.example.finalproject.kafka.service.AIService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
public class AIController {

    private final AIService aiService;

    public AIController(AIService aiService) {
        this.aiService = aiService;
    }

    // POST /api/ai/message
    @PostMapping("/message")
    public ResponseEntity<String> sendMessage(@RequestBody String message) {
        aiService.processRequest(message);  // Kafka에 메시지 비동기 전송
        return ResponseEntity.ok("Message sent to Kafka");
    }
}
