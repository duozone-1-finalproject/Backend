// JwtAuthenticationFilter.java - 제외 경로 수정
package com.example.finalproject.login_auth.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    // JWT 필터를 건너뛸 경로 목록 - SecurityConfig와 일치시키기
    private static final List<String> EXCLUDE_URLS = Arrays.asList(
            "/",
            "/login",
            "/register",
            "/auth/register",
            "/auth/login",
            "/auth/refresh",
            "/auth/**",
            "/css/**",
            "/js/**",
            "/images/**",
            "/home",
            "/main",
            "/error",
            "/favicon.ico",
            "/health",
            "/actuator/**",
            "/api/v1/kafka-test/**",
            // 🚨 회사 검색 API - SecurityConfig와 동일하게 설정
            "/api/companies",
            "/api/companies/**",
            "/companies",
            "/companies/**",
            // 기타 공개 API
            "/api/securities/**",
            "/api/dart/**",
            "/api/v1/variables/**",
            "/api/ai/**",
            "/initialTemplate/**"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        String method = request.getMethod();

        // OPTIONS 요청은 항상 통과
        if ("OPTIONS".equals(method)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = getTokenFromHeader(request);

            if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
                String username = jwtTokenProvider.getUsernameFromToken(token);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                username,
                                null,
                                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
                        );

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);

                logger.debug("JWT 인증 성공: " + username + " for path: " + path);
            } else if (StringUtils.hasText(token)) {
                logger.warn("유효하지 않은 JWT 토큰 for path: " + path);
            }
        } catch (Exception e) {
            logger.error("JWT 인증 처리 중 오류 발생 for path: " + path, e);
            SecurityContextHolder.clearContext();
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
        String method = request.getMethod();

        // OPTIONS 요청은 항상 필터 제외
        if ("OPTIONS".equals(method)) {
            logger.debug("OPTIONS 요청 필터 제외: " + path);
            return true;
        }

        boolean shouldExclude = EXCLUDE_URLS.stream()
                .anyMatch(exclude -> pathMatcher.match(exclude, path));

        if (shouldExclude) {
            logger.debug("JWT 필터 제외 경로: " + path);
        } else {
            logger.debug("JWT 필터 적용 경로: " + path);
        }

        return shouldExclude;
    }
}