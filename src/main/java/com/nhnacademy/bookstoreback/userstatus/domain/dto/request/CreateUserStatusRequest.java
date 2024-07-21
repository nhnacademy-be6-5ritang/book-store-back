package com.nhnacademy.bookstoreback.userstatus.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record CreateUserStatusRequest(
	@NotBlank
	@Size(max = 10)
	String userStatusName
) {
}
