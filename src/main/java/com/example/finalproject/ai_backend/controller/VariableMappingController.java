// 2. VariableMappingController.java 수정
package com.example.finalproject.ai_backend.controller;

import com.example.finalproject.ai_backend.dto.ApiResponseDto2;
import com.example.finalproject.ai_backend.dto.FrontendVariableResponseDto;
import com.example.finalproject.ai_backend.service.MainVariableMappingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@RequestMapping("/api/v1/variables")
@RequiredArgsConstructor
@CrossOrigin(origins = "${frontend.url}", allowCredentials = "true") // 환경변수로 변경
public class VariableMappingController {

    private final MainVariableMappingService mainVariableMappingService;

    @GetMapping("/mapping/{corpCode}")
    public CompletableFuture<ResponseEntity<ApiResponseDto2<FrontendVariableResponseDto>>> getVariableMapping(
            @PathVariable String corpCode) {

        log.info("변수 매핑 요청 수신: corpCode={}", corpCode);

        return mainVariableMappingService.processVariableMapping(corpCode)
                .thenApply(result -> {
                    log.info("변수 매핑 요청 완료: corpCode={}", corpCode);

                    ApiResponseDto2<FrontendVariableResponseDto> response =
                            ApiResponseDto2.success(result, "변수 매핑이 완료되었습니다.");

                    return ResponseEntity.ok(response);
                })
                .exceptionally(throwable -> {
                    log.error("변수 매핑 요청 실패: corpCode={}", corpCode, throwable);

                    ApiResponseDto2<FrontendVariableResponseDto> response =
                            ApiResponseDto2.error("500", "변수 매핑 처리 중 오류가 발생했습니다: " + throwable.getMessage());

                    return ResponseEntity.internalServerError().body(response);
                });
    }

    @GetMapping("/health")
    public CompletableFuture<ResponseEntity<ApiResponseDto2<String>>> checkHealth() {

        return mainVariableMappingService.checkSystemHealth()
                .thenApply(isHealthy -> {
                    if (isHealthy) {
                        ApiResponseDto2<String> response =
                                ApiResponseDto2.success("시스템이 정상 작동 중입니다.", "HEALTHY");
                        return ResponseEntity.ok(response);
                    } else {
                        ApiResponseDto2<String> response =
                                ApiResponseDto2.error("503", "시스템 연결에 문제가 있습니다.");
                        return ResponseEntity.status(503).body(response);
                    }
                })
                .exceptionally(throwable -> {
                    log.error("헬스체크 실패", throwable);
                    ApiResponseDto2<String> response =
                            ApiResponseDto2.error("500", "헬스체크 실행 중 오류가 발생했습니다.");
                    return ResponseEntity.internalServerError().body(response);
                });
    }

    @GetMapping("/status/{requestId}")
    public CompletableFuture<ResponseEntity<ApiResponseDto2<String>>> getRequestStatus(
            @PathVariable String requestId) {

        return mainVariableMappingService.getRequestStatus(requestId)
                .thenApply(status -> {
                    ApiResponseDto2<String> response =
                            ApiResponseDto2.success(status, "요청 상태를 조회했습니다.");
                    return ResponseEntity.ok(response);
                })
                .exceptionally(throwable -> {
                    log.error("요청 상태 조회 실패: requestId={}", requestId, throwable);
                    ApiResponseDto2<String> response =
                            ApiResponseDto2.error("404", "요청을 찾을 수 없습니다.");
                    return ResponseEntity.notFound().build();
                });
    }
}