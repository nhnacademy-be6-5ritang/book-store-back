package com.nhnacademy.bookstoreback.userstatus.domain.dto.response;

import com.nhnacademy.bookstoreback.userstatus.domain.entity.UserStatus;

import lombok.Builder;

@Builder
public record GetUserStatusResponse(String userStatusName) {
	public static GetUserStatusResponse fromEntity(UserStatus userStatus) {
		return GetUserStatusResponse.builder()
			.userStatusName(userStatus.getUserStatusName())
			.build();
	}
}
