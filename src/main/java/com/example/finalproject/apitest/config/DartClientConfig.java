package com.example.finalproject.apitest.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.http.client.support.HttpRequestWrapper;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.Duration;

@Configuration
public class DartClientConfig {
    @Value("${dart.api.key}")
    private String dartApiKey;

    @Value("${dart.api.base-url}")
    private String baseUrl;



    @Bean
    public RestClient dartApiClient() {

        // <<< 1. 타임아웃 설정을 위한 RequestFactory 생성 (RestTemplate 예제와 동일)
        // JDK HttpClient에 연결 타임아웃 설정
        var jdkHttpClient = java.net.http.HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(30)) //  30초 연결 타임아웃
                .build();

        // 읽기 타임아웃은 RequestFactory에 설정
        var requestFactory = new JdkClientHttpRequestFactory(jdkHttpClient);
        requestFactory.setReadTimeout(Duration.ofSeconds(60)); //  60초 읽기 타임아웃

        // 2. 모든 요청에 API 키를 쿼리 파라미터로 자동 추가하는 인터셉터 생성
        ClientHttpRequestInterceptor apiKeyInterceptor = (request, body, execution) -> {
            URI newUri = UriComponentsBuilder.fromUri(request.getURI())
                    .queryParam("crtfc_key", dartApiKey) // 헤더가 아닌 쿼리 파라미터로 추가
                    .build(true)
                    .toUri();

            // URI가 변경된 새로운 요청(request) 객체를 생성
            HttpRequestWrapper modifiedRequest = new HttpRequestWrapper(request) {
                @Override
                public URI getURI() {
                    return newUri;
                }
            };
            // 변경된 요청으로 나머지 작업을 계속 진행
            return execution.execute(modifiedRequest, body);
        };

        return RestClient.builder()
                .requestFactory(requestFactory)
                .baseUrl(baseUrl) // API 서버의 기본 주소
                .requestInterceptor(apiKeyInterceptor)
                .build();
    }
}
