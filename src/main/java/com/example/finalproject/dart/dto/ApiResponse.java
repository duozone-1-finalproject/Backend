package com.example.finalproject.dart.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ApiResponse<T> {
    private int statusCode;
    private String message;
    private T data;


    // 성공 응답 (200)
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(200, "요청에 성공했습니다.", data);
    }

    // 실패 응답 (400, 자유 메세지)
    public static <T> ApiResponse<T> fail(String message) {
        return new ApiResponse<>(400, message, null);
    }

    // 전부 임의로 사용
    public static <T> ApiResponse<T> of(int statusCode, String message, T data) {
        return new ApiResponse<>(statusCode, message, data);
    }
}