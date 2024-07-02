package com.nhnacademy.bookstoreback.userrole.domain.dto.response;

import java.util.List;

import com.nhnacademy.bookstoreback.userrole.domain.entity.UserRole;

import lombok.Builder;

@Builder
public record AddUserRoleResponse(
	Long userId,
	Long roleId,
	List<String> roleNames
) {
	public static AddUserRoleResponse fromEntity(UserRole userRole) {
		return AddUserRoleResponse.builder()
			.userId(userRole.getUser().getId())
			.roleId(userRole.getRole().getId())
			.roleNames(userRole.getUser().getAllRoles())
			.build();
	}
}
