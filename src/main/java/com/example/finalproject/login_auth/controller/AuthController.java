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
@CrossOrigin(origins = "*", allowCredentials = "true") // 추가적인 CORS 지원
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final TokenUtils tokenUtils;

    /**
     * 일반 로그인: POST /auth/login
     */
    @PostMapping(value = "/auth/login", produces = "application/json") // JSON 응답 명시
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequest, HttpServletResponse response) {
        log.info("=== 로그인 요청 시작 ===");
        log.info("Username: {}", loginRequest.getUsername());

        try {
            // 1. 사용자 인증
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            log.info("인증 성공: {}", authentication.getName());

            // 2. 토큰 생성 + 쿠키 설정 + 응답
            LoginResponseDto loginResponse = tokenUtils.generateTokensAndResponse(
                    authentication.getName(), response);

            log.info("토큰 생성 성공");
            log.info("Access Token 앞 20자: {}", loginResponse.getAccessToken().substring(0, Math.min(20, loginResponse.getAccessToken().length())));

            // 응답 헤더에 Content-Type 명시
            response.setContentType("application/json;charset=UTF-8");

            return ResponseEntity.ok(loginResponse);

        } catch (Exception e) {
            log.error("로그인 실패: {}", e.getMessage(), e);
            response.setContentType("application/json;charset=UTF-8");
            return ResponseEntity.status(401).body("{\"error\":\"로그인 실패\",\"message\":\"" + e.getMessage() + "\"}");
        }
    }

    /**
     * Access Token 갱신: POST /auth/refresh
     */
    @PostMapping(value = "/auth/refresh", produces = "application/json")
    public ResponseEntity<?> refreshAccessToken(HttpServletRequest request, HttpServletResponse response) {
        log.info("Access Token 갱신 요청");

        try {
            // 1. 쿠키에서 리프레시 토큰 추출
            String refreshToken = CookieUtils.getRefreshTokenFromCookies(request);

            if (refreshToken == null) {
                log.warn("Refresh Token 없음");
                response.setContentType("application/json;charset=UTF-8");
                return ResponseEntity.status(401).body("{\"error\":\"Refresh Token 없음\"}");
            }

            // 2. 새로운 Access Token 발급
            LoginResponseDto loginResponse = tokenUtils.refreshTokens(refreshToken, response);
            response.setContentType("application/json;charset=UTF-8");
            return ResponseEntity.ok(loginResponse);

        } catch (IllegalArgumentException e) {
            response.setContentType("application/json;charset=UTF-8");
            return ResponseEntity.status(401).body("{\"error\":\"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            log.error("토큰 갱신 중 예상치 못한 오류: {}", e.getMessage());
            response.setContentType("application/json;charset=UTF-8");
            return ResponseEntity.status(500).body("{\"error\":\"서버 오류\"}");
        }
    }
}