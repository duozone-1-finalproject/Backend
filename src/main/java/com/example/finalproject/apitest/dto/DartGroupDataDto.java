package com.example.finalproject.apitest.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DartGroupDataDto {
    @JsonProperty("grouptitle") // 실제 키가 "title"이면 여기를 "title"로 바꾸세요.
    private String groupTitle;

    @JsonProperty("list")
    private List<Object> list; // 필요 시: List<DartSecuritiesInfoDto> 등으로 제네릭 지정
}
