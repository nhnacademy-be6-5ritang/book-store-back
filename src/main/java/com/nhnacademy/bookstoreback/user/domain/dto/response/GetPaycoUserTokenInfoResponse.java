package com.nhnacademy.bookstoreback.user.domain.dto.response;

import java.util.List;

import com.nhnacademy.bookstoreback.user.domain.entity.User;

import lombok.Builder;

@Builder
public record GetPaycoUserTokenInfoResponse(
	Long id,
	List<String> roles,
	String status
) {
	public static GetPaycoUserTokenInfoResponse fromEntity(User user) {
		return GetPaycoUserTokenInfoResponse.builder()
			.id(user.getId())
			.roles(user.getAllRoles())
			.status(user.getStatus().getUserStatusName())
			.build();
	}
}
