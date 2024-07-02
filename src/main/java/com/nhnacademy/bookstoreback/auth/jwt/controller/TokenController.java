package com.nhnacademy.bookstoreback.auth.jwt.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nhnacademy.bookstoreback.auth.annotation.CurrentUser;
import com.nhnacademy.bookstoreback.auth.jwt.dto.CurrentUserDetails;
import com.nhnacademy.bookstoreback.auth.jwt.service.TokenService;

import lombok.RequiredArgsConstructor;

// authenticated test 를 위한 컨트롤러
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class TokenController {
	private final TokenService tokenService;

	@GetMapping("/info")
	public ResponseEntity<Map<String, Object>> getUserInfo(@CurrentUser CurrentUserDetails user) {
		return ResponseEntity.status(HttpStatus.OK).body(tokenService.getUserInfo(user));
	}
}
