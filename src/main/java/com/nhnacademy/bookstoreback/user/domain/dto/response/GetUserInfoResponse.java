package com.nhnacademy.bookstoreback.user.domain.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.nhnacademy.bookstoreback.user.domain.entity.User;
import com.nhnacademy.bookstoreback.userrole.domain.entity.UserRole;

public record GetUserInfoResponse(
	Long userId,
	String name,
	String email,
	LocalDateTime createdAt,
	List<String> roles,
	String userGradeName,
	String userStatusName
) {
	private record UserRoleResponse(
		Long userRoleId,
		String roleName
	) {
		public static UserRoleResponse fromEntity(UserRole userRole) {
			return new UserRoleResponse(
				userRole.getId(),
				userRole.getRoleName()
			);
		}
	}

	public static GetUserInfoResponse fromEntity(User user) {
		List<UserRoleResponse> roles = user.getUserRoles().stream()
			.map(UserRoleResponse::fromEntity)
			.toList();
		return new GetUserInfoResponse(
			user.getId(),
			user.getName(),
			user.getEmail(),
			user.getCreatedAt(),
			roles.stream().map(UserRoleResponse::roleName).toList(),
			user.getUserGrade().getUserGradeName(),
			user.getStatus().getUserStatusName()
		);
	}
}
