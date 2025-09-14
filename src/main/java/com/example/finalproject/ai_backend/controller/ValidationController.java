package com.example.finalproject.ai_backend.controller;

import com.example.finalproject.ai_backend.dto.*;
import com.example.finalproject.ai_backend.service.ValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@RequestMapping("/api/validation")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000"}, allowCredentials = "true")
public class ValidationController {

    private final ValidationService validationService;

    /**
     * 검증 요청
     * POST /api/validation/check
     */
    @PostMapping("/check")
    public CompletableFuture<ResponseEntity<ApiResponseDto2<ValidationDto>>> validateContent(
            @RequestBody CheckRequestDto request) {

        String requestId = generateRequestId();
        log.info("검증 요청 수신: requestId={}", requestId);

        // Kafka 요청 DTO 생성
        ValidationRequestDto kafkaRequest = ValidationRequestDto.builder()
                .requestId(requestId)
                .indutyName(request.getIndutyName())
                .section(request.getSection())
                .draft(request.getDraft())
                .build();

        return validationService.requestValidation(kafkaRequest)
                .thenApply(response -> {
                    log.info("검증 요청 완료: requestId={}, status={}", requestId, response.getStatus());

                    if ("SUCCESS".equals(response.getStatus())) {
                        ApiResponseDto2<ValidationDto> apiResponse =
                                ApiResponseDto2.success(response.getValidationResult(), "검증이 완료되었습니다.");
                        return ResponseEntity.ok(apiResponse);
                    } else {
                        log.error("검증 처리 실패: requestId={}, error={}", requestId, response.getErrorMessage());
                        ApiResponseDto2<ValidationDto> apiResponse =
                                ApiResponseDto2.error("500", "검증 처리 중 오류가 발생했습니다: " + response.getErrorMessage());
                        return ResponseEntity.internalServerError().body(apiResponse);
                    }
                })
                .exceptionally(throwable -> {
                    log.error("검증 요청 실패: requestId={}", requestId, throwable);
                    ApiResponseDto2<ValidationDto> apiResponse =
                            ApiResponseDto2.error("500", "검증 요청 처리 중 오류가 발생했습니다: " + throwable.getMessage());
                    return ResponseEntity.internalServerError().body(apiResponse);
                });
    }

    /**
     * 재생성 요청
     * POST /api/validation/revise
     */
    @PostMapping("/revise")
    public CompletableFuture<ResponseEntity<ApiResponseDto2<String>>> reviseContent(
            @RequestBody ValidationDto.Issue issue) {

        String requestId = generateRequestId();
        log.info("재생성 요청 수신: requestId={}", requestId);

        // Kafka 요청 DTO 생성
        RevisionRequestDto kafkaRequest = RevisionRequestDto.builder()
                .requestId(requestId)
                .issue(issue)
                .build();

        return validationService.requestRevision(kafkaRequest)
                .thenApply(response -> {
                    log.info("재생성 요청 완료: requestId={}, status={}", requestId, response.getStatus());

                    if ("SUCCESS".equals(response.getStatus())) {
                        ApiResponseDto2<String> apiResponse =
                                ApiResponseDto2.success(response.getRevisedContent(), "재생성이 완료되었습니다.");
                        return ResponseEntity.ok(apiResponse);
                    } else {
                        log.error("재생성 처리 실패: requestId={}, error={}", requestId, response.getErrorMessage());
                        ApiResponseDto2<String> apiResponse =
                                ApiResponseDto2.error("500", "재생성 처리 중 오류가 발생했습니다: " + response.getErrorMessage());
                        return ResponseEntity.internalServerError().body(apiResponse);
                    }
                })
                .exceptionally(throwable -> {
                    log.error("재생성 요청 실패: requestId={}", requestId, throwable);
                    ApiResponseDto2<String> apiResponse =
                            ApiResponseDto2.error("500", "재생성 요청 처리 중 오류가 발생했습니다: " + throwable.getMessage());
                    return ResponseEntity.internalServerError().body(apiResponse);
                });
    }

    /**
     * 요청 상태 확인
     */
    @GetMapping("/status")
    public ResponseEntity<ApiResponseDto2<String>> getStatus() {
        int validationCount = validationService.getPendingValidationRequestCount();
        int revisionCount = validationService.getPendingRevisionRequestCount();

        String statusMessage = String.format("대기 중인 검증 요청: %d건, 재생성 요청: %d건", validationCount, revisionCount);

        ApiResponseDto2<String> response = ApiResponseDto2.success(statusMessage, "상태 조회 완료");
        return ResponseEntity.ok(response);
    }

    /**
     * 고유한 요청 ID 생성
     */
    private String generateRequestId() {
        return "VAL_" + UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase();
    }
}
