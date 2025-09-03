package com.example.finalproject.ai_backend.controller;

import com.example.finalproject.ai_backend.dto.AiResponseDto;
import com.example.finalproject.ai_backend.dto.ReportGenerationRequestDto;
import com.example.finalproject.ai_backend.service.OpenSearchService;
import com.example.finalproject.ai_backend.service.ReportGenerationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.client.RestTemplateAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AiReportController.class)
@Import(RestTemplateAutoConfiguration.class) // 👈 [추가] RestTemplateBuilder를 제공하는 설정을 포함합니다.
class AiReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReportGenerationService reportGenerationService;

    @MockBean
    private OpenSearchService openSearchService;

    @Test
    @WithMockUser
    @DisplayName("헬스체크 API 호출 시 정상 응답(200 OK)을 반환한다")
    void healthCheck_ShouldReturnOk() throws Exception {
        mockMvc.perform(get("/api/v1/ai-reports/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("SUCCESS"))
                .andExpect(jsonPath("$.status").value("200"))
                .andExpect(jsonPath("$.data").value("OK"));
    }

    @Test
    @WithMockUser
    @DisplayName("보고서 생성 요청 시 비동기 처리 후 정상 응답(200 OK)을 반환한다")
    void generateReport_ShouldReturnSuccess() throws Exception {
        // given
        ReportGenerationRequestDto request = ReportGenerationRequestDto.builder()
                .corpCode("123456")
                .reportType("증권신고서")
                .build();

        AiResponseDto mockResponse = AiResponseDto.builder()
                .requestId("TEST_ID_123")
                .status("SUCCESS")
                .build();

        given(reportGenerationService.generateReport(any(String.class), any(String.class)))
                .willReturn(CompletableFuture.completedFuture(mockResponse));

        // when & then
        mockMvc.perform(post("/api/v1/ai-reports/generate")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.requestId").value("TEST_ID_123"));
    }
}
