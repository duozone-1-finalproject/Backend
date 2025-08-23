package com.example.finalproject.apitest.dto.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * 외부 API 데이터를 DB에 저장한 후 처리 결과를 반환하는 공통 DTO
 * @param <T> 저장된 후 반환될 데이터(Entity)의 타입
 */
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL) // null인 필드는 JSON으로 변환 시 제외
public class DataIngestionResponseDto<T> {

    private final String status; // 처리 상태 코드 (예: "000" - 정상)
    private final String message;  // 처리 결과 메시지
    private final List<T> list;    // 저장된 데이터 리스트

    @Builder
    private DataIngestionResponseDto(String status, String message, List<T> list) {
        this.status = status;
        this.message = message;
        this.list = list;
    }

    // --- 성공 응답을 생성하는 정적 팩토리 메소드 ---

    /**
     * 성공적인 데이터 처리 응답을 생성합니다. (저장된 데이터 포함)
     * @param data 저장된 데이터 리스트
     * @return 성공 응답 DTO
     */
    public static <T> DataIngestionResponseDto<T> success(List<T> data) {
        return DataIngestionResponseDto.<T>builder()
                .status("000") // 성공 기본 상태 코드
                .message("데이터가 성공적으로 저장되었습니다.") // 성공 기본 메시지
                .list(data)
                .build();
    }

    /**
     * 성공적인 데이터 처리 응답을 생성합니다. (저장된 데이터와 커스텀 메시지 포함)
     * @param data 저장된 데이터 리스트
     * @param message 직접 설정할 성공 메시지
     * @return 성공 응답 DTO
     */
    public static <T> DataIngestionResponseDto<T> success(List<T> data, String message) {
        return DataIngestionResponseDto.<T>builder()
                .status("000")
                .message(message)
                .list(data)
                .build();
    }

    // --- 실패/에러 응답을 생성하는 정적 팩토리 메소드 ---

    /**
     * 데이터 처리 실패 응답을 생성합니다.
     * @param status 직접 설정할 상태 코드
     * @param message 직접 설정할 에러 메시지
     * @return 실패 응답 DTO
     */
    public static <T> DataIngestionResponseDto<T> error(String status, String message) {
        return DataIngestionResponseDto.<T>builder()
                .status(status)
                .message(message)
                .list(null) // 실패 시에는 데이터를 보내지 않음
                .build();
    }
}