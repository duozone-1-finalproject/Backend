package com.example.finalproject.ai_backend.service;

import com.example.finalproject.ai_backend.dto.*;
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

import static com.example.finalproject.ai_backend.common.Constants.*;

@Slf4j
@Service
public class ValidationService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Value("${ai.timeout.seconds:600}")
    private int timeoutSeconds;

    // 요청-응답 매핑
    private final Map<String, CompletableFuture<ValidationResponseDto>> pendingValidationRequests = new ConcurrentHashMap<>();
    private final Map<String, CompletableFuture<RevisionResponseDto>> pendingRevisionRequests = new ConcurrentHashMap<>();

    public ValidationService(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    /**
     * AI에게 검증 요청 전송
     */
    public CompletableFuture<ValidationResponseDto> requestValidation(ValidationRequestDto request) {
        try {
            String requestId = request.getRequestId();
            log.info("AI에게 검증 요청 전송: {}", requestId);

            CompletableFuture<ValidationResponseDto> future = new CompletableFuture<>();
            pendingValidationRequests.put(requestId, future);

            future.orTimeout(timeoutSeconds, TimeUnit.SECONDS)
                    .whenComplete((result, throwable) -> {
                        if (throwable != null) {
                            log.error("AI 검증 요청 타임아웃: {}", requestId, throwable);
                            pendingValidationRequests.remove(requestId);
                        }
                    });

            String jsonRequest = objectMapper.writeValueAsString(request);
            kafkaTemplate.send(VALIDATION_REQUEST_TOPIC, requestId, jsonRequest)
                    .whenComplete((result, ex) -> {
                        if (ex != null) {
                            log.error("Kafka 검증 메시지 전송 실패: {}", requestId, ex);
                            future.completeExceptionally(ex);
                            pendingValidationRequests.remove(requestId);
                        } else {
                            log.info("Kafka 검증 메시지 전송 성공: {}", requestId);
                        }
                    });

            return future;

        } catch (Exception e) {
            log.error("AI 검증 요청 전송 실패", e);
            CompletableFuture<ValidationResponseDto> failedFuture = new CompletableFuture<>();
            failedFuture.completeExceptionally(e);
            return failedFuture;
        }
    }

    /**
     * AI에게 재생성 요청 전송
     */
    public CompletableFuture<RevisionResponseDto> requestRevision(RevisionRequestDto request) {
        try {
            String requestId = request.getRequestId();
            log.info("AI에게 재생성 요청 전송: {}", requestId);

            CompletableFuture<RevisionResponseDto> future = new CompletableFuture<>();
            pendingRevisionRequests.put(requestId, future);

            future.orTimeout(timeoutSeconds, TimeUnit.SECONDS)
                    .whenComplete((result, throwable) -> {
                        if (throwable != null) {
                            log.error("AI 재생성 요청 타임아웃: {}", requestId, throwable);
                            pendingRevisionRequests.remove(requestId);
                        }
                    });

            String jsonRequest = objectMapper.writeValueAsString(request);
            kafkaTemplate.send(REVISION_REQUEST_TOPIC, requestId, jsonRequest)
                    .whenComplete((result, ex) -> {
                        if (ex != null) {
                            log.error("Kafka 재생성 메시지 전송 실패: {}", requestId, ex);
                            future.completeExceptionally(ex);
                            pendingRevisionRequests.remove(requestId);
                        } else {
                            log.info("Kafka 재생성 메시지 전송 성공: {}", requestId);
                        }
                    });

            return future;

        } catch (Exception e) {
            log.error("AI 재생성 요청 전송 실패", e);
            CompletableFuture<RevisionResponseDto> failedFuture = new CompletableFuture<>();
            failedFuture.completeExceptionally(e);
            return failedFuture;
        }
    }

    /**
     * AI 검증 응답 수신
     */
    @KafkaListener(topics = VALIDATION_RESPONSE_TOPIC, groupId = "${spring.kafka.consumer.group-id}")
    public void handleValidationResponse(String message) {
        log.info("AI 검증 응답 수신: {}", message.substring(0, Math.min(message.length(), 100)) + "...");
        handleValidationResponseInternal(message);
    }

    private void handleValidationResponseInternal(String message) {
        try {
            ValidationResponseDto response = objectMapper.readValue(message, ValidationResponseDto.class);
            String requestId = response.getRequestId();

            CompletableFuture<ValidationResponseDto> pendingRequest = pendingValidationRequests.remove(requestId);

            if (pendingRequest != null && !pendingRequest.isDone()) {
                pendingRequest.complete(response);
                log.info("AI 검증 응답 처리 완료: {}", requestId);
            } else {
                log.warn("매칭되는 검증 요청을 찾을 수 없음: {}", requestId);
            }

        } catch (Exception e) {
            log.error("AI 검증 응답 처리 실패: {}", e.getMessage(), e);
        }
    }

    /**
     * AI 재생성 응답 수신
     */
    @KafkaListener(topics = REVISION_RESPONSE_TOPIC, groupId = "${spring.kafka.consumer.group-id}")
    public void handleRevisionResponse(String message) {
        log.info("AI 재생성 응답 수신: {}", message.substring(0, Math.min(message.length(), 100)) + "...");
        handleRevisionResponseInternal(message);
    }

    private void handleRevisionResponseInternal(String message) {
        try {
            RevisionResponseDto response = objectMapper.readValue(message, RevisionResponseDto.class);
            String requestId = response.getRequestId();

            CompletableFuture<RevisionResponseDto> pendingRequest = pendingRevisionRequests.remove(requestId);

            if (pendingRequest != null && !pendingRequest.isDone()) {
                pendingRequest.complete(response);
                log.info("AI 재생성 응답 처리 완료: {}", requestId);
            } else {
                log.warn("매칭되는 재생성 요청을 찾을 수 없음: {}", requestId);
            }

        } catch (Exception e) {
            log.error("AI 재생성 응답 처리 실패: {}", e.getMessage(), e);
        }
    }

    public int getPendingValidationRequestCount() {
        return pendingValidationRequests.size();
    }

    public int getPendingRevisionRequestCount() {
        return pendingRevisionRequests.size();
    }

    public boolean cancelValidationRequest(String requestId) {
        CompletableFuture<ValidationResponseDto> pendingRequest = pendingValidationRequests.remove(requestId);
        if (pendingRequest != null && !pendingRequest.isDone()) {
            pendingRequest.cancel(true);
            log.info("검증 요청 취소됨: {}", requestId);
            return true;
        }
        return false;
    }

    public boolean cancelRevisionRequest(String requestId) {
        CompletableFuture<RevisionResponseDto> pendingRequest = pendingRevisionRequests.remove(requestId);
        if (pendingRequest != null && !pendingRequest.isDone()) {
            pendingRequest.cancel(true);
            log.info("재생성 요청 취소됨: {}", requestId);
            return true;
        }
        return false;
    }
}