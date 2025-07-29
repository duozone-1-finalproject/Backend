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
import org.springframework.util.AntPathMatcher; // AntPathMatcher 임포트 추가
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays; // Arrays 임포트 추가
import java.util.List; // List 임포트 추가
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
            "/favicon.ico",    // 파비콘 요청 등
            "/api/companies",
            "/api/**",
            "/companies"
    );


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String token = getTokenFromHeader(request); // <-- 여기에 브레이크포인트 1

        // 여기에 request.getHeader("Authorization") 값과 추출된 token 값을 로그로 찍어보면 좋습니다.
        log.info("Authorization Header: {}", request.getHeader("Authorization"));
        log.info("Extracted Token: {}", token);

        if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) { // <-- 여기에 브레이크포인트 2
            String username = jwtTokenProvider.getUsernameFromToken(token); // <-- 여기에 브레이크포인트 3

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(username, null, null);
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication); // <-- 여기에 브레이크포인트 4 (이곳까지 도달해야 성공)
        }

        filterChain.doFilter(request, response);
    }

    private String getTokenFromHeader(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization"); // <-- 여기에 브레이크포인트 5
        // log.info("Raw Authorization Header in getTokenFromHeader: {}", bearer);
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