// 파일 경로: .../com/example/finalproject/kafka/service/ReportService.java
package com.example.finalproject.kafka.service;

import com.example.finalproject.kafka.dto.KafkaMessage;
import com.example.finalproject.kafka.dto.ReportCreationRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class ReportService {
    private final KafkaTemplate<String, KafkaMessage<ReportCreationRequest>> kafkaTemplate;
    private final ReportRequestManager requestManager;
    private final String requestTopic = "report-request-topic";

    public ReportService(KafkaTemplate<String, KafkaMessage<ReportCreationRequest>> kafkaTemplate, ReportRequestManager requestManager) {
        this.kafkaTemplate = kafkaTemplate;
        this.requestManager = requestManager;
    }

    public String submitReportRequest(ReportCreationRequest requestData) {
        String requestId = UUID.randomUUID().toString();
        requestManager.createRequest(requestId);
        KafkaMessage<ReportCreationRequest> message = new KafkaMessage<>();
        message.setRequestId(requestId);
        message.setData(requestData);
        kafkaTemplate.send(requestTopic, message);
        return requestId;
    }
}
