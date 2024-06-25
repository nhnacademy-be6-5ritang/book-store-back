package com.nhnacademy.bookstoreback.auth.jwt.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nhnacademy.bookstoreback.auth.jwt.service.TokenService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class TokenController {
	private final TokenService tokenService;

	@GetMapping("/info")
	public ResponseEntity<Map<String, Object>> getUserInfo(HttpServletRequest request) {
		return ResponseEntity.status(HttpStatus.OK).body(tokenService.getUserInfo(request));
	}

	@PostMapping("/reissue")
	public ResponseEntity<?> refresh(HttpServletRequest request, HttpServletResponse response) {
		Cookie[] cookies = request.getCookies();

		Map<String, Object> tokens = tokenService.reissueToken(cookies);

		if (tokens == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Refresh Token 재발급 중 오류가 발생했습니다.");
		}

		removeCookieWithRefreshToken(cookies, response);

		response.setHeader("Authorization", (String)tokens.get("access"));
		response.addCookie((Cookie)tokens.get("CookieWithRefreshToken"));

		return ResponseEntity.status(HttpStatus.OK).body("Refresh Token 재발급 성공");
	}

	private void removeCookieWithRefreshToken(Cookie[] cookies, HttpServletResponse response) {
		for (Cookie cookie : cookies) {
			if ("Refresh-Token".equals(cookie.getName())) {
				cookie.setValue(null);
				cookie.setMaxAge(0);
				cookie.setPath("/");
				response.addCookie(cookie);
			}
		}
	}
}
