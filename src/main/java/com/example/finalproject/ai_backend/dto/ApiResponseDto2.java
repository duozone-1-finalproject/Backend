// src/main/java/com/example/ai_backend/dto/ApiResponseDto.java
package com.example.finalproject.ai_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponseDto2<T> {

    @JsonProperty("result")
    private String result;      // 응답 결과

    @JsonProperty("status")
    private String status;      // 에러 및 정보 코드

    @JsonProperty("message")
    private String message;     // 에러 및 정보 메시지

    @JsonProperty("data")
    private T data;             // 실제 데이터

    public static <T> ApiResponseDto2<T> success(T data) {
        return ApiResponseDto2.<T>builder()
                .result("SUCCESS")
                .status("200")
                .message("정상 처리되었습니다.")
                .data(data)
                .build();
    }

    public static <T> ApiResponseDto2<T> success(T data, String message) {
        return ApiResponseDto2.<T>builder()
                .result("SUCCESS")
                .status("200")
                .message(message)
                .data(data)
                .build();
    }

    public static <T> ApiResponseDto2<T> error(String status, String message) {
        return ApiResponseDto2.<T>builder()
                .result("ERROR")
                .status(status)
                .message(message)
                .data(null)
                .build();
    }

    public static <T> ApiResponseDto2<T> error(String status, String message, T data) {
        return ApiResponseDto2.<T>builder()
                .result("ERROR")
                .status(status)
                .message(message)
                .data(data)
                .build();
    }
}