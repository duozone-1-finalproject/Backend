package com.example.finalproject.apitest.dto;
// 이사·감사 전체의 보수현황(보수지급금액 - 유형별)
import com.example.finalproject.apitest.entity.DartDirectorAndAuditorCompensation;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class DartDirectorAndAuditorCompensationResponse {

    private String rceptNo; // 접수번호
    private String corpCls; // 법인구분
    private String corpCode; // 고유번호
    private String corpName; // 회사명
    private String se; // 구분
    private Long nmpr; // 인원수
    private Long pymntTotamt; // 보수총액
    private Long psn1AvrgPymntamt; // 1인당 평균보수액
    private String rm; // 비고
    private LocalDate stlmDt; // 결산기준일


    public static DartDirectorAndAuditorCompensationResponse from(DartDirectorAndAuditorCompensation entity) {
        return DartDirectorAndAuditorCompensationResponse.builder()
                .rceptNo(entity.getRceptNo())
                .corpCls(entity.getCorpCls())
                .corpCode(entity.getCorpCode())
                .corpName(entity.getCorpName())
                .se(entity.getSe())
                .nmpr(entity.getNmpr())
                .pymntTotamt(entity.getPymntTotamt())
                .psn1AvrgPymntamt(entity.getPsn1AvrgPymntamt())
                .rm(entity.getRm())
                .stlmDt(entity.getStlmDt())
                .build();
    }
}
