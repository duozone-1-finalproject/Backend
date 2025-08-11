// src/main/java/com/example/finalproject/login_auth/controller/AuthController.java

package com.example.finalproject.login_auth.controller;

import com.example.finalproject.login_auth.dto.LoginRequestDto;
import com.example.finalproject.login_auth.dto.LoginResponseDto;
import com.example.finalproject.login_auth.util.CookieUtils;
import com.example.finalproject.login_auth.util.TokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final TokenUtils tokenUtils;

    /**
     * 일반 로그인: POST /auth/login
     */
    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequest, HttpServletResponse response) {
        try {
            // 1. 사용자 인증
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            // 2. 토큰 생성 + 쿠키 설정 + 응답
            LoginResponseDto loginResponse = tokenUtils.generateTokensAndResponse(
                    authentication.getName(), response);

            return ResponseEntity.ok(loginResponse);

        } catch (Exception e) {
            log.warn("🚨 로그인 실패: {}", e.getMessage());
            return ResponseEntity.status(401).body("로그인 실패: " + e.getMessage());
        }
    }

    /**
     * Access Token 갱신: POST /auth/refresh
     * - OAuth2 로그인 직후, 또는 Access Token 만료 시 호출되는 통합 엔드포인트
     */
    @PostMapping("/auth/refresh")
    public ResponseEntity<?> refreshAccessToken(HttpServletRequest request, HttpServletResponse response) {
        log.info("🌐 Access Token 갱신 요청");

        try {
            // 1. 쿠키에서 리프레시 토큰 추출
            String refreshToken = CookieUtils.getRefreshTokenFromCookies(request);

            if (refreshToken == null) {
                log.warn("🚨 Refresh Token 없음");
                return ResponseEntity.status(401).body("Refresh Token 없음");
            }

            // 2. 새로운 Access Token 발급
            LoginResponseDto loginResponse = tokenUtils.refreshTokens(refreshToken, response);
            return ResponseEntity.ok(loginResponse);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        } catch (Exception e) {
            log.error("❌ 토큰 갱신 중 예상치 못한 오류: {}", e.getMessage());
            return ResponseEntity.status(500).body("서버 오류");
        }
    }
}