package com.nhnacademy.bookstoreback.userrole.domain.dto.response;

import java.util.List;
import java.util.stream.Collectors;

import com.nhnacademy.bookstoreback.userrole.domain.entity.UserRole;

import lombok.Builder;

@Builder
public record GetUserRoleResponse(
	Long userId,
	List<Long> roleIds,
	List<String> roleNames
) {
	public static GetUserRoleResponse fromEntities(Long userId, List<UserRole> userRoles) {
		List<Long> roleIds = userRoles.stream()
			.map(userRole -> userRole.getRole().getId())
			.collect(Collectors.toList());

		List<String> roleNames = userRoles.stream()
			.map(UserRole::getRoleName)
			.distinct()
			.collect(Collectors.toList());

		return GetUserRoleResponse.builder()
			.userId(userId)
			.roleIds(roleIds)
			.roleNames(roleNames)
			.build();
	}
}
