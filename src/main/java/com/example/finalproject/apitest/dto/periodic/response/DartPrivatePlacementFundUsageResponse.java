package com.example.finalproject.apitest.dto.periodic.response;

// 사모자금의 사용내역
import com.example.finalproject.apitest.entity.periodic.DartPrivatePlacementFundUsage;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class DartPrivatePlacementFundUsageResponse {

    private String rceptNo; // 접수번호
    private String corpCls; // 법인구분
    private String corpCode; // 고유번호
    private String corpName; // 회사명
    private String seNm; // 구분
    private String tm; // 회차
    private LocalDate payDe; // 납입일
    private Long payAmount; // 납입금액 (구)
    private String cptalUsePlan; // 자금사용 계획 (구)
    private String realCptalUseSttus; // 실제 자금사용 현황 (구)
    private String mtrptCptalUsePlanUseprps; // 주요사항보고서의 자금사용 계획(사용용도) (신)
    private Long mtrptCptalUsePlanPicureAmount; // 주요사항보고서의 자금사용 계획(조달금액) (신)
    private String realCptalUseDtlsCn; // 실제 자금사용 내역(내용) (신)
    private Long realCptalUseDtlsAmount; // 실제 자금사용 내역(금액) (신)
    private String dffrncOccrrncResn; // 차이발생 사유 등
    private LocalDate stlmDt; // 결산기준일

    /**
     * Entity 객체를 Response DTO 객체로 변환하는 정적 팩토리 메소드
     */
    public static DartPrivatePlacementFundUsageResponse from(DartPrivatePlacementFundUsage entity) {
        return DartPrivatePlacementFundUsageResponse.builder()
                .rceptNo(entity.getRceptNo())
                .corpCls(entity.getCorpCls())
                .corpCode(entity.getCorpCode())
                .corpName(entity.getCorpName())
                .seNm(entity.getSeNm())
                .tm(entity.getTm())
                .payDe(entity.getPayDe())
                .payAmount(entity.getPayAmount())
                .cptalUsePlan(entity.getCptalUsePlan())
                .realCptalUseSttus(entity.getRealCptalUseSttus())
                .mtrptCptalUsePlanUseprps(entity.getMtrptCptalUsePlanUseprps())
                .mtrptCptalUsePlanPicureAmount(entity.getMtrptCptalUsePlanPicureAmount())
                .realCptalUseDtlsCn(entity.getRealCptalUseDtlsCn())
                .realCptalUseDtlsAmount(entity.getRealCptalUseDtlsAmount())
                .dffrncOccrrncResn(entity.getDffrncOccrrncResn())
                .stlmDt(entity.getStlmDt())
                .build();
    }
}