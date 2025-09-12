package com.example.finalproject.apitest.dto.equity.external;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import java.util.List;

@Data
public class EquityGroupDto {
    private String title;
    private JsonNode list; // [수정] List<JsonNode>에서 JsonNode로 변경
}