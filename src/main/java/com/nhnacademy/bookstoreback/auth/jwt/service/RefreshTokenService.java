package com.nhnacademy.bookstoreback.auth.jwt.service;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.nhnacademy.bookstoreback.auth.jwt.utils.JwtUtils;
import com.nhnacademy.bookstoreback.user.domain.entity.Role;

import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
	private final RedisTemplate<String, Object> redisTemplate;
	private final JwtUtils jwtUtils;

	@Value("${spring.jwt.access-token.expires-in}")
	private Long accessTokenExpiresIn;
	@Value("${spring.jwt.refresh-token.expires-in}")
	private Long refreshTokenExpiresIn;

	public Map<String, Object> reissueToken(Cookie[] cookies) {
		Map<String, Object> tokens = new HashMap<>();

		String refreshToken = null;
		for (Cookie cookie : cookies) {
			if ("Refresh-Token".equals(cookie.getName())) {
				refreshToken = cookie.getValue();
				break;
			}
		}

		if (refreshToken == null) {
			return null;
		}

		if (jwtUtils.validateToken(refreshToken) != null) {
			return null;
		}

		String tokenType = jwtUtils.getTokenTypeFromToken(refreshToken);
		if (!"refresh".equals(tokenType)) {
			return null;
		}

		String email = jwtUtils.getEmailFromToken(refreshToken);
		String redisKey = "RefreshToken:" + email;
		boolean refreshTokenExists = redisTemplate.opsForHash().hasKey(redisKey, "token");
		if (!refreshTokenExists) {
			return null;
		}

		Role role = jwtUtils.getRoleFromToken(refreshToken);

		String newAccessToken = jwtUtils.generateToken("access", email, role, accessTokenExpiresIn);
		String newRefreshToken = jwtUtils.generateToken("refresh", email, role, refreshTokenExpiresIn);

		saveRefreshToken(email, newRefreshToken, refreshTokenExpiresIn);
		Cookie cookieWithRefreshToken = createCookie("Refresh-Token", newRefreshToken);

		tokens.put("access", newAccessToken);
		tokens.put("CookieWithRefreshToken", cookieWithRefreshToken);

		return tokens;
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
		cookie.setHttpOnly(true);

		return cookie;
	}
}
