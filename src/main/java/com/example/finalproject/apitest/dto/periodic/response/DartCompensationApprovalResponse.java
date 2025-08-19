package com.example.finalproject.apitest.dto.periodic.response;
// 이사·감사 전체의 보수현황(주주총회 승인금액)
import com.example.finalproject.apitest.entity.periodic.DartCompensationApproval;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class DartCompensationApprovalResponse {

    private String rceptNo; // 접수번호
    private String corpCls; // 법인구분
    private String corpCode; // 고유번호
    private String corpName; // 회사명
    private String se; // 구분
    private Long nmpr; // 인원수
    private Long gmtsckCnfrmAmount; // 주주총회 승인금액
    private String rm; // 비고
    private LocalDate stlmDt; // 결산기준일

    /**
     * Entity 객체를 Response DTO 객체로 변환하는 정적 팩토리 메소드
     * @param entity 변환할 엔티티 객체
     * @return 생성된 응답 DTO 객체
     */
    public static DartCompensationApprovalResponse from(DartCompensationApproval entity) {
        return DartCompensationApprovalResponse.builder()
                .rceptNo(entity.getRceptNo())
                .corpCls(entity.getCorpCls())
                .corpCode(entity.getCorpCode())
                .corpName(entity.getCorpName())
                .se(entity.getSe())
                .nmpr(entity.getNmpr())
                .gmtsckCnfrmAmount(entity.getGmtsckCnfrmAmount())
                .rm(entity.getRm())
                .stlmDt(entity.getStlmDt())
                .build();
    }
}
