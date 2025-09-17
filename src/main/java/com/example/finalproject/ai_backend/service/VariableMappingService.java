// src/main/java/com/example/finalproject/ai_backend/service/VariableMappingService.java
package com.example.finalproject.ai_backend.service;

import com.example.finalproject.ai_backend.dto.VariableMappingRequestDto;
import com.example.finalproject.ai_backend.dto.VariableMappingResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class VariableMappingService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Value("${ai.timeout.seconds:600}")
    private int timeoutSeconds;

    // 요청-응답 매핑
    private final Map<String, CompletableFuture<VariableMappingResponseDto>> pendingRequests = new ConcurrentHashMap<>();

    private static final String AI_REQUEST_TOPIC = "ai-report-request";
    private static final String AI_RESPONSE_TOPIC = "ai-report-response";

    public VariableMappingService(
            KafkaTemplate<String, String> kafkaTemplate,
            ObjectMapper objectMapper
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    /**
     * AI에게 변수 매핑 요청 전송
     */
    public CompletableFuture<VariableMappingResponseDto> requestVariableMapping(VariableMappingRequestDto request) {
        try {
            String requestId = request.getRequestId();
            log.info("AI에게 변수 매핑 요청 전송: {}", requestId);

            CompletableFuture<VariableMappingResponseDto> future = new CompletableFuture<>();
            pendingRequests.put(requestId, future);

            future.orTimeout(timeoutSeconds, TimeUnit.SECONDS)
                    .whenComplete((result, throwable) -> {
                        if (throwable != null) {
                            log.error("AI 요청 타임아웃: {}", requestId, throwable);
                            pendingRequests.remove(requestId);
                        }
                    });

            String jsonRequest = objectMapper.writeValueAsString(request);
            kafkaTemplate.send(AI_REQUEST_TOPIC, requestId, jsonRequest)
                    .whenComplete((result, ex) -> {
                        if (ex != null) {
                            log.error("Kafka 메시지 전송 실패: {}", requestId, ex);
                            future.completeExceptionally(ex);
                            pendingRequests.remove(requestId);
                        } else {
                            log.info("Kafka 메시지 전송 성공: {}", requestId);
                        }
                    });

            return future;

        } catch (Exception e) {
            log.error("AI 요청 전송 실패", e);
            CompletableFuture<VariableMappingResponseDto> failedFuture = new CompletableFuture<>();
            failedFuture.completeExceptionally(e);
            return failedFuture;
        }
    }

    /**
     * AI 응답 수신
     */
    @KafkaListener(topics = AI_RESPONSE_TOPIC, groupId = "${spring.kafka.consumer.group-id}")
    public void handleAiResponse(String message) {
        log.info("AI 서버 응답 수신: {}", message.substring(0, Math.min(message.length(), 100)) + "...");
        handleAiResponseInternal(message);
    }

    private void handleAiResponseInternal(String message) {
        try {
            VariableMappingResponseDto response = objectMapper.readValue(message, VariableMappingResponseDto.class);
            String requestId = response.getRequestId();

            CompletableFuture<VariableMappingResponseDto> pendingRequest = pendingRequests.remove(requestId);

            if (pendingRequest != null && !pendingRequest.isDone()) {
                pendingRequest.complete(response);
                log.info("AI 응답 처리 완료: {}", requestId);
            } else {
                log.warn("매칭되는 요청을 찾을 수 없음: {}", requestId);
            }

        } catch (Exception e) {
            log.error("AI 응답 처리 실패: {}", e.getMessage(), e);
        }
    }

    public CompletableFuture<Boolean> checkKafkaConnection() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String testMessage = "health-check-" + System.currentTimeMillis();
                kafkaTemplate.send("health-check", "test", testMessage).get(5, TimeUnit.SECONDS);
                log.info("Kafka 연결 정상");
                return true;
            } catch (Exception e) {
                log.error("Kafka 연결 실패: {}", e.getMessage());
                return false;
            }
        });
    }

    public int getPendingRequestCount() {
        return pendingRequests.size();
    }

    public boolean cancelRequest(String requestId) {
        CompletableFuture<VariableMappingResponseDto> pendingRequest = pendingRequests.remove(requestId);
        if (pendingRequest != null && !pendingRequest.isDone()) {
            pendingRequest.cancel(true);
            log.info("요청 취소됨: {}", requestId);
            return true;
        }
        return false;
    }
}