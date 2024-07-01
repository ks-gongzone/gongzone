package com.gongzone.central.member.login.security;

import com.gongzone.central.member.domain.Member;
import com.gongzone.central.member.login.service.MemberDetails;
import com.gongzone.central.member.login.service.MemberDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    private final MemberDetailsService memberDetailsService;

    private final JwtUtil jwtUtil;

    private static final String[] EXCLUDED_PATHS = {
            "/api/login",
            "/api/register",
            "/api/check/**",
            "/api/party/**",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/api/location",
            "/api/naver/token",
            "/api/naver/**",
            "/api/google/token",
            "/api/google/**",
            "/api/kakao/token",
            "/api/kakao/**",
            "/api/location",
            "/api/check",
            "/api/party/**",
            "/api/boards",
    };

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        System.out.println("가로채");
        System.out.println("Filtering request: " + request.getRequestURI());
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String requestURI = request.getRequestURI();

        // 특정 경로는 필터링하지 않음
        for (String path : EXCLUDED_PATHS) {
            if (requestURI.startsWith(path)) {
                //String requestBody = new String(request.getInputStream().readAllBytes());
                //request.setAttribute("requestBody", requestBody);
                if (requestURI.startsWith("/api/naver/token") || requestURI.startsWith("/api/kakao/token") || requestURI.startsWith("/api/google/token")) {
                    System.out.println("필터 들어옴" + requestURI);
                    String requestBody = new String(request.getInputStream().readAllBytes());
                    httpRequest.setAttribute("requestBody", requestBody);
                }
                System.out.println("Skipping filter for path: " + path);
                chain.doFilter(httpRequest, httpResponse);
                return;
            }
        }

        final String requestTokenHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        // 토큰 추출후 검증
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwt = requestTokenHeader.substring(7);
            try {
                username = jwtUtil.extractMemberNo(jwt);

                // 받은 후 유효성 검사
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    MemberDetails memberDetails = (MemberDetails) this.memberDetailsService.loadUserByUsername(username);
                    // 토큰이 유효한지 검사
                    if (jwtUtil.validateToken(jwt, memberDetails)) {
                        memberDetails.setToken(jwt);
                        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                                new UsernamePasswordAuthenticationToken(memberDetails, null, memberDetails.getAuthorities());

                        usernamePasswordAuthenticationToken
                                .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                    }
                }
           } catch (IllegalArgumentException e) {
                System.out.println("토큰이 없습니다");
            }
        } else {
            logger.warn("JWT Token does not begin with Bearer String");
        }
        chain.doFilter(request, httpResponse);
    }

    /*@Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return "/api/login".equals(request.getRequestURI());
    }*/
}
