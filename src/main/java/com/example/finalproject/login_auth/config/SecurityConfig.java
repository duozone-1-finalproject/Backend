package com.example.finalproject.login_auth.config;

import com.example.finalproject.login_auth.handler.OAuthHandler;
import com.example.finalproject.login_auth.security.JwtAuthenticationFilter;
import com.example.finalproject.login_auth.security.JwtTokenProvider;
import com.example.finalproject.login_auth.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService customUserDetailsService;

    /**
     * ✅ 정적 리소스 + AI API 완전 제외
     */
    @Bean
    @Order(1)
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> {
            log.info("🔧 WebSecurityCustomizer 설정 - 정적 리소스 + AI API 제외");
            web.ignoring()
                    .requestMatchers(
                            "/css/**",
                            "/js/**",
                            "/images/**",
                            "/favicon.ico",
                            // ⭐ AI API 경로를 완전히 Security에서 제외
                            "/api/v1/ai-reports/**",
                            "/api/v1/**",
                            "/api/**"
                    );
        };
    }

    /**
     * ✅ 메인 Security 필터 체인
     */
    @Bean
    @Order(2)
    public SecurityFilterChain securityFilterChain(HttpSecurity http, OAuthHandler oAuthHandler) throws Exception {
        log.info("🔧 SecurityFilterChain 설정 시작");

        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                .authorizeHttpRequests(auth -> auth
                        // ⭐ OPTIONS 요청은 항상 허용
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/api/v1/kafka-test/**").permitAll()
                        .requestMatchers("/actuator/**").permitAll()
                        // ⭐ 공개 API
                        .requestMatchers(
                                "/",
                                "/login",
                                "/register",
                                "/auth/register",
                                "/auth/login",
                                "/auth/refresh",
                                "/home",
                                "/main",
                                "/api/companies",
                                "/companies",
                                "/error"
                        ).permitAll()

                        // ⭐ 인증 필요한 API
                        .requestMatchers("/auth/status").authenticated()

                        // ⭐ 나머지는 모두 인증 필요
                        .anyRequest().authenticated()
                )

                .oauth2Login(oauth -> oauth
                        .loginPage("/login")
                        .successHandler(oAuthHandler)
                )

                .formLogin(form -> form
                        .loginPage("/login")
                        .permitAll()
                )

                // ✅ JWT 필터 적용
                .addFilterBefore(
                        new JwtAuthenticationFilter(jwtTokenProvider),
                        UsernamePasswordAuthenticationFilter.class
                );

        log.info("🔧 SecurityFilterChain 설정 완료");
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * ✅ CORS 설정
     */
    @Value("${frontend.url}")
    private String frontendUrl; // 환경변수 주입

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        log.info("🔧 CORS 설정, 프론트엔드 URL: {}", frontendUrl);
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOriginPatterns(List.of(frontendUrl)); // ✅ 실제 URL 사용
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}