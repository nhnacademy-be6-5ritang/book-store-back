package com.nhnacademy.bookstoreback.user.domain.dto.response;

import java.time.LocalDate;

import com.nhnacademy.bookstoreback.user.domain.entity.User;

import lombok.Builder;

@Builder
public record UpdateUserInfoResponse(
	String name,
	String email,
	LocalDate birth,
	String contact
) {
	public static UpdateUserInfoResponse fromEntity(User updatedUser) {
		return UpdateUserInfoResponse.builder()
			.name(updatedUser.getName())
			.email(updatedUser.getEmail())
			.birth(updatedUser.getBirth())
			.contact(updatedUser.getContact())
			.build();
	}
}
