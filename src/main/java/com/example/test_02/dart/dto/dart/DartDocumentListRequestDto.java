package com.example.test_02.dart.dto.dart;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DartDocumentListRequestDto {
    private String reportNm; // 보고서 이름
    private String corpCode; // 기업코드
    private String bgnDe; //시작날짜
    private String endDe; //종료날짜
}
