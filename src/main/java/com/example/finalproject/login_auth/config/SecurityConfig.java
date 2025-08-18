// src/main/java/com/example/finalproject/login_auth/config/SecurityConfig.java

package com.example.finalproject.login_auth.config;

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
    // LocalLoginSuccessHandler 필드 제거

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
                        // RESTful 규칙에 맞춰 변경된 경로들
                        .requestMatchers(
                                "/",
                                "/login",
                                "/users", // 회원가입: POST /users
                                "/auth/login", // 기존 로그인
                                "/auth/refresh", // 통합된 토큰 갱신 엔드포인트
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/home",
                                "/main",
                                "/api/companies",
                                "/api/**"
                        ).permitAll()
                        .requestMatchers("/auth/status").authenticated() // 인증 상태 확인
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form // 이 부분은 OAuth2 로그인을 시작하는 페이지를 위해 남겨둘 수 있습니다.
                                .loginPage("/login")
                                .permitAll()
                        // .successHandler(...) 설정 제거
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