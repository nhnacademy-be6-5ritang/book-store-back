package com.nhnacademy.bookstoreback.user.domain.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.nhnacademy.bookstoreback.user.domain.entity.User;
import com.nhnacademy.bookstoreback.usergrade.domain.entity.UserGrade;
import com.nhnacademy.bookstoreback.userrole.domain.entity.UserRole;

public record GetMyUserInfoResponse(
	String name,
	String email,
	LocalDate birth,
	String contact,
	LocalDateTime createdAt,
	List<UserRole> roles,
	UserGrade userGrade
) {
	public static GetMyUserInfoResponse fromEntity(User user) {
		return new GetMyUserInfoResponse(
			user.getName(),
			user.getEmail(),
			user.getBirth(),
			user.getContact(),
			user.getCreatedAt(),
			user.getUserRoles(),
			user.getUserGrade()
		);
	}
}
