package com.example.finalproject.login_auth.controller;

import com.example.finalproject.login_auth.dto.LoginRequestDto;
import com.example.finalproject.login_auth.dto.LoginResponseDto;
import com.example.finalproject.login_auth.dto.UserRequestDto;
import com.example.finalproject.login_auth.entity.User;
import com.example.finalproject.login_auth.repository.UserRepository;
import com.example.finalproject.login_auth.security.JwtTokenProvider;
import com.example.finalproject.login_auth.service.CustomUserDetailsService;
import com.example.finalproject.login_auth.service.UserService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SecurityException;


@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final UserService userService;

    // 기존 로그인 기능 유지 (URI만 변경)
    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequest, HttpServletResponse response) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            String accessToken = jwtTokenProvider.generateToken(authentication.getName());
            String refreshToken = jwtTokenProvider.generateRefreshToken(authentication.getName());

            Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
            refreshCookie.setHttpOnly(true);
            refreshCookie.setPath("/");
            refreshCookie.setMaxAge(7 * 24 * 60 * 60);
            refreshCookie.setSecure(false);
            response.addCookie(refreshCookie);

            User user = userRepository.findByUsername(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("로그인 후 사용자를 찾을 수 없습니다."));

            return ResponseEntity.ok(new LoginResponseDto(
                    accessToken,
                    user.getUsername(),
                    user.getEmail(),
                    user.getName(),
                    user.getRole()
            ));

        } catch (Exception e) {
            log.warn("🚨 /auth/login - 로그인 실패: {}", e.getMessage(), e);
            return ResponseEntity.status(401).body("로그인 실패: " + e.getMessage());
        }
    }

    // 회원가입: POST /users (사용자 리소스 생성)
    @PostMapping("/users") // 변경: /auth/register -> /users
    public ResponseEntity<?> register(@RequestBody UserRequestDto requestDto) {
        log.info("🌐 /users 엔드포인트 호출됨.");
        try {
            userService.register(requestDto);
            log.info("✅ /users - 회원가입 성공: {}", requestDto.getUsername());
            return ResponseEntity.status(HttpStatus.CREATED).body("회원가입 성공");
        } catch (IllegalArgumentException e) {
            log.warn("🚨 /users - 회원가입 실패: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            log.error("❌ /users - 회원가입 중 예상치 못한 오류 발생: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("회원가입 실패: 서버 오류");
        }
    }

    // 소셜 로그인 성공 후 토큰 처리: GET /auth/oauth/tokens (OAuth를 통한 토큰 획득)
    @GetMapping("/auth/oauth/tokens") // 변경: /auth/oauth/success -> /auth/oauth/tokens
    // 함수명 변경: oauthSuccess -> oauthTokens
    public ResponseEntity<?> oauthTokens(HttpServletRequest request, HttpServletResponse response) {
        log.info("🌐 /auth/oauth/tokens 엔드포인트 진입 시도: 요청 URI = {}", request.getRequestURI());

        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length == 0) {
            log.warn("🚨 /auth/oauth/tokens - Refresh Token 없음: 요청에 쿠키가 전혀 포함되지 않았습니다.");
            return ResponseEntity.status(401).body("Refresh Token 없음");
        }

        String refreshToken = null;
        for (Cookie cookie : cookies) {
            if ("refreshToken".equals(cookie.getName())) {
                refreshToken = cookie.getValue();
            }
        }

        if (refreshToken == null) {
            log.warn("🚨 /auth/oauth/tokens - Refresh Token 없음: 'refreshToken' 이름의 쿠키를 찾을 수 없습니다.");
            return ResponseEntity.status(401).body("Refresh Token 없음");
        }

        try {
            if (!jwtTokenProvider.validateToken(refreshToken)) {
                log.warn("🚨 /auth/oauth/tokens - Refresh Token 유효성 검증 실패 ( validateToken() 이 false 반환).");
                Cookie expiredCookie = new Cookie("refreshToken", null);
                expiredCookie.setHttpOnly(true);
                expiredCookie.setPath("/");
                expiredCookie.setMaxAge(0);
                expiredCookie.setSecure(false);
                response.addCookie(expiredCookie);
                return ResponseEntity.status(401).body("Refresh Token 유효하지 않음");
            }
        } catch (ExpiredJwtException e) {
            log.warn("🚨 /auth/oauth/tokens - Refresh Token 만료: {}", e.getMessage());
            Cookie expiredCookie = new Cookie("refreshToken", null);
            expiredCookie.setHttpOnly(true);
            expiredCookie.setPath("/");
            expiredCookie.setMaxAge(0);
            expiredCookie.setSecure(false);
            response.addCookie(expiredCookie);
            return ResponseEntity.status(401).body("Refresh Token 만료");
        } catch (SecurityException | MalformedJwtException e) {
            log.warn("🚨 /auth/oauth/tokens - Refresh Token 위조 또는 유효하지 않은 서명: {}", e.getMessage());
            Cookie expiredCookie = new Cookie("refreshToken", null);
            expiredCookie.setHttpOnly(true);
            expiredCookie.setPath("/");
            expiredCookie.setMaxAge(0);
            expiredCookie.setSecure(false);
            response.addCookie(expiredCookie);
            return ResponseEntity.status(401).body("Refresh Token 위조 또는 유효하지 않음");
        } catch (UnsupportedJwtException | IllegalArgumentException e) {
            log.warn("🚨 /auth/oauth/tokens - Refresh Token 형식 오류 또는 기타 문제: {}", e.getMessage());
            return ResponseEntity.status(401).body("Refresh Token 형식 오류");
        } catch (Exception e) {
            log.error("❌ /auth/oauth/tokens - Refresh Token 검증 중 예상치 못한 오류 발생: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body("서버 오류: 토큰 검증 중 문제 발생");
        }

        String username = jwtTokenProvider.getUsernameFromToken(refreshToken);
        log.info("✅ /auth/oauth/tokens - Refresh Token 유효. 사용자: {}", username);
        String newAccessToken = jwtTokenProvider.generateToken(username);
        log.info("✅ /auth/oauth/tokens - Access Token 갱신 완료. 길이: {}", newAccessToken.length());

        String newRefreshToken = jwtTokenProvider.generateRefreshToken(username);
        Cookie refreshCookie = new Cookie("refreshToken", newRefreshToken);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(7 * 24 * 60 * 60);
        refreshCookie.setSecure(false);
        response.addCookie(refreshCookie);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.error("❌ /auth/oauth/tokens - DB에서 사용자를 찾을 수 없습니다: {}", username);
                    return new RuntimeException("OAuth 성공 후 사용자를 찾을 수 없습니다.");
                });

        return ResponseEntity.ok(new LoginResponseDto(
                newAccessToken,
                user.getUsername(),
                user.getEmail(),
                user.getName(),
                user.getRole()
        ));
    }

    // 토큰 갱신: POST /tokens/refresh (새로운 토큰을 생성하는 행위)
    @PostMapping("/tokens/refresh") // 변경: /auth/refresh -> /tokens/refresh
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        log.info("🌐 /tokens/refresh 엔드포인트 호출됨.");
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            log.warn("🚨 /tokens/refresh - Refresh Token 없음 (쿠키 존재하지 않음).");
            return ResponseEntity.status(401).body("Refresh Token 없음");
        }

        String refreshToken = null;
        for (Cookie cookie : cookies) {
            if ("refreshToken".equals(cookie.getName())) {
                refreshToken = cookie.getValue();
            }
        }

        if (refreshToken == null) {
            log.warn("🚨 /tokens/refresh - Refresh Token 없음 (쿠키에 토큰 값 없음).");
            return ResponseEntity.status(401).body("Refresh Token 없음");
        }

        try {
            if (!jwtTokenProvider.validateToken(refreshToken)) {
                log.warn("🚨 /tokens/refresh - Refresh Token 유효성 검증 실패 (validateToken()이 false 반환).");
                Cookie expiredCookie = new Cookie("refreshToken", null);
                expiredCookie.setHttpOnly(true);
                expiredCookie.setPath("/");
                expiredCookie.setMaxAge(0);
                expiredCookie.setSecure(false);
                response.addCookie(expiredCookie);
                return ResponseEntity.status(401).body("Refresh Token 유효하지 않음");
            }
        } catch (ExpiredJwtException e) {
            log.warn("🚨 /tokens/refresh - Refresh Token 만료: {}", e.getMessage());
            Cookie expiredCookie = new Cookie("refreshToken", null);
            expiredCookie.setHttpOnly(true);
            expiredCookie.setPath("/");
            expiredCookie.setMaxAge(0);
            expiredCookie.setSecure(false);
            response.addCookie(expiredCookie);
            return ResponseEntity.status(401).body("Refresh Token 만료");
        } catch (SecurityException | MalformedJwtException e) {
            log.warn("🚨 /tokens/refresh - Refresh Token 위조 또는 유효하지 않은 서명: {}", e.getMessage());
            Cookie expiredCookie = new Cookie("refreshToken", null);
            expiredCookie.setHttpOnly(true);
            expiredCookie.setPath("/");
            expiredCookie.setMaxAge(0);
            expiredCookie.setSecure(false);
            response.addCookie(expiredCookie);
            return ResponseEntity.status(401).body("Refresh Token 위조 또는 유효하지 않음");
        } catch (UnsupportedJwtException | IllegalArgumentException e) {
            log.warn("🚨 /tokens/refresh - Refresh Token 형식 오류 또는 기타 문제: {}", e.getMessage());
            return ResponseEntity.status(401).body("Refresh Token 형식 오류");
        } catch (Exception e) {
            log.error("❌ /tokens/refresh - Refresh Token 검증 중 예상치 못한 오류 발생: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body("서버 오류: 토큰 검증 중 문제 발생");
        }

        String username = jwtTokenProvider.getUsernameFromToken(refreshToken);
        String newAccessToken = jwtTokenProvider.generateToken(username);

        String newRefreshToken = jwtTokenProvider.generateRefreshToken(username);
        Cookie refreshCookie = new Cookie("refreshToken", newRefreshToken);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(7 * 24 * 60 * 60);
        refreshCookie.setSecure(false);
        response.addCookie(refreshCookie);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("리프레시 토큰으로 사용자를 찾을 수 없습니다."));

        return ResponseEntity.ok(new LoginResponseDto(
                newAccessToken,
                user.getUsername(),
                user.getEmail(),
                user.getName(),
                user.getRole()
        ));
    }

    // 인증 상태 확인: GET /auth/status (인증 상태를 리소스로 보고 상태를 확인)
    @GetMapping("/auth/status") // 변경: /auth/check-auth -> /auth/status
    public ResponseEntity<?> checkAuth(@RequestHeader("Authorization") String token) {
        if (jwtTokenProvider.validateToken(token.replace("Bearer ", ""))) {
            log.info("✅ /auth/status - 토큰 유효.");
            return ResponseEntity.ok("토큰 유효");
        } else {
            log.warn("🚨 /auth/status - 토큰 유효하지 않음.");
            return ResponseEntity.status(401).body("토큰 유효하지 않음");
        }
    }
}