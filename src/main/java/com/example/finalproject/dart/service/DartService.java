package com.example.finalproject.dart.service;

import com.example.finalproject.dart.dto.dart.DartDocumentListRequestDto;
import com.example.finalproject.dart.dto.dart.DartDocumentListResponseDto;

public interface DartService {
    DartDocumentListResponseDto getDocumentList(DartDocumentListRequestDto dto);
}