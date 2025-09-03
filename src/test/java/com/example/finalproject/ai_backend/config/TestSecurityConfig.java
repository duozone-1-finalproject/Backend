package com.example.finalproject.ai_backend.config; // 패키지 경로는 프로젝트에 맞게 조정하세요.

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@TestConfiguration
public class TestSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 모든 요청을 인증 없이 허용하도록 설정
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                // 테스트에서는 CSRF와 CORS 검증이 필요 없으므로 비활성화
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable);
        return http.build();
    }
}