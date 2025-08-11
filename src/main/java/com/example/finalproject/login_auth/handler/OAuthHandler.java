// ========== 6. OAuthHandler.java ==========
package com.example.finalproject.login_auth.handler;

import com.example.finalproject.login_auth.constant.SecurityConstants;
import com.example.finalproject.login_auth.dto.UserInfo;
import com.example.finalproject.login_auth.entity.User;
import com.example.finalproject.login_auth.security.JwtTokenProvider;
import com.example.finalproject.login_auth.repository.UserRepository;
import com.example.finalproject.login_auth.util.CookieUtils;
import com.example.finalproject.login_auth.util.OAuth2Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

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

        // 1. OAuth 제공자 정보 추출
        String provider = extractProvider(authentication);
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        // 2. 사용자 정보 추출 (함수로 분리)
        UserInfo userInfo = OAuth2Utils.extractUserInfo(provider, oAuth2User);

        // 3. 사용자 등록 또는 조회
        User user = findOrCreateUser(userInfo);

        // 4. 리프레시 토큰만 쿠키에 설정 (함수로 분리)
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getUsername());
        CookieUtils.setRefreshTokenCookie(response, refreshToken);

        // 5. 프론트엔드 리다이렉트 (함수로 분리)
        OAuth2Utils.redirectToFrontend(response);
    }

    private String extractProvider(Authentication authentication) {
        if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
            return oauthToken.getAuthorizedClientRegistrationId();
        }
        return SecurityConstants.PROVIDER_UNKNOWN;
    }

    private User findOrCreateUser(UserInfo userInfo) {
        return userRepository.findByEmailOrUsername(userInfo.getEmail(), userInfo.getName())
                .map(user -> updateUserIfNeeded(user, userInfo))
                .orElseGet(() -> createNewUser(userInfo));
    }

    private User updateUserIfNeeded(User user, UserInfo userInfo) {
        boolean changed = false;
        if (userInfo.getEmail() != null && !userInfo.getEmail().equals(user.getEmail())) {
            user.setEmail(userInfo.getEmail());
            changed = true;
        }
        if (userInfo.getName() != null && !userInfo.getName().equals(user.getUsername())) {
            user.setUsername(userInfo.getName());
            changed = true;
        }
        if (!userInfo.getProvider().equals(user.getProvider())) {
            user.setProvider(userInfo.getProvider());
            changed = true;
        }

        if (changed) {
            log.info("🔄 사용자 정보 업데이트: {}", userInfo.getEmail());
            return userRepository.save(user);
        }
        return user;
    }

    private User createNewUser(UserInfo userInfo) {
        User newUser = User.builder()
                .email(userInfo.getEmail())
                .username(userInfo.getName())
                .provider(userInfo.getProvider())
                .role(SecurityConstants.ROLE_USER)
                .build();
        log.info("👤 신규 사용자 등록: {}", userInfo.getEmail());
        return userRepository.save(newUser);
    }
}