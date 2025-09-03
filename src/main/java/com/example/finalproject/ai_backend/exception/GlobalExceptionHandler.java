// src/main/java/com/example/ai_backend/exception/GlobalExceptionHandler.java
package com.example.finalproject.ai_backend.exception;

import com.example.finalproject.ai_backend.dto.ApiResponseDto2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientException;

import java.util.concurrent.CompletionException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 유효성 검증 실패 예외 처리
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseDto2<String>> handleValidationException(MethodArgumentNotValidException e) {
        log.error("유효성 검증 실패", e);

        ApiResponseDto2<String> response = ApiResponseDto2.error(
                "400",
                "입력값이 올바르지 않습니다: " + e.getBindingResult().getFieldError().getDefaultMessage()
        );

        return ResponseEntity.badRequest().body(response);
    }

    /**
     * WebClient 예외 처리
     */
    @ExceptionHandler(WebClientException.class)
    public ResponseEntity<ApiResponseDto2<String>> handleWebClientException(WebClientException e) {
        log.error("외부 API 호출 실패", e);

        ApiResponseDto2<String> response = ApiResponseDto2.error(
                "502",
                "외부 서비스 호출에 실패했습니다."
        );

        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(response);
    }

    /**
     * 비동기 처리 예외 처리
     */
    @ExceptionHandler(CompletionException.class)
    public ResponseEntity<ApiResponseDto2<String>> handleCompletionException(CompletionException e) {
        log.error("비동기 처리 실패", e);

        ApiResponseDto2<String> response = ApiResponseDto2.error(
                "500",
                "요청 처리 중 오류가 발생했습니다."
        );

        return ResponseEntity.internalServerError().body(response);
    }

    /**
     * 일반 예외 처리
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDto2<String>> handleGeneralException(Exception e) {
        log.error("예상치 못한 오류 발생", e);

        ApiResponseDto2<String> response = ApiResponseDto2.error(
                "500",
                "서버 내부 오류가 발생했습니다."
        );

        return ResponseEntity.internalServerError().body(response);
    }
}