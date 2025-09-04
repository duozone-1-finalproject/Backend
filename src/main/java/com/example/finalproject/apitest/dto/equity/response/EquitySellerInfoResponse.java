package com.example.finalproject.apitest.dto.equity.response;

import com.example.finalproject.apitest.entity.equity.EquitySellerInfo;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EquitySellerInfoResponse {
    private String rceptNo;
    private String corpCls;
    private String corpCode;
    private String corpName;
    private String hdr;
    private String rlCmp;
    private String bfslHdstk; // Long to String
    private String slstk; // Long to String
    private String atslHdstk; // Long to String

    public static EquitySellerInfoResponse from(EquitySellerInfo entity) {
        return EquitySellerInfoResponse.builder()
                .rceptNo(entity.getRceptNo())
                .corpCls(entity.getCorpCls())
                .corpCode(entity.getCorpCode())
                .corpName(entity.getCorpName())
                .hdr(entity.getHdr())
                .rlCmp(entity.getRlCmp())
                .bfslHdstk(entity.getBfslHdstk() != null ? String.valueOf(entity.getBfslHdstk()) : null)
                .slstk(entity.getSlstk() != null ? String.valueOf(entity.getSlstk()) : null)
                .atslHdstk(entity.getAtslHdstk() != null ? String.valueOf(entity.getAtslHdstk()) : null)
                .build();
    }
}