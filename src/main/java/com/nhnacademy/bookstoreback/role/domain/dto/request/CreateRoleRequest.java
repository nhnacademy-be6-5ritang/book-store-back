package com.nhnacademy.bookstoreback.role.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record CreateRoleRequest(
	@NotBlank @Size(max = 10)
	String roleName
) {
}
