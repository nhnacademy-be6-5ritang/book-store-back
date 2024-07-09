package com.nhnacademy.bookstoreback.user.domain.dto.response;

import java.time.LocalDate;

import com.nhnacademy.bookstoreback.user.domain.entity.User;

import lombok.Builder;

@Builder
public record CreateUserResponse(
	Long id,
	String name,
	String email,
	LocalDate birth,
	String contact,
	String userStatus,
	String userGrade
) {
	public static CreateUserResponse fromEntity(User savedUser) {
		return CreateUserResponse.builder()
			.id(savedUser.getId())
			.name(savedUser.getName())
			.email(savedUser.getEmail())
			.birth(savedUser.getBirth())
			.contact(savedUser.getContact())
			.userStatus(savedUser.getStatus().getUserStatusName())
			.userGrade(savedUser.getUserGrade().getUserGradeName())
			.build();
	}
}
