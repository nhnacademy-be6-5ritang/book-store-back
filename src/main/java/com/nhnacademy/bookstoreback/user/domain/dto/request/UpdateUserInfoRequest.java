package com.nhnacademy.bookstoreback.user.domain.dto.request;

import java.time.LocalDate;

import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.Builder;

@Builder
public record UpdateUserInfoRequest(
	Long id,
	String name,
	String email,
	String password,
	LocalDate birth,
	String contact
) {
	public UpdateUserInfoRequest encodePassword(PasswordEncoder passwordEncoder) {
		return new UpdateUserInfoRequest(id, name, email, passwordEncoder.encode(password), birth, contact);
	}
}
