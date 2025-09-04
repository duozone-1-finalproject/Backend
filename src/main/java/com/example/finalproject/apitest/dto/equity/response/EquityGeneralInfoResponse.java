package com.example.finalproject.apitest.dto.equity.response;

import com.example.finalproject.apitest.entity.equity.EquityGeneralInfo;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDate;

@Getter
@Builder
public class EquityGeneralInfoResponse {
    private String rceptNo;
    private String corpCls;
    private String corpCode;
    private String corpName;
    private LocalDate sbd;
    private LocalDate pymd;
    private LocalDate sband;
    private LocalDate asand;
    private LocalDate asstd;
    private String exstk;
    private String exprc; // Long to String
    private String expd;
    private String rptRcpn;

    public static EquityGeneralInfoResponse from(EquityGeneralInfo entity) {
        return EquityGeneralInfoResponse.builder()
                .rceptNo(entity.getRceptNo())
                .corpCls(entity.getCorpCls())
                .corpCode(entity.getCorpCode())
                .corpName(entity.getCorpName())
                .sbd(entity.getSbd())
                .pymd(entity.getPymd())
                .sband(entity.getSband())
                .asand(entity.getAsand())
                .asstd(entity.getAsstd())
                .exstk(entity.getExstk())
                .exprc(entity.getExprc() != null ? String.valueOf(entity.getExprc()) : null)
                .expd(entity.getExpd())
                .rptRcpn(entity.getRptRcpn())
                .build();
    }
}