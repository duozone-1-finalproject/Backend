package com.example.finalproject.apitest.dto.periodic.response;

// 사외이사 및 그 변동현황
import com.example.finalproject.apitest.entity.periodic.DartOutsideDirectorChangeStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class DartOutsideDirectorChangeStatusResponse {

    private String rceptNo; // 접수번호
    private String corpCls; // 법인구분
    private String corpCode; // 고유번호
    private String corpName; // 회사명
    private Long drctrCo; // 이사의 수
    private Long otcmpDrctrCo; // 사외이사 수
    private Long apnt; // 사외이사 변동현황(선임)
    private Long rlsofc; // 사외이사 변동현황(해임)
    private Long mdstrmResig; // 사외이사 변동현황(중도퇴임)
    private LocalDate stlmDt; // 결산기준일

    /**
     * Entity 객체를 Response DTO 객체로 변환하는 정적 팩토리 메소드
     */
    public static DartOutsideDirectorChangeStatusResponse from(DartOutsideDirectorChangeStatus entity) {
        return DartOutsideDirectorChangeStatusResponse.builder()
                .rceptNo(entity.getRceptNo())
                .corpCls(entity.getCorpCls())
                .corpCode(entity.getCorpCode())
                .corpName(entity.getCorpName())
                .drctrCo(entity.getDrctrCo())
                .otcmpDrctrCo(entity.getOtcmpDrctrCo())
                .apnt(entity.getApnt())
                .rlsofc(entity.getRlsofc())
                .mdstrmResig(entity.getMdstrmResig())
                .stlmDt(entity.getStlmDt())
                .build();
    }
}