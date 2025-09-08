package com.example.finalproject.dart_viewer.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Slf4j
@Configuration
public class FastApiConfig {

    // application.properties에서 FastAPI 서버의 기본 URL을 주입받습니다.
    @Value("${spring.fast.uris}")
    private String fastApiBaseUrl;

    /**
     * FastAPI 서버와 통신하기 위한 RestClient Bean을 생성합니다.
     * @return 설정이 완료된 RestClient 인스턴스
     */
    @Bean(name = "fastApiClient")
    public RestClient fastApiClient() {
        log.info("Creating RestClient bean for FastAPI server at: {}", fastApiBaseUrl);

        // 타임아웃 설정을 위한 Request Factory 생성
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(10000); // 연결 타임아웃 10초 (밀리초 단위)
        requestFactory.setReadTimeout(60000);    // 읽기 타임아웃 60초 (밀리초 단위)

        return RestClient.builder()
                // 주입받은 URL을 기본 URL로 설정
                .baseUrl(fastApiBaseUrl)
                // 기본 헤더 설정 (필요시)
                .defaultHeader("Accept", "application/json")
                // 생성한 Request Factory를 빌더에 설정
                .requestFactory(requestFactory)
                .build();
    }
}