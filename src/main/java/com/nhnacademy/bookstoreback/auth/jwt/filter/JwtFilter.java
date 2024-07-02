package com.nhnacademy.bookstoreback.auth.jwt.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Objects;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.nhnacademy.bookstoreback.auth.jwt.client.TokenReissueClient;
import com.nhnacademy.bookstoreback.auth.jwt.dto.CurrentUserDetails;
import com.nhnacademy.bookstoreback.auth.jwt.dto.response.ReissueTokensResponse;
import com.nhnacademy.bookstoreback.auth.jwt.utils.JwtUtils;
import com.nhnacademy.bookstoreback.user.domain.dto.response.UserTokenInfo;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
	private final JwtUtils jwtUtils;
	private final TokenReissueClient tokenReissueClient;

	@Override
	protected void doFilterInternal(
		@NonNull HttpServletRequest request,
		@NonNull HttpServletResponse response,
		@NonNull FilterChain filterChain
	) throws ServletException, IOException {
		String accessToken = request.getHeader("Authorization");
		String refreshToken = null;
		Cookie[] cookies = request.getCookies();
		if (Objects.nonNull(cookies)) {
			for (Cookie cookie : cookies) {
				if ("Refresh-Token".equals(cookie.getName())) {
					refreshToken = cookie.getValue();
					break;
				}
			}
		}

		if (Objects.isNull(accessToken)) {
			filterChain.doFilter(request, response);
			return;
		}

		String accessTokenErrorMessage = jwtUtils.validateToken(accessToken);
		if ("만료된 토큰입니다.".equals(accessTokenErrorMessage)) {
			ReissueTokensResponse reissuedTokens = tokenReissueClient.reissueTokensWithRefreshToken(refreshToken);
			accessToken = reissuedTokens.accessToken();
			refreshToken = reissuedTokens.refreshToken();
			accessTokenErrorMessage = jwtUtils.validateToken(accessToken);
		}
		if (Objects.nonNull(accessTokenErrorMessage)) {
			PrintWriter writer = response.getWriter();
			writer.print(accessTokenErrorMessage);
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}

		Long userId = jwtUtils.getUserIdFromToken(accessToken);
		List<String> userRoles = jwtUtils.getUserRolesFromToken(accessToken);

		UserTokenInfo user = UserTokenInfo.builder()
			.id(userId)
			.roles(userRoles)
			.build();
		CurrentUserDetails userDetails = new CurrentUserDetails(user);
		Authentication authentication = new UsernamePasswordAuthenticationToken(
			userDetails, null, userDetails.getAuthorities()
		);
		SecurityContextHolder.getContext().setAuthentication(authentication);

		response.setHeader("Authorization", accessToken);
		Cookie cookieWithRefreshToken = new Cookie("Refresh-Token", refreshToken);
		cookieWithRefreshToken.setPath("/");
		response.addCookie(cookieWithRefreshToken);

		filterChain.doFilter(request, response);
	}

	@Override
	@NonNull
	protected String getAlreadyFilteredAttributeName() {
		return this.getClass().getName() + ".FILTERED";
	}
}
