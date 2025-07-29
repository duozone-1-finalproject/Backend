// SecurityConfig.java
package com.example.test_02.config;

import com.example.test_02.handler.LocalLoginSuccessHandler;
import com.example.test_02.handler.OAuthHandler;
import com.example.test_02.security.JwtAuthenticationFilter;
import com.example.test_02.security.JwtTokenProvider;
import com.example.test_02.service.CustomUserDetailsService;
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
                // ✅ oauth2Login 설정을 authorizeHttpRequests보다 먼저 명시합니다.
                .oauth2Login(oauth -> oauth
                        .loginPage("/login") // OAuth2 로그인 시작 시 인증되지 않은 상태면 이 페이지로 리다이렉트
                        .successHandler(oAuthHandler)
                )
                .authorizeHttpRequests(auth -> auth
                        // ✅ "/oauth2/**" 패턴을 permitAll()에서 제거한 상태 유지.
                        // Spring Security의 oauth2Login()이 이 경로를 처리하도록 맡깁니다.
                        // 또한, OAuth2 콜백 경로도 permitAll()에서 제외하여 oauth2Login()이 처리하도록 합니다.
                        .requestMatchers("/", "/login", "/register", "/auth/**", "/auth/register","/auth/login", "/css/**", "/js/**", "/images/**", "/home", "/main").permitAll()
                        .requestMatchers("/auth/check-auth").authenticated()
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