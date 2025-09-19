package com.example.finalproject.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class CorsConfig {

    @Value("${cors.allowed-origins}")
    private String allowedOrigins;

    @Bean
    public CorsFilter corsFilter() {
        log.info("🔧 CorsFilter 설정, 허용된 오리진: {}", allowedOrigins);

        CorsConfiguration config = new CorsConfiguration();

        // 구체적인 프론트엔드 URL 설정 (와일드카드 * 대신)
        config.setAllowedOrigins(List.of(allowedOrigins));

        // allowCredentials true 설정
        config.setAllowCredentials(true);

        // 허용 HTTP 메서드
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // 허용 헤더
        config.setAllowedHeaders(List.of("*"));

        // 노출 헤더 설정
        config.setExposedHeaders(List.of("Authorization", "Content-Type"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}
