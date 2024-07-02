package com.nhnacademy.bookstoreback.userstatus.domain.dto.response;

import com.nhnacademy.bookstoreback.userstatus.domain.entity.UserStatus;

import lombok.Builder;

@Builder
public record CreateUserStatusResponse(
	String userStatusName
) {
	public static CreateUserStatusResponse fromEntity(UserStatus savedUserStatus) {
		return CreateUserStatusResponse.builder()
			.userStatusName(savedUserStatus.getUserStatusName())
			.build();
	}
}
