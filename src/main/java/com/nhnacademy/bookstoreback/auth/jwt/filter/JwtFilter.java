package com.nhnacademy.bookstoreback.auth.jwt.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.nhnacademy.bookstoreback.auth.jwt.dto.AppCustomUserDetails;
import com.nhnacademy.bookstoreback.auth.jwt.utils.JwtUtils;
import com.nhnacademy.bookstoreback.user.domain.entity.Role;
import com.nhnacademy.bookstoreback.user.domain.entity.User;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
	private final JwtUtils jwtUtils;

	@Override
	protected void doFilterInternal(
		@NonNull HttpServletRequest request,
		@NonNull HttpServletResponse response,
		@NonNull FilterChain filterChain
	) throws ServletException, IOException {
		String accessToken = request.getHeader("Authorization");

		// 토큰이 없어도 일부 허용되는 기능이 있을 수 있음
		if (Objects.isNull(accessToken)) {
			filterChain.doFilter(request, response);
			return;
		}

		// 토큰 유효성 검증(만료, 변조, 빈 토큰 등)
		String accessTokenErrorMessage = jwtUtils.validateToken(accessToken);
		if (Objects.nonNull(accessTokenErrorMessage)) {
			PrintWriter writer = response.getWriter();
			writer.print(accessTokenErrorMessage);
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}

		// 토큰 타입이 access token인지 확인
		String tokenType = jwtUtils.getTokenTypeFromToken(accessToken);
		if (!"access".equals(tokenType)) {
			PrintWriter writer = response.getWriter();
			writer.print("Access token이 아닙니다.");
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}

		// TODO: IP 주소까지 확인할 것인가?

		String userEmail = jwtUtils.getEmailFromToken(accessToken);
		Role role = jwtUtils.getRoleFromToken(accessToken);

		User user = User.builder()
			.email(userEmail)
			.role(role)
			.build();
		AppCustomUserDetails userDetails = new AppCustomUserDetails(user);
		Authentication authentication = new UsernamePasswordAuthenticationToken(
			userDetails, null, userDetails.getAuthorities()
		);
		SecurityContextHolder.getContext().setAuthentication(authentication);

		filterChain.doFilter(request, response);
	}
}
