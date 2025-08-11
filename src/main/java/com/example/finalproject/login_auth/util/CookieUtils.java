// ========== 2. CookieUtils.java ==========
package com.example.finalproject.login_auth.util;

import com.example.finalproject.login_auth.constant.SecurityConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class CookieUtils {

    /**
     * 리프레시 토큰 쿠키 설정 (AuthController에서 반복되는 코드를 함수로 분리)
     */
    public static void setRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        Cookie refreshCookie = new Cookie(SecurityConstants.REFRESH_TOKEN_COOKIE_NAME, refreshToken);
        refreshCookie.setHttpOnly(SecurityConstants.COOKIE_HTTP_ONLY);
        refreshCookie.setPath(SecurityConstants.COOKIE_PATH);
        refreshCookie.setMaxAge(SecurityConstants.REFRESH_TOKEN_EXPIRY_SECONDS);
        refreshCookie.setSecure(SecurityConstants.COOKIE_SECURE);
        response.addCookie(refreshCookie);
        log.info("🍪 Refresh Token 쿠키 설정 완료");
    }

    /**
     * 쿠키에서 리프레시 토큰 추출 (AuthController에서 반복되는 코드를 함수로 분리)
     */
    public static String getRefreshTokenFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }

        for (Cookie cookie : cookies) {
            if (SecurityConstants.REFRESH_TOKEN_COOKIE_NAME.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    /**
     * 리프레시 토큰 쿠키 삭제 (로그아웃이나 토큰 만료 시 사용)
     */
    public static void clearRefreshTokenCookie(HttpServletResponse response) {
        Cookie expiredCookie = new Cookie(SecurityConstants.REFRESH_TOKEN_COOKIE_NAME, null);
        expiredCookie.setHttpOnly(SecurityConstants.COOKIE_HTTP_ONLY);
        expiredCookie.setPath(SecurityConstants.COOKIE_PATH);
        expiredCookie.setMaxAge(0); // 즉시 만료
        expiredCookie.setSecure(SecurityConstants.COOKIE_SECURE);
        response.addCookie(expiredCookie);
        log.info("🗑️ Refresh Token 쿠키 삭제 완료");
    }
}