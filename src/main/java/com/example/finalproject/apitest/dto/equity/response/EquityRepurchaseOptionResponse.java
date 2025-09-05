package com.example.finalproject.apitest.dto.equity.response;

import com.example.finalproject.apitest.entity.equity.EquityRepurchaseOption;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EquityRepurchaseOptionResponse {
    private String rceptNo;
    private String corpCls;
    private String corpCode;
    private String corpName;
    private String grtrs;
    private String exavivr;
    private String grtcnt; // Long to String
    private String expd;
    private String exprc; // Long to String

    public static EquityRepurchaseOptionResponse from(EquityRepurchaseOption entity) {
        return EquityRepurchaseOptionResponse.builder()
                .rceptNo(entity.getRceptNo())
                .corpCls(entity.getCorpCls())
                .corpCode(entity.getCorpCode())
                .corpName(entity.getCorpName())
                .grtrs(entity.getGrtrs())
                .exavivr(entity.getExavivr())
                .grtcnt(entity.getGrtcnt() != null ? String.valueOf(entity.getGrtcnt()) : null)
                .expd(entity.getExpd())
                .exprc(entity.getExprc() != null ? String.valueOf(entity.getExprc()) : null)
                .build();
    }
}