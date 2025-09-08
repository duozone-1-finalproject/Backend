package com.example.finalproject.ai_backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * FastAPI에서 받는 주식 공모 주석 응답 DTO
 * equity_request.py의 EquityAnnotationResponse와 동일한 구조
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EquityAnnotationResponseDto {

    @JsonProperty("S4_NOTE1_1")
    private String S4_NOTE1_1; // note_1

    @JsonProperty("S4_NOTE1_2")
    private String S4_NOTE1_2; // note_2

    @JsonProperty("S4_NOTE1_3")
    private String S4_NOTE1_3; // note_3

    @JsonProperty("S4_NOTE1_4")
    private String S4_NOTE1_4; // note_4

    @JsonProperty("S4_NOTE1_5")
    private String S4_NOTE1_5; // note_5

    // 추가 정보 (선택사항)
    private String processing_status;
    private String company_name;
    private Long processing_time_ms;
    private String timestamp;

    // 기본값 설정을 위한 메소드
    public static EquityAnnotationResponseDto createDefault(String companyName) {
        return EquityAnnotationResponseDto.builder()
                .S4_NOTE1_1("모집(매출) 예정가액과 관련된 내용은 「제1부 모집 또는 매출에 관한 사항」- 「Ⅳ. 인수인의 의견(분석기관의 의견)」의 「4. 공모가격에 대한 의견」부분을 참조하시기 바랍니다.")
                .S4_NOTE1_2("모집(매출)가액, 모집(매출)이액, 인수금액 및 인수대가는 발행회사와 대표주관회사가 협의하여 제시하는 공모희망가액 기준입니다.")
                .S4_NOTE1_3("모집(매출)가액의 확정은 청약일 전에 실시하는 수요예측 결과를 반영하여 대표주관회사와 발행회사가 협의하여 최종 결정할 예정입니다.")
                .S4_NOTE1_4("증권의 발행 및 공시 등에 관한 규정에 따라 정정신고서 상의 공모주식수는 증권신고서의 공모할 주식수의 80% 이상 120% 이하로 변경가능합니다.")
                .S4_NOTE1_5("투자 위험 등 자세한 내용은 투자설명서를 참조하시기 바라며, 투자결정시 신중하게 검토하시기 바랍니다.")
                .company_name(companyName)
                .processing_status("COMPLETED")
                .build();
    }
}