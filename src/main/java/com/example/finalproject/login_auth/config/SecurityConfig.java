package com.example.finalproject.login_auth.config;

import com.example.finalproject.login_auth.handler.OAuthHandler;
import com.example.finalproject.login_auth.security.JwtAuthenticationFilter;
import com.example.finalproject.login_auth.security.JwtTokenProvider;
import com.example.finalproject.login_auth.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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

import java.util.Arrays;
import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService customUserDetailsService;

    @Value("${frontend.url}")
    private String frontendUrl;

    @Value("${cors.allowed-origins:${frontend.url}}")
    private String[] allowedOrigins;

    /**
     * ✅ 정적 리소스 + AI API 완전 제외
     */
    @Bean
    @Order(1)
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> {
            log.info("🔧 WebSecurityCustomizer 설정 - 정적 리소스 + AI API만 제외");
            web.ignoring()
                    .requestMatchers(
                            "/css/**",
                            "/js/**",
                            "/images/**",
                            "/favicon.ico",
                            // ⭐ AI API만 제외하고 /api/versions는 JWT 인증이 필요하므로 제거
                            "/api/v1/ai-reports/**"
                            // "/api/v1/**", // 이것도 제거
                            // "/api/**"     // 이것도 제거
                    );
        };
    }

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
                                "/api/companies/**",
                                "/companies",
                                "/api/securities/**",
                                "/api/dart/**",              // ✅ 추가
                                "/api/v1/variables/**",      // ✅ 추가
                                "/api/ai/**",
                                "/initialTemplate/**",
                                "/error"
                        ).permitAll()

                        // ⭐ AI API는 여전히 공개 (또는 필요에 따라 인증 필요로 변경)
                        .requestMatchers("/api/v1/ai-reports/**").permitAll()

                        // ⭐ 버전 관리 API는 인증 필요
                        .requestMatchers("/api/versions/**").authenticated()

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
     * ✅ CORS 설정 - 환경변수 기반으로 동적 설정
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        log.info("🔧 CORS 설정 - Frontend URL: {}", frontendUrl);
        log.info("🔧 CORS 설정 - Allowed Origins: {}", Arrays.toString(allowedOrigins));

        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);

        // 환경변수로부터 동적으로 설정
        if (allowedOrigins.length > 0) {
            config.setAllowedOriginPatterns(Arrays.asList(allowedOrigins));
        } else {
            // fallback으로 frontend.url 사용
            config.setAllowedOriginPatterns(List.of(frontendUrl));
        }

        config.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS", "PATCH"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}