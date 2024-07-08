package com.gongzone.central.member.login.security;

import com.gongzone.central.member.login.service.MemberDetails;
import com.gongzone.central.member.login.service.MemberDetailsService;
import com.gongzone.central.member.service.MemberServiceImpl;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    private final MemberDetailsService memberDetailsService;

    private final JwtUtil jwtUtil;

    private static final String[] EXCLUDED_PATHS = {
            "/api/login",
            "/api/register",
            "/api/check/**",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/api/location",
            "/api/naver/token",
            "/api/google/token",
            "/api/kakao/token",
            "/api/attachement"
    };
    private final MemberServiceImpl memberServiceImpl;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        System.out.println("가로채");
        System.out.println("Filtering request: " + request.getRequestURI());
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String requestURI = request.getRequestURI();

        System.out.println("request.getParameterNames() " + request.getParameterNames());
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            System.out.println("Parameter: " + paramName + " = " + request.getParameter(paramName));
        }

        // 특정 경로는 필터링하지 않음
        for (String path : EXCLUDED_PATHS) {
            if (requestURI.startsWith(path)) {
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
            } catch (ExpiredJwtException e) {
                //System.out.println("액세스토큰 만료");
                //request.setAttribute("exceptionRefreshToken", e);
                response.setHeader("token-expired", "true");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT Token has expired");
                return;
            } catch (Exception e) {
                System.out.println("JWT Token validation failed: " + e.getMessage());
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT Token is invalid or missing");
                return;
            }

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
                    logger.info("필터인증 완료");
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                } else {
                    // 추가된 부분: 토큰이 유효하지 않은 경우 처리
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT Token is invalid");
                    return;
                }
            }
        } else {
            logger.warn("JWT Token does not begin with Bearer String");
        }
        chain.doFilter(request, httpResponse);
    }
}
