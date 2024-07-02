package com.nhnacademy.bookstoreback.user.domain.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.nhnacademy.bookstoreback.user.domain.entity.User;
import com.nhnacademy.bookstoreback.usergrade.domain.entity.UserGrade;
import com.nhnacademy.bookstoreback.userrole.domain.entity.UserRole;

public record GetMyUserInfoResponse(
	String name,
	String email,
	LocalDate birth,
	String contact,
	LocalDateTime createdAt,
	List<UserRoleResponse> roles,
	UserGrade userGrade
) {
	private record UserRoleResponse(
		Long id,
		String roleName
	) {
		public static UserRoleResponse fromEntity(UserRole userRole) {
			return new UserRoleResponse(
				userRole.getId(),
				userRole.getRoleName()
			);
		}
	}

	public static GetMyUserInfoResponse fromEntity(User user) {
		List<UserRoleResponse> roles = user.getUserRoles().stream()
			.map(UserRoleResponse::fromEntity)
			.collect(Collectors.toList());
		return new GetMyUserInfoResponse(
			user.getName(),
			user.getEmail(),
			user.getBirth(),
			user.getContact(),
			user.getCreatedAt(),
			roles,
			user.getUserGrade()
		);
	}
}
