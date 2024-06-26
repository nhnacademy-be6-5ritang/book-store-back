package com.nhnacademy.bookstoreback.user.domain.dto.response;

import com.nhnacademy.bookstoreback.user.domain.entity.User;

import lombok.Builder;

@Builder
public record GetUserInfoResponse(
	Long id,
	String password,
	String role
) {
	public static GetUserInfoResponse fromEntity(User user) {
		return GetUserInfoResponse.builder()
			.id(user.getId())
			.password(user.getPassword())
			.role(String.valueOf(user.getRole()))
			.build();
	}
}

