package com.css.gachimeokja.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = resolveToken(request); // HTTP 헤더에서 토큰 추출

        // [디버깅 1] 토큰이 들어오긴 했나?
        System.out.println("1. Filter - Token Check: " + (token != null ? "Exist" : "Null"));

        if (StringUtils.hasText(token)) {
            boolean isValid = jwtTokenProvider.validateToken(token);
            // [디버깅 2] 토큰이 유효한가?
            System.out.println("2. Filter - Token Valid?: " + isValid);

            if (isValid) {
                String userId = jwtTokenProvider.getUserId(token);
                System.out.println("3. Filter - UserId: " + userId);

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userId,
                        null,
                        Collections.emptyList()
                );
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);

                // [디버깅 4] 인증 객체 저장 완료
                System.out.println("4. Filter - Authentication Set Success");
            }
        }

        // 다음 필터로 요청을 넘김
        filterChain.doFilter(request, response);
    }

    // HTTP 헤더의 "Bearer " 접두사를 제거하고 토큰 추출
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}