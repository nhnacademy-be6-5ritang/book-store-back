package com.nhnacademy.bookstoreback.userstatus.domain.dto.request;

import lombok.Builder;

@Builder
public record CreateUserStatusRequest(
	String userStatusName
) {
}
