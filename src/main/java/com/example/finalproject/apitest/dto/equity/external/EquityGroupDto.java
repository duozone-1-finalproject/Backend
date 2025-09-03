package com.example.finalproject.apitest.dto.equity.external;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import java.util.List;

@Data
public class EquityGroupDto {
    private String title;
    private List<JsonNode> list; // 다양한 타입의 리스트를 받기 위해 JsonNode 사용
}