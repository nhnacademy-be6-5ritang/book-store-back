package com.nhnacademy.bookstoreback.user.domain.dto.request;

import java.time.LocalDate;

import lombok.Builder;

@Builder
public record CreateUserRequest(
	String name,
	String email,
	String password,
	LocalDate birth,
	String contact
) {
}
