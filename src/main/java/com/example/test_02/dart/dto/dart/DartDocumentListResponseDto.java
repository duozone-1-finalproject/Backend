package com.example.test_02.dart.dto.dart;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DartDocumentListResponseDto {
    private List<DartDocumentDto> documents;

    public static DartDocumentListResponseDto from(List<DartDocumentDto> documentList) {
        return DartDocumentListResponseDto.builder()
                .documents(documentList)
                .build();
    }
}
