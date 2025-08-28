package com.example.finalproject.dart.dto.dart;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DartDocumentDto {
    private String reportNm; // 보고서 이름
    private Integer rceptNo; // 보고서의 접수번호

}
