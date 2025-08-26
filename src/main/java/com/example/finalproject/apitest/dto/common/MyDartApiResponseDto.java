package com.example.finalproject.apitest.dto.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MyDartApiResponseDto <T>{
    private String status;
    private String message;

    private T data;

    public static <T> MyDartApiResponseDto<T> ok(T data) {
        return new MyDartApiResponseDto<>("SUCCESS", "요청에 성공했습니다.", data);
    }
    public static <T> MyDartApiResponseDto<T> ok() {
        return new MyDartApiResponseDto<>("SUCCESS", "요청에 성공했습니다.", null);
    }
    public static <T> MyDartApiResponseDto<T> error(String message) {
        return new MyDartApiResponseDto<>("ERROR", message, null);
    }
}
