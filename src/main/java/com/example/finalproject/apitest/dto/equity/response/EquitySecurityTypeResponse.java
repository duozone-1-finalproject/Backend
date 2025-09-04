package com.example.finalproject.apitest.dto.equity.response;

import com.example.finalproject.apitest.entity.equity.EquitySecurityType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EquitySecurityTypeResponse {
    private String rceptNo;
    private String corpCls;
    private String corpCode;
    private String corpName;
    private String stksen;
    private String stkcnt; // Long to String
    private String fv; // Long to String
    private String slprc; // Long to String
    private String slta; // Long to String
    private String slmthn;

    public static EquitySecurityTypeResponse from(EquitySecurityType entity) {
        return EquitySecurityTypeResponse.builder()
                .rceptNo(entity.getRceptNo())
                .corpCls(entity.getCorpCls())
                .corpCode(entity.getCorpCode())
                .corpName(entity.getCorpName())
                .stksen(entity.getStksen())
                .stkcnt(entity.getStkcnt() != null ? String.valueOf(entity.getStkcnt()) : null)
                .fv(entity.getFv() != null ? String.valueOf(entity.getFv()) : null)
                .slprc(entity.getSlprc() != null ? String.valueOf(entity.getSlprc()) : null)
                .slta(entity.getSlta() != null ? String.valueOf(entity.getSlta()) : null)
                .slmthn(entity.getSlmthn())
                .build();
    }
}