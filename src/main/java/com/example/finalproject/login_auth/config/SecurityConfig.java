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
import org.springframework.http.HttpStatus;
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

    @Bean
    @Order(1)
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> {
            log.info("🔧 WebSecurityCustomizer 설정 - 정적 리소스만 제외");
            web.ignoring()
                    .requestMatchers(
                            "/css/**",
                            "/js/**",
                            "/images/**",
                            "/favicon.ico"
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
                        // OPTIONS 요청 허용
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // 공개 API - 인증 없이 접근 가능
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/refresh").permitAll()
                        .requestMatchers(HttpMethod.POST, "/users").permitAll() // 회원가입
                        .requestMatchers("/users/check").permitAll() // 중복 확인

                        // 기본 경로들
                        .requestMatchers("/", "/login", "/register", "/home", "/main", "/error").permitAll()

                        // API 경로들
                        .requestMatchers("/api/companies/**").permitAll()
                        .requestMatchers("/api/securities/**").permitAll()
                        .requestMatchers("/api/dart/**").permitAll()
                        .requestMatchers("/api/v1/variables/**").permitAll()
                        .requestMatchers("/api/ai/**").permitAll()
                        .requestMatchers("/api/v1/ai-reports/**").permitAll()
                        .requestMatchers("/api/v1/kafka-test/**").permitAll()
                        .requestMatchers("/initialTemplate/**").permitAll()
                        .requestMatchers("/actuator/**").permitAll()
                        .requestMatchers("/health").permitAll()

                        // 인증 필요한 API
                        .requestMatchers("/users/me").authenticated()
                        .requestMatchers("/api/versions/**").authenticated()
                        .requestMatchers("/auth/status").authenticated()

                        // 나머지는 모두 인증 필요
                        .anyRequest().authenticated()
                )

                // OAuth 로그인만 활성화
                .oauth2Login(oauth -> oauth
                        .loginPage("/login")
                        .successHandler(oAuthHandler)
                )

                // formLogin 완전 비활성화 - 이게 핵심!
                .formLogin(AbstractHttpConfigurer::disable)

                // 인증 실패 시 JSON 응답
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setContentType("application/json;charset=UTF-8");
                            response.setStatus(HttpStatus.UNAUTHORIZED.value());
                            response.getWriter().write("{\"error\":\"Unauthorized\",\"message\":\"" + authException.getMessage() + "\"}");
                        })
                )

                // JWT 필터 적용
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

    // Kubernetes 환경변수명에 맞춰 수정
    @Value("${FRONTEND_URL}")
    private String frontendUrl;

    @Value("${CORS_ALLOWED_ORIGIN}")  // 단수형 주의!
    private String allowedOrigins;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        log.info("🔧 CORS 설정, 프론트엔드 URL: {}, 허용된 오리진: {}", frontendUrl, allowedOrigins);
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(List.of(allowedOrigins)); // 와일드카드 대신 구체적인 URL 사용
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("Authorization", "Content-Type"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}