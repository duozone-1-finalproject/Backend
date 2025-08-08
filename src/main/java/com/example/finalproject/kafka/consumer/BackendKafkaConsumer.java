
// 파일 경로: .../com/example/finalproject/kafka/consumer/BackendKafkaConsumer.java
package com.example.finalproject.kafka.consumer;

import com.example.finalproject.kafka.dto.KafkaMessage;
import com.example.finalproject.kafka.dto.ReportResult;
import com.example.finalproject.kafka.service.ReportRequestManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import java.util.LinkedHashMap;

@Component
public class BackendKafkaConsumer {
    private final ReportRequestManager requestManager;
    private final ObjectMapper objectMapper;

    public BackendKafkaConsumer(ReportRequestManager requestManager, ObjectMapper objectMapper) {
        this.requestManager = requestManager;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "report-result-topic", containerFactory = "resultListenerContainerFactory")
    public void consumeResult(KafkaMessage<LinkedHashMap<String, Object>> message) {
        String requestId = message.getRequestId();
        ReportResult result = objectMapper.convertValue(message.getData(), ReportResult.class);
        System.out.println("Received result for request ID: " + requestId);
        requestManager.completeRequest(requestId, result);
    }
}
