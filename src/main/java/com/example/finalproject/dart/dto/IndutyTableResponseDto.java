package com.example.finalproject.dart.dto;


import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IndutyTableResponseDto {
    private String indutyCode; // 산업코드
    private String indutyName; // 산업명
}
