package com.example.finalproject.apitest.dto.periodic.response;

// 회계감사인의 명칭 및 감사의견
import com.example.finalproject.apitest.entity.periodic.DartAuditOpinion;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class DartAuditOpinionResponse {

    private String rceptNo; // 접수번호
    private String corpCls; // 법인구분
    private String corpCode; // 고유번호
    private String corpName; // 회사명
    private String bsnsYear; // 사업연도
    private String adtor; // 감사인
    private String adtOpinion; // 감사의견
    private String adtReprtSpcmntMatter; // 감사보고서 특기사항 (구)
    private String emphsMatter; // 강조사항 등 (신)
    private String coreAdtMatter; // 핵심감사사항 (신)
    private LocalDate stlmDt; // 결산기준일

    /**
     * Entity 객체를 Response DTO 객체로 변환하는 정적 팩토리 메소드
     */
    public static DartAuditOpinionResponse from(DartAuditOpinion entity) {
        return DartAuditOpinionResponse.builder()
                .rceptNo(entity.getRceptNo())
                .corpCls(entity.getCorpCls())
                .corpCode(entity.getCorpCode())
                .corpName(entity.getCorpName())
                .bsnsYear(entity.getBsnsYear())
                .adtor(entity.getAdtor())
                .adtOpinion(entity.getAdtOpinion())
                .adtReprtSpcmntMatter(entity.getAdtReprtSpcmntMatter())
                .emphsMatter(entity.getEmphsMatter())
                .coreAdtMatter(entity.getCoreAdtMatter())
                .stlmDt(entity.getStlmDt())
                .build();
    }
}