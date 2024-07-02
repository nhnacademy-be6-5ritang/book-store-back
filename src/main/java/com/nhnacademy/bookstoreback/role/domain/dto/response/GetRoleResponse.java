package com.nhnacademy.bookstoreback.role.domain.dto.response;

import com.nhnacademy.bookstoreback.role.domain.entity.Role;

import lombok.Builder;

@Builder
public record GetRoleResponse(String roleName) {
	public static GetRoleResponse fromEntity(Role role) {
		return GetRoleResponse.builder()
			.roleName(role.getRoleName())
			.build();
	}
}
