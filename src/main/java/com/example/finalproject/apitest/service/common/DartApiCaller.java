package com.example.finalproject.apitest.service.common;

import com.example.finalproject.apitest.dto.common.DartApiResponseDto;
import com.example.finalproject.apitest.exception.DartApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriBuilder;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

@Slf4j
@Component
public class DartApiCaller {

    private final RestClient client;

    @Value("${dart.api.key}")
    private String dartApiKey;

    public DartApiCaller(@Qualifier("dartApiClient") RestClient restClient) {
        this.client = restClient;
    }

    /**
     * DART API로부터 'list' 형태로 감싸인 표준 응답을 호출하고, 데이터 리스트를 반환합니다.
     */
    public <T> List<T> call(
            Consumer<UriBuilder> uriCustomizer,
            ParameterizedTypeReference<DartApiResponseDto<T>> responseType
    ) {
        // [수정] 표준 API 호출 및 검증을 담당하는 private 메소드 호출
        DartApiResponseDto<T> responseDto = executeStandardApiCall(uriCustomizer, responseType);

        List<T> items = responseDto.getList();
        if (items == null || items.isEmpty()) {
            log.info("DART API로부터 수신한 데이터가 없습니다.");
            return Collections.emptyList();
        }
        return items;
    }

    /**
     * DART API로부터 단일 객체 형태로 감싸인 표준 응답(예: 기업개황)을 호출하고, 해당 객체를 반환합니다.
     */
    public <T> T callSingle(
            Consumer<UriBuilder> uriCustomizer,
            ParameterizedTypeReference<DartApiResponseDto<T>> responseType
    ) {
        // [수정] 표준 API 호출 및 검증을 담당하는 private 메소드 호출
        DartApiResponseDto<T> responseDto = executeStandardApiCall(uriCustomizer, responseType);

        T item = responseDto.getSingleResult();
        if (item == null) {
            log.info("DART API로부터 수신한 단일 데이터가 없습니다.");
        }
        return item;
    }

    /**
     * DART API로부터 비표준 응답(예: 지분증권의 'group' 구조)을 호출하고, 응답 DTO를 그대로 반환합니다.
     */
    public <T> T callGrouped(
            Consumer<UriBuilder> uriCustomizer,
            ParameterizedTypeReference<T> responseType
    ) {
        // [수정] 이 메소드는 상태 코드 검증이 없으므로, 순수 API 호출만 담당하는 executeApiCall을 직접 호출
        return executeApiCall(uriCustomizer, responseType);
    }

    /**
     * [신규] 표준 DART API 응답(status, message 포함)을 처리하는 공통 메소드.
     * API를 호출하고 응답 상태 코드를 검증하여 유효한 DTO를 반환합니다.
     */
    private <T> DartApiResponseDto<T> executeStandardApiCall(
            Consumer<UriBuilder> uriCustomizer,
            ParameterizedTypeReference<DartApiResponseDto<T>> responseType) {

        DartApiResponseDto<T> responseDto = executeApiCall(uriCustomizer, responseType);

        if (responseDto == null || !"000".equals(responseDto.getStatus())) {
            handleApiError(responseDto);
        }
        return responseDto;
    }

    /**
     * [수정] 실제 HTTP 요청을 보내고 응답을 받는 순수 API 호출 메소드.
     * 이 메소드는 상태 코드 검증을 하지 않습니다.
     */
    private <T> T executeApiCall(
            Consumer<UriBuilder> uriCustomizer,
            ParameterizedTypeReference<T> responseType) {
        try {
            return client.get()
                    .uri(uriBuilder -> {
                        uriBuilder.queryParam("crtfc_key", dartApiKey);
                        uriCustomizer.accept(uriBuilder);
                        return uriBuilder.build();
                    })
                    .retrieve()
                    .body(responseType);
        } catch (Exception e) {
            log.error("DART API 호출 중 에러 발생", e);
            throw new DartApiException("DART API 호출 중 에러 발생", e);
        }
    }

    private <T> void handleApiError(DartApiResponseDto<T> responseDto) {
        String status = (responseDto != null) ? responseDto.getStatus() : "null";
        String message = (responseDto != null) ? responseDto.getMessage() : "null response";
        log.error("DART API가 에러를 반환했습니다. Status: {}, Message: {}", status, message);
        throw new DartApiException("DART API 에러: status=" + status + ", message=" + message);
    }
}

