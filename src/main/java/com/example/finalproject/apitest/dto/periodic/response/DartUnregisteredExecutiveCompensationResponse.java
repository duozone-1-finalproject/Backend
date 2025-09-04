package com.example.finalproject.apitest.dto.periodic.response;
// 미등기임원 보수현황
import com.example.finalproject.apitest.entity.periodic.DartUnregisteredExecutiveCompensation;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class DartUnregisteredExecutiveCompensationResponse {

    private String rceptNo; // 접수번호
    private String corpCls; // 법인구분
    private String corpCode; // 고유번호
    private String corpName; // 회사명
    private String se; // 구분(미등기임원)
    private Long nmpr; // 인원수
    private Long fyerSalaryTotamt; // 연간급여 총액
    private Long janSalaryAm; // 1인평균 급여액
    private String rm; // 비고
    private LocalDate stlmDt; // 결산기준일

    /**
     * Entity 객체를 Response DTO 객체로 변환하는 정적 팩토리 메소드
     * @param entity 변환할 엔티티 객체
     * @return 생성된 응답 DTO 객체
     */
    public static DartUnregisteredExecutiveCompensationResponse from(DartUnregisteredExecutiveCompensation entity) {
        return DartUnregisteredExecutiveCompensationResponse.builder()
                .rceptNo(entity.getRceptNo())
                .corpCls(entity.getCorpCls())
                .corpCode(entity.getCorpCode())
                .corpName(entity.getCorpName())
                .se(entity.getSe())
                .nmpr(entity.getNmpr())
                .fyerSalaryTotamt(entity.getFyerSalaryTotamt())
                .janSalaryAm(entity.getJanSalaryAm())
                .rm(entity.getRm())
                .stlmDt(entity.getStlmDt())
                .build();
    }
}