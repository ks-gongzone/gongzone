package com.gongzone.central.member.login.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtRequestFilter jwtRequestFilter;
	private final Http401UnauthorizedEntryPoint http401UnauthorizedEntryPoint;

	private static final String[] BASIC_LIST = {
			"/api/login",
			"/api/register",
			"/api/check/**",
			"/swagger-ui/**",
			"/v3/api-docs/**",
			"/swagger-ui.html",
			"/api/location",
			"/api/boards/",
			"/api/boards/**",
			"/api/naver/token",
			"/api/google/token",
			"/api/kakao/token",
			"/api/location",
			"/api/refresh",
			"/api/attachement/**"
	};

	// 보안 필터 체인을 정의
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(AbstractHttpConfigurer::disable) // 비활성화
				.authorizeHttpRequests(authorize -> authorize
						.requestMatchers(BASIC_LIST).permitAll()
						.anyRequest().authenticated()
				)
				.exceptionHandling(exception -> exception
						.authenticationEntryPoint(http401UnauthorizedEntryPoint) // 401 Unauthorized 처리
						.accessDeniedHandler(new AccessDeniedHandlerImpl()) // 403 Forbidden 처리
				)
				.sessionManagement(session -> session
						.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				)
				.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
    }

	// 인증 관리에 사용
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}
}
