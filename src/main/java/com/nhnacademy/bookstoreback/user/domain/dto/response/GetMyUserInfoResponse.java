package com.nhnacademy.bookstoreback.user.domain.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.nhnacademy.bookstoreback.user.domain.entity.User;
import com.nhnacademy.bookstoreback.userrole.domain.entity.UserRole;

public record GetMyUserInfoResponse(
	String name,
	String email,
	LocalDate birth,
	String contact,
	LocalDateTime createdAt,
	List<String> roles,
	String userGradeName,
	String userStatusName,
	BigDecimal points
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
			.toList();
		return new GetMyUserInfoResponse(
			user.getName(),
			user.getEmail(),
			user.getBirth(),
			user.getContact(),
			user.getCreatedAt(),
			roles.stream().map(UserRoleResponse::roleName).toList(),
			user.getUserGrade().getUserGradeName(),
			user.getStatus().getUserStatusName(),
			user.getPoints()
		);
	}
}
