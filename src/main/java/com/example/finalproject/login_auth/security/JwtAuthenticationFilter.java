package com.example.finalproject.login_auth.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private AntPathMatcher pathMatcher = new AntPathMatcher();

    // JWT 인증 필터를 건너뛸 경로 목록 (REST API 규칙 적용)
    private static final List<String> EXCLUDE_URLS = Arrays.asList(
            "/",
            "/login",
            "/users", // 사용자 리소스 생성 (회원가입)
            "/users/check", // 사용자명 중복 체크
            "/auth/sessions", // 로그인 세션 생성
            "/auth/login", // 기존 로그인 (하위 호환성)
            "/auth/oauth/tokens", // OAuth 토큰 획득
            "/tokens/refresh", // 토큰 갱신
            "/auth/**", // OAuth 관련 경로
            "/css/**",
            "/js/**",
            "/images/**",
            "/home",
            "/main",
            "/error",
            "/favicon.ico",
            "/api/companies",
            "/api/**",
            "/companies"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String token = getTokenFromHeader(request);

        log.info("Authorization Header: {}", request.getHeader("Authorization"));
        log.info("Extracted Token: {}", token);

        if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
            String username = jwtTokenProvider.getUsernameFromToken(token);

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(username, null, null);
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private String getTokenFromHeader(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return EXCLUDE_URLS.stream().anyMatch(exclude -> pathMatcher.match(exclude, path));
    }}