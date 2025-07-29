// src/main/java/com/example/test_02/handler/OAuthHandler.java
package com.example.test_02.handler;

import com.example.test_02.model.User;
import com.example.test_02.repository.UserRepository;
import com.example.test_02.security.JwtTokenProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import jakarta.servlet.http.Cookie;
import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuthHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        String provider = null;
        if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
            provider = oauthToken.getAuthorizedClientRegistrationId();
            log.info("🌐 OAuthHandler - 로그인 제공자: {}", provider);
        }

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String email = null;
        String name = null;

        if ("google".equals(provider)) {
            email = oAuth2User.getAttribute("email");
            name = oAuth2User.getAttribute("name");
        } else if ("naver".equals(provider)) {
            Map<String, Object> responseAttributes = oAuth2User.getAttribute("response");
            if (responseAttributes != null) {
                email = (String) responseAttributes.get("email");
                name = (String) responseAttributes.get("name");
            }
        } else if ("kakao".equals(provider)) {
            Map<String, Object> kakaoAccount = oAuth2User.getAttribute("kakao_account");
            if (kakaoAccount != null) {
                email = (String) kakaoAccount.get("email");
                Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
                if (profile != null) {
                    name = (String) profile.get("nickname");
                }
            }
        }

        if (email == null && oAuth2User.getAttribute("email") != null) {
            email = oAuth2User.getAttribute("email");
        }
        if (name == null && oAuth2User.getAttribute("name") != null) {
            name = oAuth2User.getAttribute("name");
        }

        log.info("🔑 OAuth 사용자 정보 (Provider: {}): Email={}, Name={}", provider, email, name);

        String finalProvider = provider != null ? provider : "unknown";
        String finalEmail = email;
        String finalName = name;

        User user = userRepository.findByEmailOrUsername(finalEmail, finalName).orElseGet(() -> {
            User newUser = User.builder()
                    .email(finalEmail)
                    .username(finalName)
                    .provider(finalProvider)
                    .role("USER")
                    .build();
            log.info("👤 신규 사용자 등록: Email={}, Username={}, Provider={}", finalEmail, finalName, finalProvider);
            return userRepository.save(newUser);
        });

        if (user.getId() != null) {
            boolean changed = false;
            if (finalEmail != null && !finalEmail.equals(user.getEmail())) {
                user.setEmail(finalEmail);
                changed = true;
            }
            if (finalName != null && !finalName.equals(user.getUsername())) {
                user.setUsername(finalName);
                changed = true;
            }
            if (!finalProvider.equals(user.getProvider())) {
                user.setProvider(finalProvider);
                changed = true;
            }
            if (changed) {
                userRepository.save(user);
                log.info("🔄 기존 사용자 정보 업데이트: Email={}, Username={}, Provider={}", user.getEmail(), user.getUsername(), user.getProvider());
            } else {
                log.info("✅ 처리된 사용자 (변동 없음): Email={}, Username={}, Name={}", user.getEmail(), user.getUsername(), user.getName());
            }
        } else {
            log.info("✅ 처리된 사용자: Email={}, Username={}, Name={}", user.getEmail(), user.getUsername(), user.getName());
        }

        // Access Token은 프론트에서 호출함.
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getUsername());

        Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(7 * 24 * 60 * 60);
        refreshCookie.setSecure(false);
        response.addCookie(refreshCookie);
        log.info("🍪 Refresh Token HttpOnly 쿠키 설정 완료.");

        response.sendRedirect("http://localhost:3000/oauth-success");
        log.info("🚀 프론트엔드 리다이렉트 (HttpOnly 쿠키 설정 후): http://localhost:3000/oauth-success");
    }
}