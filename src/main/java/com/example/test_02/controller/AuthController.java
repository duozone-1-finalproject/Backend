package com.example.test_02.controller;

import com.example.test_02.dto.LoginRequestDto;
import com.example.test_02.dto.LoginResponseDto;
import com.example.test_02.dto.UserRequestDto; // UserRequestDto 임포트 추가
import com.example.test_02.model.User;
import com.example.test_02.repository.UserRepository;
import com.example.test_02.security.JwtTokenProvider;
import com.example.test_02.service.CustomUserDetailsService;
import com.example.test_02.service.UserService; // UserService 임포트 추가

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus; // HttpStatus 임포트 추가
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
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final UserService userService; // ⭐ UserService 주입 추가

    @PostMapping("/login")
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
            refreshCookie.setSecure(false); // 개발 환경에서 false, HTTPS 배포 시 true
            response.addCookie(refreshCookie);
            //log.info("✅ /auth/login - Refresh Token 쿠키 설정 완료 for user: {}", authentication.getName());


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
            log.warn("🚨 /auth/login - 로그인 실패: {}", e.getMessage(), e); // 스택 트레이스도 함께 출력
            return ResponseEntity.status(401).body("로그인 실패: " + e.getMessage());
        }
    }

    // ⭐⭐ 새로 추가된 회원가입 엔드포인트 ⭐⭐
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRequestDto requestDto) {
        log.info("🌐 /auth/register 엔드포인트 호출됨.");
        try {
            userService.register(requestDto); // UserService의 register 메서드 호출
            log.info("✅ /auth/register - 회원가입 성공: {}", requestDto.getUsername());
            return ResponseEntity.status(HttpStatus.CREATED).body("회원가입 성공");
        } catch (IllegalArgumentException e) {
            log.warn("🚨 /auth/register - 회원가입 실패: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            log.error("❌ /auth/register - 회원가입 중 예상치 못한 오류 발생: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("회원가입 실패: 서버 오류");
        }
    }

    @GetMapping("/oauth/success")
    public ResponseEntity<?> oauthSuccess(HttpServletRequest request, HttpServletResponse response) {
        log.info("🌐 /auth/oauth/success 엔드포인트 진입 시도: 요청 URI = {}", request.getRequestURI());

        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length == 0) {
            log.warn("🚨 /auth/oauth/success - Refresh Token 없음: 요청에 쿠키가 전혀 포함되지 않았습니다.");
            return ResponseEntity.status(401).body("Refresh Token 없음");
        }

        String refreshToken = null;
        for (Cookie cookie : cookies) {
            if ("refreshToken".equals(cookie.getName())) {
                refreshToken = cookie.getValue();
            }
        }

        if (refreshToken == null) {
            log.warn("🚨 /auth/oauth/success - Refresh Token 없음: 'refreshToken' 이름의 쿠키를 찾을 수 없습니다.");
            return ResponseEntity.status(401).body("Refresh Token 없음");
        }

        try {
            if (!jwtTokenProvider.validateToken(refreshToken)) {
                log.warn("🚨 /auth/oauth/success - Refresh Token 유효성 검증 실패 ( validateToken() 이 false 반환).");
                Cookie expiredCookie = new Cookie("refreshToken", null);
                expiredCookie.setHttpOnly(true);
                expiredCookie.setPath("/");
                expiredCookie.setMaxAge(0);
                expiredCookie.setSecure(false);
                response.addCookie(expiredCookie);
                return ResponseEntity.status(401).body("Refresh Token 유효하지 않음");
            }
        } catch (ExpiredJwtException e) {
            log.warn("🚨 /auth/oauth/success - Refresh Token 만료: {}", e.getMessage());
            Cookie expiredCookie = new Cookie("refreshToken", null);
            expiredCookie.setHttpOnly(true);
            expiredCookie.setPath("/");
            expiredCookie.setMaxAge(0);
            expiredCookie.setSecure(false);
            response.addCookie(expiredCookie);
            return ResponseEntity.status(401).body("Refresh Token 만료");
        } catch (SecurityException | MalformedJwtException e) {
            log.warn("🚨 /auth/oauth/success - Refresh Token 위조 또는 유효하지 않은 서명: {}", e.getMessage());
            Cookie expiredCookie = new Cookie("refreshToken", null);
            expiredCookie.setHttpOnly(true);
            expiredCookie.setPath("/");
            expiredCookie.setMaxAge(0);
            expiredCookie.setSecure(false);
            response.addCookie(expiredCookie);
            return ResponseEntity.status(401).body("Refresh Token 위조 또는 유효하지 않음");
        } catch (UnsupportedJwtException | IllegalArgumentException e) {
            log.warn("🚨 /auth/oauth/success - Refresh Token 형식 오류 또는 기타 문제: {}", e.getMessage());
            return ResponseEntity.status(401).body("Refresh Token 형식 오류");
        } catch (Exception e) {
            log.error("❌ /auth/oauth/success - Refresh Token 검증 중 예상치 못한 오류 발생: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body("서버 오류: 토큰 검증 중 문제 발생");
        }


        String username = jwtTokenProvider.getUsernameFromToken(refreshToken);
        log.info("✅ /auth/oauth/success - Refresh Token 유효. 사용자: {}", username);
        String newAccessToken = jwtTokenProvider.generateToken(username);
        log.info("✅ /auth/oauth/success - Access Token 갱신 완료. 길이: {}", newAccessToken.length());

        String newRefreshToken = jwtTokenProvider.generateRefreshToken(username);
        Cookie refreshCookie = new Cookie("refreshToken", newRefreshToken);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(7 * 24 * 60 * 60);
        refreshCookie.setSecure(false);
        response.addCookie(refreshCookie);


        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.error("❌ /auth/oauth/success - DB에서 사용자를 찾을 수 없습니다: {}", username);
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

    @GetMapping("/refresh")
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        log.info("🌐 /auth/refresh 엔드포인트 호출됨.");
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            log.warn("🚨 /auth/refresh - Refresh Token 없음 (쿠키 존재하지 않음).");
            return ResponseEntity.status(401).body("Refresh Token 없음");
        }

        String refreshToken = null;
        for (Cookie cookie : cookies) {
            if ("refreshToken".equals(cookie.getName())) {
                refreshToken = cookie.getValue();
            }
        }

        if (refreshToken == null) {
            log.warn("🚨 /auth/refresh - Refresh Token 없음 (쿠키에 토큰 값 없음).");
            return ResponseEntity.status(401).body("Refresh Token 없음");
        }

        try {
            if (!jwtTokenProvider.validateToken(refreshToken)) {
                log.warn("🚨 /auth/refresh - Refresh Token 유효성 검증 실패 (validateToken()이 false 반환).");
                Cookie expiredCookie = new Cookie("refreshToken", null);
                expiredCookie.setHttpOnly(true);
                expiredCookie.setPath("/");
                expiredCookie.setMaxAge(0);
                expiredCookie.setSecure(false);
                response.addCookie(expiredCookie);
                return ResponseEntity.status(401).body("Refresh Token 유효하지 않음");
            }
        } catch (ExpiredJwtException e) {
            log.warn("🚨 /auth/refresh - Refresh Token 만료: {}", e.getMessage());
            Cookie expiredCookie = new Cookie("refreshToken", null);
            expiredCookie.setHttpOnly(true);
            expiredCookie.setPath("/");
            expiredCookie.setMaxAge(0);
            expiredCookie.setSecure(false);
            response.addCookie(expiredCookie);
            return ResponseEntity.status(401).body("Refresh Token 만료");
        } catch (SecurityException | MalformedJwtException e) {
            log.warn("🚨 /auth/refresh - Refresh Token 위조 또는 유효하지 않은 서명: {}", e.getMessage());
            Cookie expiredCookie = new Cookie("refreshToken", null);
            expiredCookie.setHttpOnly(true);
            expiredCookie.setPath("/");
            expiredCookie.setMaxAge(0);
            expiredCookie.setSecure(false);
            response.addCookie(expiredCookie);
            return ResponseEntity.status(401).body("Refresh Token 위조 또는 유효하지 않음");
        } catch (UnsupportedJwtException | IllegalArgumentException e) {
            log.warn("🚨 /auth/refresh - Refresh Token 형식 오류 또는 기타 문제: {}", e.getMessage());
            return ResponseEntity.status(401).body("Refresh Token 형식 오류");
        } catch (Exception e) {
            log.error("❌ /auth/refresh - Refresh Token 검증 중 예상치 못한 오류 발생: {}", e.getMessage(), e);
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


    @GetMapping("/check-auth")
    public ResponseEntity<?> checkAuth(@RequestHeader("Authorization") String token) {
        if (jwtTokenProvider.validateToken(token.replace("Bearer ", ""))) {
            log.info("✅ /auth/check-auth - 토큰 유효.");
            return ResponseEntity.ok("토큰 유효");
        } else {
            log.warn("🚨 /auth/check-auth - 토큰 유효하지 않음.");
            return ResponseEntity.status(401).body("토큰 유효하지 않음");
        }
    }
}