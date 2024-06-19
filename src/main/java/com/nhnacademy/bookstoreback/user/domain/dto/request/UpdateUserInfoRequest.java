package com.nhnacademy.bookstoreback.user.domain.dto.request;

import java.time.LocalDate;

import lombok.Builder;

@Builder
public record UpdateUserInfoRequest(
	Long id,
	String name,
	String email,
	Boolean isEmailCertified,
	String password,
	LocalDate birth,
	String contact,
	String address
) {
}
