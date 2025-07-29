package com.example.test_02.dart.service.impl;



import com.example.dart.model.dto.dart.DartDocumentDto;
import com.example.dart.model.dto.dart.DartDocumentListRequestDto;
import com.example.dart.model.dto.dart.DartDocumentListResponseDto;
import com.example.dart.model.service.DartService;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;


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