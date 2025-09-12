package com.example.finalproject.login_auth.util;

import com.example.finalproject.login_auth.constant.SecurityConstants;
import com.example.finalproject.login_auth.dto.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

import static com.example.finalproject.login_auth.constant.SecurityConstants.OAUTH_ATTR_PROFILE;

@Component
@Slf4j
public class OAuth2Utils {

    private final SecurityConstants securityConstants;

    public OAuth2Utils(SecurityConstants securityConstants) {
        this.securityConstants = securityConstants;
    }

    /**
     * OAuth2 성공 후 프론트엔드 리다이렉트
     * (OAuthHandler에서 사용)
     */
    public void redirectToFrontend(HttpServletResponse response) throws IOException {
        String redirectUrl = securityConstants.FRONTEND_URL + SecurityConstants.OAUTH_SUCCESS_PATH;
        log.info("🚀 프론트엔드 리다이렉트: {}", redirectUrl);
        response.sendRedirect(redirectUrl);
    }

    /**
     * OAuth2 제공자별 사용자 정보 추출
     * (OAuthHandler에서 반복되는 로직을 함수로 분리)
     */
    public static UserInfo extractUserInfo(String provider, OAuth2User oAuth2User) {
        String email = null;
        String name = null;

        if (SecurityConstants.PROVIDER_GOOGLE.equals(provider)) {
            email = oAuth2User.getAttribute(SecurityConstants.OAUTH_ATTR_EMAIL);
            name = oAuth2User.getAttribute(SecurityConstants.OAUTH_ATTR_NAME);
        } else if (SecurityConstants.PROVIDER_NAVER.equals(provider)) {
            Map<String, Object> responseAttributes = oAuth2User.getAttribute(SecurityConstants.OAUTH_ATTR_RESPONSE);
            if (responseAttributes != null) {
                email = (String) responseAttributes.get(SecurityConstants.OAUTH_ATTR_EMAIL);
                name = (String) responseAttributes.get(SecurityConstants.OAUTH_ATTR_NAME);
            }
        } else if (SecurityConstants.PROVIDER_KAKAO.equals(provider)) {
            Map<String, Object> kakaoAccount = oAuth2User.getAttribute(SecurityConstants.OAUTH_ATTR_KAKAO_ACCOUNT);
            if (kakaoAccount != null) {
                email = (String) kakaoAccount.get(SecurityConstants.OAUTH_ATTR_EMAIL);
                Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get(OAUTH_ATTR_PROFILE);
                if (profile != null) {
                    name = (String) profile.get(SecurityConstants.OAUTH_ATTR_NICKNAME);
                }
            }
        }

        // fallback: 기본 속성에서 추출 시도
        if (email == null) email = oAuth2User.getAttribute(SecurityConstants.OAUTH_ATTR_EMAIL);
        if (name == null) name = oAuth2User.getAttribute(SecurityConstants.OAUTH_ATTR_NAME);

        log.info("🔑 OAuth 사용자 정보 추출 ({}): Email={}, Name={}", provider, email, name);
        return new UserInfo(email, name, provider);
    }
}
