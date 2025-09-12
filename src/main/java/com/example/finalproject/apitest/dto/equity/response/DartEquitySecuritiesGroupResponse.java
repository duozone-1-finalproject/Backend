package com.example.finalproject.apitest.dto.equity.response;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class DartEquitySecuritiesGroupResponse {
    private List<TitledGroup<?>> group;
}