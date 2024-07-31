package com.gongzone.central.member.login.security;

import com.gongzone.central.member.login.service.LoginLogService;
import com.gongzone.central.member.login.service.MemberDetails;
import com.gongzone.central.member.login.service.MemberDetailsService;
import com.gongzone.central.member.service.MemberServiceImpl;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    private final MemberDetailsService memberDetailsService;

    private final JwtUtil jwtUtil;
    private final LoginLogService loginLogService;

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
            "/api/attachement",
            "/api/alertSSE/**"
    };
    private final MemberServiceImpl memberServiceImpl;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String requestURI = request.getRequestURI();

        for (String path : EXCLUDED_PATHS) {
            if (requestURI.startsWith(path)) {
                if (requestURI.startsWith("/api/naver/token") || requestURI.startsWith("/api/kakao/token") || requestURI.startsWith("/api/google/token")) {
                    String requestBody = new String(request.getInputStream().readAllBytes());
                    httpRequest.setAttribute("requestBody", requestBody);
                }
                chain.doFilter(httpRequest, httpResponse);
                return;
            }
        }

        final String requestTokenHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwt = requestTokenHeader.substring(7);
            try {
                username = jwtUtil.extractMemberNo(jwt);
            } catch (ExpiredJwtException e) {
                response.setHeader("token-expired", "true");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT Token has expired");
                return;
            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT Token is invalid or missing");
                return;
            }

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                MemberDetails memberDetails = (MemberDetails) this.memberDetailsService.loadUserByUsername(username);
                if (jwtUtil.validateToken(jwt, memberDetails)) {
                    memberDetails.setToken(jwt);
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(memberDetails, null, memberDetails.getAuthorities());

                    usernamePasswordAuthenticationToken
                            .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    logger.info("필터인증 완료");
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                } else {
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
