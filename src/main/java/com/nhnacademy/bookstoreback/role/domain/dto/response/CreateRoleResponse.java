package com.nhnacademy.bookstoreback.role.domain.dto.response;

import com.nhnacademy.bookstoreback.role.domain.entity.Role;

import lombok.Builder;

@Builder
public record CreateRoleResponse(String roleName) {
	public static CreateRoleResponse fromEntity(Role role) {
		return CreateRoleResponse.builder()
			.roleName(role.getRoleName())
			.build();
	}
}
