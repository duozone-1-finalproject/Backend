package com.example.finalproject.apitest.dto.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Getter
@NoArgsConstructor
public class DartApiResponseDto<T> {
    private String status;
    private String message;

    private List<T> list;

    @JsonUnwrapped
    private T singleResult;
}
