package com.example.finalproject.ai_backend.service;

import com.example.finalproject.ai_backend.dto.EquityAnnotationRequestDto;
import com.example.finalproject.ai_backend.dto.EquityAnnotationResponseDto;
import com.example.finalproject.ai_backend.dto.FastApiRequestDto;
import com.example.finalproject.ai_backend.dto.FastApiResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class EquityAnnotationService {

    private final FastApiCommunicationService fastApiCommunicationService;

    /**
     * Kafka를 통한 FastAPI 서버로 주식 공모 주석 생성 요청
     */
    public CompletableFuture<EquityAnnotationResponseDto> generateEquityAnnotation(
            EquityAnnotationRequestDto request) {

        // 고유한 요청 ID 생성
        String requestId = generateRequestId(request.getCompanyName());

        log.info("주식 공모 주석 생성 요청 시작: company={}, requestId={}",
                request.getCompanyName(), requestId);

        // EquityAnnotationRequestDto를 FastApiRequestDto로 변환
        FastApiRequestDto fastApiRequest = FastApiRequestDto.fromEquityRequest(request, requestId);

        return fastApiCommunicationService.requestEquityAnnotation(fastApiRequest)
                .thenApply(this::handleFastApiResponse)
                .exceptionally(throwable -> handleRequestException(throwable, request.getCompanyName()));
    }

    /**
     * FastAPI 응답을 EquityAnnotationResponseDto로 변환
     */
    private EquityAnnotationResponseDto handleFastApiResponse(FastApiResponseDto fastApiResponse) {
        try {
            if (fastApiResponse.isSuccess()) {
                log.info("FastAPI 응답 성공: company={}, requestId={}",
                        fastApiResponse.getCompanyName(), fastApiResponse.getRequestId());
                return fastApiResponse.toEquityResponse();
            } else {
                log.warn("FastAPI 응답 실패: company={}, requestId={}, error={}",
                        fastApiResponse.getCompanyName(), fastApiResponse.getRequestId(),
                        fastApiResponse.getErrorMessage());
                return EquityAnnotationResponseDto.createDefault(fastApiResponse.getCompanyName());
            }
        } catch (Exception e) {
            log.error("FastAPI 응답 처리 중 오류 발생: company={}",
                    fastApiResponse.getCompanyName(), e);
            return EquityAnnotationResponseDto.createDefault(fastApiResponse.getCompanyName());
        }
    }

    /**
     * 요청 실패 시 기본 응답 생성
     */
    private EquityAnnotationResponseDto handleRequestException(Throwable throwable, String companyName) {
        log.error("주식 공모 주석 생성 요청 실패: company={}", companyName, throwable);

        EquityAnnotationResponseDto defaultResponse = EquityAnnotationResponseDto.createDefault(companyName);
        // 실패 상태로 설정
        defaultResponse.setProcessing_status("FAILED");

        return defaultResponse;
    }

    /**
     * 고유한 요청 ID 생성
     */
    private String generateRequestId(String companyName) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        String sanitizedCompanyName = companyName != null ?
                companyName.replaceAll("[^a-zA-Z0-9가-힣]", "").substring(0, Math.min(companyName.length(), 10)) :
                "unknown";

        return String.format("equity_%s_%s_%s", sanitizedCompanyName, timestamp, uuid);
    }

    /**
     * Kafka 연결 상태 확인
     */
    public CompletableFuture<Boolean> checkKafkaConnection() {
        log.info("Kafka 연결 상태 확인 요청");
        return fastApiCommunicationService.checkKafkaConnection()
                .thenApply(isConnected -> {
                    log.info("Kafka 연결 상태: {}", isConnected ? "정상" : "실패");
                    return isConnected;
                })
                .exceptionally(throwable -> {
                    log.error("Kafka 연결 상태 확인 실패", throwable);
                    return false;
                });
    }

    /**
     * 서비스 상태 확인 (Kafka 연결 + 대기 중인 요청 수)
     */
    public CompletableFuture<ServiceHealthDto> checkServiceHealth() {
        log.info("서비스 상태 확인 요청");

        return checkKafkaConnection()
                .thenApply(kafkaConnected -> {
                    int pendingRequests = fastApiCommunicationService.getPendingRequestCount();

                    ServiceHealthDto health = ServiceHealthDto.builder()
                            .kafkaConnected(kafkaConnected)
                            .pendingRequestCount(pendingRequests)
                            .serviceStatus(kafkaConnected ? "HEALTHY" : "UNHEALTHY")
                            .timestamp(java.time.LocalDateTime.now().toString())
                            .build();

                    log.info("서비스 상태: kafka={}, pending={}, status={}",
                            kafkaConnected, pendingRequests, health.getServiceStatus());

                    return health;
                })
                .exceptionally(throwable -> {
                    log.error("서비스 상태 확인 실패", throwable);
                    return ServiceHealthDto.builder()
                            .kafkaConnected(false)
                            .pendingRequestCount(0)
                            .serviceStatus("ERROR")
                            .errorMessage(throwable.getMessage())
                            .timestamp(java.time.LocalDateTime.now().toString())
                            .build();
                });
    }

    /**
     * 특정 요청 취소
     */
    public boolean cancelRequest(String requestId) {
        log.info("요청 취소: requestId={}", requestId);
        boolean cancelled = fastApiCommunicationService.cancelRequest(requestId);
        log.info("요청 취소 결과: requestId={}, cancelled={}", requestId, cancelled);
        return cancelled;
    }

    /**
     * 서비스 상태 정보를 담는 DTO
     */
    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class ServiceHealthDto {
        private boolean kafkaConnected;
        private int pendingRequestCount;
        private String serviceStatus;
        private String errorMessage;
        private String timestamp;
    }
}