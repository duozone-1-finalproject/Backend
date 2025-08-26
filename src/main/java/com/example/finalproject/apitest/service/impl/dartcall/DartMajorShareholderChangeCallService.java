package com.example.finalproject.apitest.service.impl.dartcall;

import com.example.finalproject.apitest.dto.periodic.response.DartMajorShareholderChangeResponse;

import java.io.IOException;
import java.util.List;

public interface DartMajorShareholderChangeCallService {
    List<DartMajorShareholderChangeResponse> DartMajorShareholderChangeCall(String corpCode, String reprtCode, String bsnsYear) throws IOException;
}
