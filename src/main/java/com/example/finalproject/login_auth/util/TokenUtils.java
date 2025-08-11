// ========== 3. TokenUtils.java ==========
package com.example.finalproject.login_auth.util;

import com.example.finalproject.login_auth.constant.SecurityConstants;
import com.example.finalproject.login_auth.dto.LoginResponseDto;
import com.example.finalproject.login_auth.entity.User;
import com.example.finalproject.login_auth.security.JwtTokenProvider;
import com.example.finalproject.login_auth.repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletResponse;

@Component
@RequiredArgsConstructor
@Slf4j
public class TokenUtils {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    /**
     * 토큰 쌍 생성 + 쿠키 설정을 한 번에 처리
     * (AuthController의 login, oauthTokens, refreshToken에서 반복되는 로직을 통합)
     */
    public LoginResponseDto generateTokensAndResponse(String username, HttpServletResponse response) {
        // 1. 토큰 생성
        String accessToken = jwtTokenProvider.generateToken(username);
        String refreshToken = jwtTokenProvider.generateRefreshToken(username);

        // 2. 리프레시 토큰을 쿠키에 설정
        CookieUtils.setRefreshTokenCookie(response, refreshToken);

        // 3. 사용자 정보 조회
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + username));

        // 4. 응답 DTO 생성
        return new LoginResponseDto(
                accessToken,
                user.getUsername(),
                user.getEmail(),
                user.getName(),
                user.getRole()
        );
    }

    /**
     * 리프레시 토큰 검증 + 새 토큰 발급
     * (AuthController의 oauthTokens, refreshToken에서 반복되는 로직을 통합)
     */
    public LoginResponseDto refreshTokens(String refreshToken, HttpServletResponse response) {
        // 1. 토큰 유효성 검증
        validateRefreshToken(refreshToken, response);

        // 2. 사용자명 추출
        String username = jwtTokenProvider.getUsernameFromToken(refreshToken);
        log.info("✅ 토큰 갱신 요청: 사용자 = {}", username);

        // 3. 새 토큰 쌍 생성 + 응답
        return generateTokensAndResponse(username, response);
    }

    /**
     * 리프레시 토큰 유효성 검증 (내부 함수)
     * 검증 실패 시 쿠키도 함께 삭제
     */
    private void validateRefreshToken(String refreshToken, HttpServletResponse response) {
        try {
            if (!jwtTokenProvider.validateToken(refreshToken)) {
                log.warn("🚨 Refresh Token 유효성 검증 실패");
                CookieUtils.clearRefreshTokenCookie(response);
                throw new IllegalArgumentException("Refresh Token 유효하지 않음");
            }
        } catch (ExpiredJwtException e) {
            log.warn("🚨 Refresh Token 만료: {}", e.getMessage());
            CookieUtils.clearRefreshTokenCookie(response);
            throw new IllegalArgumentException("Refresh Token 만료");
        } catch (SecurityException | MalformedJwtException e) {
            log.warn("🚨 Refresh Token 위조 또는 유효하지 않은 서명: {}", e.getMessage());
            CookieUtils.clearRefreshTokenCookie(response);
            throw new IllegalArgumentException("Refresh Token 위조 또는 유효하지 않음");
        } catch (UnsupportedJwtException | IllegalArgumentException e) {
            log.warn("🚨 Refresh Token 형식 오류: {}", e.getMessage());
            throw new IllegalArgumentException("Refresh Token 형식 오류");
        }
    }
}