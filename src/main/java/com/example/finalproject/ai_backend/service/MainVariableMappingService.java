// src/main/java/com/example/finalproject/ai_backend/service/MainVariableMappingService.java
package com.example.finalproject.ai_backend.service;

import com.example.finalproject.ai_backend.dto.FrontendVariableResponseDto;
import com.example.finalproject.ai_backend.dto.VariableMappingRequestDto;
import com.example.finalproject.ai_backend.dto.VariableMappingResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class MainVariableMappingService {

    private final CompanyDataMappingService companyDataMappingService;
    private final VariableMappingService variableMappingService;

    /**
     * 회사 코드로 변수 매핑 요청 (전체 플로우)
     * 1. 사용자 입력 corpCode로 DB 조회
     * 2. 회사코드, 회사명, 업종코드, 업종명 4개 변수 추출
     * 3. AI에게 전송
     * 4. 프론트엔드용 응답 변환
     */
    public CompletableFuture<FrontendVariableResponseDto> processVariableMapping(String corpCode) {
        String requestId = generateRequestId();
        log.info("=== 변수 매핑 프로세스 시작 ===");
        log.info("requestId: {}", requestId);
        log.info("입력 corpCode: {}", corpCode);

        try {
            // 1. 회사 존재 여부 먼저 확인
            if (!companyDataMappingService.existsCompany(corpCode)) {
                log.error("존재하지 않는 회사 코드: {}", corpCode);
                return CompletableFuture.completedFuture(createErrorResponse("존재하지 않는 회사입니다."));
            }

            // 2. 데이터 매핑 전체 검증 (디버깅용)
            companyDataMappingService.validateDataMapping(corpCode);

            // 3. DB에서 회사 데이터 조회 및 AI 요청 DTO 생성
            VariableMappingRequestDto aiRequest = companyDataMappingService.createAiRequestData(corpCode, requestId);

            log.info("=== AI 전송 데이터 확인 ===");
            log.info("requestId: {}", aiRequest.getRequestId());
            log.info("corpCode: {}", aiRequest.getCorpCode());
            log.info("corpName: {}", aiRequest.getCorpName());
            log.info("indutyCode: {}", aiRequest.getIndutyCode());
            log.info("indutyName: {}", aiRequest.getIndutyName());

            // 4. AI 서비스에 Kafka로 요청
            log.info("Kafka를 통해 AI 서버에 요청 전송 중...");
            return variableMappingService.requestVariableMapping(aiRequest)
                    .thenApply(aiResponse -> {
                        log.info("AI 응답 수신 완료: requestId={}, status={}",
                                requestId, aiResponse.getStatus());

                        if ("SUCCESS".equals(aiResponse.getStatus())) {
                            FrontendVariableResponseDto frontendResponse = FrontendVariableResponseDto.from(aiResponse);
                            log.info("프론트엔드 응답 변환 완료");
                            log.info("프론트엔드 응답 데이터: {}", frontendResponse);
                            return frontendResponse;
                        } else {
                            log.error("AI 처리 실패: requestId={}, status={}", requestId, aiResponse.getStatus());
                            return createErrorResponse("AI 처리 중 오류가 발생했습니다.");
                        }
                    })
                    .exceptionally(throwable -> {
                        log.error("AI 요청/응답 처리 실패: requestId={}", requestId, throwable);
                        return createErrorResponse("AI 서버 통신 중 오류가 발생했습니다: " + throwable.getMessage());
                    });

        } catch (Exception e) {
            log.error("변수 매핑 프로세스 초기화 실패: requestId={}", requestId, e);
            return CompletableFuture.completedFuture(
                    createErrorResponse("회사 정보 조회에 실패했습니다: " + e.getMessage())
            );
        }
    }

    /**
     * 에러 응답 생성
     */
    private FrontendVariableResponseDto createErrorResponse(String errorMessage) {
        return FrontendVariableResponseDto.builder()
                .s3_1a_1(errorMessage)
                .s3_1b_1(errorMessage)
                .s3_1c_1(errorMessage)
                .build();
    }

    /**
     * 요청 상태 확인
     */
    public CompletableFuture<String> getRequestStatus(String requestId) {
        return CompletableFuture.supplyAsync(() -> {
            int pendingCount = variableMappingService.getPendingRequestCount();
            return "대기 중인 요청 수: " + pendingCount;
        });
    }

    /**
     * 요청 취소
     */
    public CompletableFuture<Boolean> cancelRequest(String requestId) {
        return CompletableFuture.supplyAsync(() -> variableMappingService.cancelRequest(requestId));
    }

    /**
     * 시스템 상태 확인
     */
    public CompletableFuture<Boolean> checkSystemHealth() {
        return variableMappingService.checkKafkaConnection();
    }

    /**
     * 고유한 요청 ID 생성
     */
    private String generateRequestId() {
        return "VAR_" + UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase();
    }
}