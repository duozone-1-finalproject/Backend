package com.example.test_02.login_auth.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.util.AntPathMatcher; // AntPathMatcher 임포트 추가
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays; // Arrays 임포트 추가
import java.util.List; // List 임포트 추가


@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private AntPathMatcher pathMatcher = new AntPathMatcher(); // AntPathMatcher 인스턴스 생성

    // ⭐ JWT 인증 필터를 건너뛸 경로 목록 (SecurityConfig의 permitAll() 경로와 일치해야 합니다)
    private static final List<String> EXCLUDE_URLS = Arrays.asList(
            "/",
            "/login",
            "/register",
            "/auth/register", // 회원가입 API 경로
            "/auth/login",    // 로그인 API 경로
            "/auth/**",       // /auth/로 시작하는 모든 경로 (필요하다면)
            "/css/**",
            "/js/**",
            "/images/**",
            "/home",
            "/main",
            "/error",         // 에러 페이지
            "/favicon.ico"    // 파비콘 요청 등
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String token = getTokenFromHeader(request);

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

    // ⭐⭐ 이 메서드를 오버라이드하여 특정 경로에서는 필터가 작동하지 않도록 합니다.
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        // EXCLUDE_URLS 목록에 있는 경로 중 하나라도 현재 요청 경로와 일치하면 필터링을 건너뜁니다.
        return EXCLUDE_URLS.stream().anyMatch(exclude -> pathMatcher.match(exclude, path));
    }
}