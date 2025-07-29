package com.example.test_02.dart.service;

import com.example.dart.model.dto.dart.DartDocumentListRequestDto;
import com.example.dart.model.dto.dart.DartDocumentListResponseDto;

public interface DartService {
    DartDocumentListResponseDto getDocumentList(DartDocumentListRequestDto dto);
}