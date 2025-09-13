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



    @Bean(name = "dartApiClient")
    public RestClient dartApiClient() {

        // 1. 타임아웃 설정을 위한 RequestFactory 생성
        var jdkHttpClient = java.net.http.HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(30)) // 30초 연결 타임아웃
                .build();

        var requestFactory = new JdkClientHttpRequestFactory(jdkHttpClient);
        requestFactory.setReadTimeout(Duration.ofSeconds(60)); // 60초 읽기 타임아웃

        // 2. User-Agent 헤더 설정 (API 서버 차단을 피하기 위해 중요)
        String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36";

        return RestClient.builder()
                .requestFactory(requestFactory)
                .baseUrl(baseUrl)
                .defaultHeader("User-Agent", userAgent) // 모든 요청에 User-Agent 헤더 추가
                .build();
    }
}
