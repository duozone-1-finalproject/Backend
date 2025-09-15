package com.example.finalproject.ai_backend.controller;

import com.example.finalproject.ai_backend.dto.ApiResponseDto2;
import com.example.finalproject.ai_backend.dto.EquityAnnotationRequestDto;
import com.example.finalproject.ai_backend.dto.EquityAnnotationResponseDto;
import com.example.finalproject.ai_backend.service.EquityAnnotationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeoutException;

@Slf4j
@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
@CrossOrigin(origins = "${frontend.url}", allowCredentials = "true")
public class EquityAnnotationController {

    private final EquityAnnotationService equityAnnotationService;

    @PostMapping("/equity-annotation")
    public CompletableFuture<ResponseEntity<ApiResponseDto2<EquityAnnotationResponseDto>>> generateEquityAnnotation(
            @Valid @RequestBody EquityAnnotationRequestDto request) {

        log.info("주식 공모 주석 생성 요청 수신: company={}", request.getCompanyName());

        return equityAnnotationService.generateEquityAnnotation(request)
                .thenApply(response -> {
                    log.info("주식 공모 주석 생성 성공: company={}", request.getCompanyName());
                    return ResponseEntity.ok(ApiResponseDto2.success(
                            response, "주식 공모 주석이 성공적으로 생성되었습니다."
                    ));
                })
                .exceptionally(throwable -> {
                    if (throwable.getCause() instanceof TimeoutException) {
                        log.error("주식 공모 주석 생성 타임아웃: company={}", request.getCompanyName(), throwable);
                        return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT)
                                .body(ApiResponseDto2.error("408",
                                        "요청 처리 시간이 초과되었습니다. 잠시 후 다시 시도해주세요."));
                    } else {
                        log.error("주식 공모 주석 생성 실패: company={}", request.getCompanyName(), throwable);
                        return ResponseEntity.internalServerError()
                                .body(ApiResponseDto2.error("500",
                                        "주식 공모 주석 생성에 실패했습니다: " + throwable.getMessage()));
                    }
                });
    }

    @GetMapping("/equity-annotation/health")
    public ResponseEntity<ApiResponseDto2<String>> healthCheck() {
        log.info("FastAPI 연동 상태 확인 요청");
        return ResponseEntity.ok(ApiResponseDto2.success(
                "OK", "FastAPI 연동 서비스가 정상적으로 동작하고 있습니다."
        ));
    }
}