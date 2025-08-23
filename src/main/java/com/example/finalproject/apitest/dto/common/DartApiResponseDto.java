package com.example.finalproject.apitest.dto.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Getter;

import java.util.Map;

@Data
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class DartApiResponseDto {
    private String status;
    private String message;

    // 각 그룹별 데이터 - JSON에서 동적으로 파싱
    private Map<String, Object> data;
}
