package com.example.finalproject.apitest.dto.equity.response;

import com.example.finalproject.apitest.entity.equity.EquityFundUsage;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EquityFundUsageResponse {
    private String rceptNo;
    private String corpCls;
    private String corpCode;
    private String corpName;
    private String se;
    private String amt; // Long to String

    public static EquityFundUsageResponse from(EquityFundUsage entity) {
        return EquityFundUsageResponse.builder()
                .rceptNo(entity.getRceptNo())
                .corpCls(entity.getCorpCls())
                .corpCode(entity.getCorpCode())
                .corpName(entity.getCorpName())
                .se(entity.getSe())
                .amt(entity.getAmt() != null ? String.valueOf(entity.getAmt()) : null)
                .build();
    }
}