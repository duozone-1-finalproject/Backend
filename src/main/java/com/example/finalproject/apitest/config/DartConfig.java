package com.example.finalproject.apitest.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class DartConfig {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        // JDK HttpClient에 연결 타임아웃 설정
        var jdkHttpClient = java.net.http.HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(30))
                .build();

        // 읽기 타임아웃은 RequestFactory에 설정
        var requestFactory = new JdkClientHttpRequestFactory(jdkHttpClient);
        requestFactory.setReadTimeout(Duration.ofSeconds(60));

        return builder
                .requestFactory(() -> requestFactory)
                .build();
    }
}
