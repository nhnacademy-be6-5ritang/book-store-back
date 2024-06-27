package com.nhnacademy.bookstoreback.auth.jwt.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.nhnacademy.bookstoreback.auth.jwt.dto.AppCustomUserDetails;
import com.nhnacademy.bookstoreback.user.domain.entity.Role;

import lombok.RequiredArgsConstructor;

// authenticated test를 위한 서비스
@Service
@RequiredArgsConstructor
public class TokenService {
	public Map<String, Object> getUserInfo(AppCustomUserDetails currentUser) {
		Long id = currentUser.getUserId();
		Role role = currentUser.getAuthorities().stream()
			.findFirst()
			.map(authority -> Role.valueOf(authority.getAuthority()))
			.orElse(null);

		Map<String, Object> userInfo = new HashMap<>();
		userInfo.put("id", id);
		userInfo.put("role", role);

		return userInfo;
	}
}
