package com.example.finalproject.apitest.service.common;

import com.example.finalproject.apitest.dto.common.DartApiResponseDto;
import com.example.finalproject.apitest.exception.DartApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequiredArgsConstructor
public class DartApiCaller {

    private final RestClient client;

    @Value("${dart.api.key}")
    private String dartApiKey;

    /**
     * DART API를 호출하고 결과를 리스트 형태로 반환합니다.
     * @param uriCustomizer URI를 설정하는 Consumer
     * @param responseType 응답 DTO의 타입 참조
     * @return API 응답 데이터 리스트
     * @param <T> 응답 아이템의 타입
     */
    public <T> List<T> call(
            Consumer<UriBuilder> uriCustomizer,
            ParameterizedTypeReference<DartApiResponseDto<T>> responseType
    ) {
        DartApiResponseDto<T> responseDto = executeApiCall(uriCustomizer, responseType);

        List<T> items = responseDto.getList();
        if (items == null || items.isEmpty()) {
            log.info("DART API로부터 수신한 데이터가 없습니다.");
            return Collections.emptyList();
        }

        return items;
    }

    /**
     * DART API를 호출하고 결과를 단일 객체 형태로 반환합니다. (예: 기업개황)
     * @param uriCustomizer URI를 설정하는 Consumer
     * @param responseType 응답 DTO의 타입 참조
     * @return API 응답 데이터 객체
     * @param <T> 응답 아이템의 타입
     */
    public <T> T callSingle(
            Consumer<UriBuilder> uriCustomizer,
            ParameterizedTypeReference<DartApiResponseDto<T>> responseType
    ) {
        DartApiResponseDto<T> responseDto = executeApiCall(uriCustomizer, responseType);

        T item = responseDto.getSingleResult();
        if (item == null) {
            log.info("DART API로부터 수신한 단일 데이터가 없습니다.");
        }
        return item;
    }

    /**
     * 실제 DART API 호출 및 공통 예외 처리를 담당하는 private 메소드
     */
    private <T> DartApiResponseDto<T> executeApiCall(
            Consumer<UriBuilder> uriCustomizer,
            ParameterizedTypeReference<DartApiResponseDto<T>> responseType) {

        DartApiResponseDto<T> responseDto;
        try {
            responseDto = client.get()
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

        if (responseDto == null || !"000".equals(responseDto.getStatus())) {
            String status = (responseDto != null) ? responseDto.getStatus() : "null";
            String message = (responseDto != null) ? responseDto.getMessage() : "null response";
            log.error("DART API가 에러를 반환했습니다. Status: {}, Message: {}", status, message);
            throw new DartApiException("DART API 에러: status=" + status + ", message=" + message);
        }

        return responseDto;
    }
}