package com.example.finalproject.login_auth.config;

import com.example.finalproject.login_auth.handler.LocalLoginSuccessHandler;
import com.example.finalproject.login_auth.handler.OAuthHandler;
import com.example.finalproject.login_auth.security.JwtAuthenticationFilter;
import com.example.finalproject.login_auth.security.JwtTokenProvider;
import com.example.finalproject.login_auth.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService customUserDetailsService;
    private final LocalLoginSuccessHandler localLoginSuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, OAuthHandler oAuthHandler) throws Exception {
        http
                .cors(withDefaults())
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2Login(oauth -> oauth
                        .loginPage("/login")
                        .successHandler(oAuthHandler)
                )
                .authorizeHttpRequests(auth -> auth
                        // REST API 규칙에 맞춰 업데이트된 경로들
                        .requestMatchers(
                                "/",
                                "/login",
                                "/users", // 사용자 리소스 생성 (회원가입)
                                "/users/check", // 사용자명 중복 체크
                                "/auth/sessions", // 로그인 세션 생성
                                "/auth/login", // 기존 로그인 (하위 호환성)
                                "/auth/oauth/tokens", // OAuth 토큰 획득
                                "/tokens/refresh", // 토큰 갱신
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/home",
                                "/main",
                                "/api/companies",
                                "/api/**"
                        ).permitAll()
                        .requestMatchers("/auth/status").authenticated() // 인증 상태 확인
                        .requestMatchers("/users/me").authenticated() // 현재 사용자 정보 조회
                        .requestMatchers("/auth/sessions").authenticated() // 로그아웃 (DELETE)
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .successHandler(localLoginSuccessHandler)
                        .permitAll()
                )
                .addFilterBefore(
                        new JwtAuthenticationFilter(jwtTokenProvider),
                        UsernamePasswordAuthenticationFilter.class
                );

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

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:3000")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}