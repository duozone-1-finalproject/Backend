package com.example.finalproject.apitest.dto.equity.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class DartEquitySecuritiesApiResponse {
    private String status;
    private String message;
    @JsonProperty("group")
    private List<EquityGroupDto> groups;
}
