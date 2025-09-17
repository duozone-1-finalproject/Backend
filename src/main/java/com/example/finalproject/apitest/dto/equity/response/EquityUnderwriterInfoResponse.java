package com.example.finalproject.apitest.dto.equity.response;

import com.example.finalproject.apitest.entity.equity.EquityUnderwriterInfo;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EquityUnderwriterInfoResponse {
    private String rceptNo;
    private String corpCls;
    private String corpCode;
    private String corpName;
    private String actsen;
    private String actnmn;
    private String stksen;
    private String udtcnt; // Long to String
    private String udtamt; // Long to String
    private String udtprc; // Long to String
    private String udtmth;

    public static EquityUnderwriterInfoResponse from(EquityUnderwriterInfo entity) {
        return EquityUnderwriterInfoResponse.builder()
                .rceptNo(entity.getRceptNo())
                .corpCls(entity.getCorpCls())
                .corpCode(entity.getCorpCode())
                .corpName(entity.getCorpName())
                .actsen(entity.getActsen())
                .actnmn(entity.getActnmn())
                .stksen(entity.getStksen())
                .udtcnt(entity.getUdtcnt() != null ? String.valueOf(entity.getUdtcnt()) : null)
                .udtamt(entity.getUdtamt() != null ? String.valueOf(entity.getUdtamt()) : null)
                .udtprc(entity.getUdtprc() != null ? String.valueOf(entity.getUdtprc()) : null)
                .udtmth(entity.getUdtmth())
                .build();
    }
}