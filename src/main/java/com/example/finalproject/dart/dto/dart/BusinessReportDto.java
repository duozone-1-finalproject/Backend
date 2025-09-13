package com.example.finalproject.dart.dto.dart;

import lombok.*;

/**
 * 최신 사업보고서의 접수번호와 HTML 본문을 함께 담는 DTO
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusinessReportDto {
    private String recepNo;
    private String htmlContent;
}

