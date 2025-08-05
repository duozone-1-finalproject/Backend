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

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final UserService userService;

    // 로그인 세션 생성: POST /auth/sessions (세션을 리소스로 보고 생성)
    @PostMapping("/auth/sessions")
    public ResponseEntity<?> createSession(@RequestBody LoginRequestDto loginRequest, HttpServletResponse response) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            String accessToken = jwtTokenProvider.generateToken(authentication.getName());
            String refreshToken = jwtTokenProvider.generateRefreshToken(authentication.getName());

            // 리프레시 토큰을 DB에 저장
            User user = userRepository.findByUsername(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("로그인 후 사용자를 찾을 수 없습니다."));

            user.setRefreshToken(refreshToken);
            user.setRefreshTokenExpiryDate(LocalDateTime.now().plusDays(7));
            userRepository.save(user);

            Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
            refreshCookie.setHttpOnly(true);
            refreshCookie.setPath("/");
            refreshCookie.setMaxAge(7 * 24 * 60 * 60);
            refreshCookie.setSecure(false);
            response.addCookie(refreshCookie);

            return ResponseEntity.ok(new LoginResponseDto(
                    accessToken,
                    user.getUsername(),
                    user.getEmail(),
                    user.getName(),
                    user.getRole()
            ));

        } catch (Exception e) {
            log.warn("🚨 /auth/sessions - 로그인 실패: {}", e.getMessage(), e);
            return ResponseEntity.status(401).body("로그인 실패: " + e.getMessage());
        }
    }

    // 기존 로그인 기능 유지 (하위 호환성)
    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequest, HttpServletResponse response) {
        return createSession(loginRequest, response);
    }

    // 사용자 리소스 생성: POST /users
    @PostMapping("/users")
    public ResponseEntity<?> createUser(@RequestBody UserRequestDto requestDto) {
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

    // OAuth 토큰 획득: GET /auth/oauth/tokens
    @GetMapping("/auth/oauth/tokens")
    public ResponseEntity<?> getOAuthTokens(HttpServletRequest request, HttpServletResponse response) {
        log.info("🌐 /auth/oauth/tokens 엔드포인트 진입 시도: 요청 URI = {}", request.getRequestURI());

        String refreshToken = extractRefreshTokenFromCookies(request);
        if (refreshToken == null) {
            log.warn("🚨 /auth/oauth/tokens - Refresh Token 없음");
            return ResponseEntity.status(401).body("Refresh Token 없음");
        }

        try {
            // DB에서 리프레시 토큰 검증
            User user = validateRefreshTokenFromDB(refreshToken);
            if (user == null) {
                clearRefreshTokenCookie(response);
                return ResponseEntity.status(401).body("유효하지 않은 Refresh Token");
            }

            // 새로운 토큰 발급
            String newAccessToken = jwtTokenProvider.generateToken(user.getUsername());
            String newRefreshToken = jwtTokenProvider.generateRefreshToken(user.getUsername());

            // DB에 새 리프레시 토큰 저장
            user.setRefreshToken(newRefreshToken);
            user.setRefreshTokenExpiryDate(LocalDateTime.now().plusDays(7));
            userRepository.save(user);

            setRefreshTokenCookie(response, newRefreshToken);

            return ResponseEntity.ok(new LoginResponseDto(
                    newAccessToken,
                    user.getUsername(),
                    user.getEmail(),
                    user.getName(),
                    user.getRole()
            ));

        } catch (Exception e) {
            log.error("❌ /auth/oauth/tokens - 토큰 처리 중 오류: {}", e.getMessage(), e);
            clearRefreshTokenCookie(response);
            return ResponseEntity.status(401).body("토큰 처리 실패");
        }
    }

    // 토큰 갱신: POST /tokens/refresh
    @PostMapping("/tokens/refresh")
    public ResponseEntity<?> refreshTokens(HttpServletRequest request, HttpServletResponse response) {
        log.info("🌐 /tokens/refresh 엔드포인트 호출됨.");

        String refreshToken = extractRefreshTokenFromCookies(request);
        if (refreshToken == null) {
            log.warn("🚨 /tokens/refresh - Refresh Token 없음");
            return ResponseEntity.status(401).body("Refresh Token 없음");
        }

        try {
            // DB에서 리프레시 토큰 검증
            User user = validateRefreshTokenFromDB(refreshToken);
            if (user == null) {
                clearRefreshTokenCookie(response);
                return ResponseEntity.status(401).body("유효하지 않은 Refresh Token");
            }

            // 새로운 토큰 발급
            String newAccessToken = jwtTokenProvider.generateToken(user.getUsername());
            String newRefreshToken = jwtTokenProvider.generateRefreshToken(user.getUsername());

            // DB에 새 리프레시 토큰 저장
            user.setRefreshToken(newRefreshToken);
            user.setRefreshTokenExpiryDate(LocalDateTime.now().plusDays(7));
            userRepository.save(user);

            setRefreshTokenCookie(response, newRefreshToken);

            return ResponseEntity.ok(new LoginResponseDto(
                    newAccessToken,
                    user.getUsername(),
                    user.getEmail(),
                    user.getName(),
                    user.getRole()
            ));

        } catch (Exception e) {
            log.error("❌ /tokens/refresh - 토큰 갱신 중 오류: {}", e.getMessage(), e);
            clearRefreshTokenCookie(response);
            return ResponseEntity.status(401).body("토큰 갱신 실패");
        }
    }

    // 인증 상태 확인: GET /auth/status
    @GetMapping("/auth/status")
    public ResponseEntity<?> getAuthStatus(@RequestHeader("Authorization") String token) {
        if (jwtTokenProvider.validateToken(token.replace("Bearer ", ""))) {
            log.info("✅ /auth/status - 토큰 유효.");
            return ResponseEntity.ok("토큰 유효");
        } else {
            log.warn("🚨 /auth/status - 토큰 유효하지 않음.");
            return ResponseEntity.status(401).body("토큰 유효하지 않음");
        }
    }

    // 로그아웃: DELETE /auth/sessions (세션 삭제)
    @DeleteMapping("/auth/sessions")
    public ResponseEntity<?> deleteSession(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = extractRefreshTokenFromCookies(request);

        if (refreshToken != null) {
            // DB에서 리프레시 토큰 제거
            try {
                User user = validateRefreshTokenFromDB(refreshToken);
                if (user != null) {
                    user.setRefreshToken(null);
                    user.setRefreshTokenExpiryDate(null);
                    userRepository.save(user);
                }
            } catch (Exception e) {
                log.warn("로그아웃 중 DB 처리 오류: {}", e.getMessage());
            }
        }

        // 쿠키 삭제
        clearRefreshTokenCookie(response);

        return ResponseEntity.ok("로그아웃 성공");
    }

    // 헬퍼 메서드들
    private String extractRefreshTokenFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;

        for (Cookie cookie : cookies) {
            if ("refreshToken".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    private User validateRefreshTokenFromDB(String refreshToken) {
        try {
            // JWT 토큰 자체의 유효성 검증
            if (!jwtTokenProvider.validateToken(refreshToken)) {
                return null;
            }

            // DB에서 토큰 조회 및 만료 시간 확인
            User user = userRepository.findByRefreshToken(refreshToken).orElse(null);
            if (user == null) {
                return null;
            }

            // 헬퍼 메서드를 사용하여 String -> LocalDateTime 변환 후 비교
            LocalDateTime expiryDate = user.getRefreshTokenExpiryDateAsLocalDateTime();
            if (expiryDate == null || expiryDate.isBefore(LocalDateTime.now())) {
                return null;
            }

            return user;
        } catch (Exception e) {
            log.warn("리프레시 토큰 검증 실패: {}", e.getMessage());
            return null;
        }
    }

    private void setRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(7 * 24 * 60 * 60);
        refreshCookie.setSecure(false);
        response.addCookie(refreshCookie);
    }

    private void clearRefreshTokenCookie(HttpServletResponse response) {
        Cookie expiredCookie = new Cookie("refreshToken", null);
        expiredCookie.setHttpOnly(true);
        expiredCookie.setPath("/");
        expiredCookie.setMaxAge(0);
        expiredCookie.setSecure(false);
        response.addCookie(expiredCookie);
    }
}