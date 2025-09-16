package com.example.finalproject.login_auth.constant;

public class SecurityConstants {
    // 쿠키 관련
    public static final String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";
    public static final int REFRESH_TOKEN_EXPIRY_SECONDS = 7 * 24 * 60 * 60; // 7일
    public static final String COOKIE_PATH = "/";
    public static final boolean COOKIE_HTTP_ONLY = true;
    public static final boolean COOKIE_SECURE = false; // 로컬 개발용

    // 프론트엔드 URL (환경변수)
    public static final String FRONTEND_URL = System.getenv().getOrDefault("FRONTEND_URL", "http://k8s-default-frontend-770bd41637-61355deefa2c0980.elb.ap-northeast-2.amazonaws.com");
    public static final String OAUTH_SUCCESS_PATH = System.getenv().getOrDefault("OAUTH_SUCCESS_PATH", "/oauth-success");

    // 사용자 역할 및 제공자
    public static final String ROLE_USER = "USER";
    public static final String PROVIDER_GOOGLE = "google";
    public static final String PROVIDER_NAVER = "naver";
    public static final String PROVIDER_KAKAO = "kakao";
    public static final String PROVIDER_UNKNOWN = "unknown";

    // OAuth 속성 키
    public static final String OAUTH_ATTR_EMAIL = "email";
    public static final String OAUTH_ATTR_NAME = "name";
    public static final String OAUTH_ATTR_RESPONSE = "response";
    public static final String OAUTH_ATTR_KAKAO_ACCOUNT = "kakao_account";
    public static final String OAUTH_ATTR_PROFILE = "profile";
    public static final String OAUTH_ATTR_NICKNAME = "nickname";
}
