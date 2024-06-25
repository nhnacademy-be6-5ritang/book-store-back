package com.nhnacademy.bookstoreback.auth.jwt.filter;

import java.io.IOException;
import java.time.Duration;
import java.util.Collection;
import java.util.Iterator;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.nhnacademy.bookstoreback.auth.jwt.utils.JwtUtils;
import com.nhnacademy.bookstoreback.user.domain.entity.Role;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
	private final AuthenticationManager authenticationManager;
	private final JwtUtils jwtUtils;
	private final RedisTemplate<String, Object> redisTemplate;
	private final Long accessTokenExpiresIn;
	private final Long refreshTokenExpiresIn;

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
		throws AuthenticationException {
		String userEmail = obtainUsername(request);
		String password = obtainPassword(request);

		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userEmail, password,
			null);

		return authenticationManager.authenticate(authToken);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
		Authentication authentication) throws IOException, ServletException {
		String userEmail = authentication.getName();
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
		GrantedAuthority auth = iterator.next();
		Role role = Role.valueOf(auth.getAuthority());

		String accessToken = jwtUtils.generateToken("access", userEmail, role, accessTokenExpiresIn);
		String refreshToken = jwtUtils.generateToken("refresh", userEmail, role, refreshTokenExpiresIn);

		saveRefreshToken(userEmail, refreshToken, refreshTokenExpiresIn);

		response.setHeader("Authorization", "Bearer " + accessToken);
		response.addCookie(createCookie("Refresh-Token", refreshToken));
		response.setStatus(HttpStatus.OK.value());
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException failed) throws IOException, ServletException {
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
	}

	private void saveRefreshToken(String userEmail, String refreshToken, Long expiresIn) {
		String redisKey = "RefreshToken:" + userEmail;
		redisTemplate.delete(redisKey);
		redisTemplate.opsForHash().put(redisKey, "token", refreshToken);
		redisTemplate.expire(redisKey, Duration.ofMillis(expiresIn));
	}

	private Cookie createCookie(String key, String value) {
		Cookie cookie = new Cookie(key, value);
		cookie.setMaxAge(24 * 60 * 60);
		// cookie.setSecure(true);
		// cookie.setPath("/");
		cookie.setHttpOnly(true);

		return cookie;
	}
}
