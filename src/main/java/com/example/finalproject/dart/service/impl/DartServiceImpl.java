package com.example.finalproject.dart.service.impl;



import com.example.finalproject.dart.dto.dart.DartDocumentDto;
import com.example.finalproject.dart.dto.dart.DartDocumentListRequestDto;
import com.example.finalproject.dart.dto.dart.DartDocumentListResponseDto;
import com.example.finalproject.dart.service.DartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class DartServiceImpl implements DartService {

    @Override
    public DartDocumentListResponseDto getDocumentList(DartDocumentListRequestDto dto) {



        return DartDocumentListResponseDto.builder()
                .documents(List.of(DartDocumentDto.builder()
                        .reportNm("테스트1")
                        .rceptNo(100)
                        .build())
                )
                .build();
    }
}