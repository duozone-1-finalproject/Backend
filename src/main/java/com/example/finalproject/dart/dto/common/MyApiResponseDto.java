package com.example.finalproject.dart.dto.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MyApiResponseDto<T> {

    private final String status;
    private final String message;
    private final T data;


    public static <T> MyApiResponseDto<T> ok(T data) {
        return new MyApiResponseDto<>("SUCCESS", "요청에 성공했습니다.", data);
    }

    public static MyApiResponseDto<String> error(String message) {
        return new MyApiResponseDto<>("ERROR", message, null);
    }
}
