package com.nhnacademy.bookstoreback.user.domain.dto.request;

import java.time.LocalDate;

import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UpdateUserInfoRequest(

	@NotBlank
	@Size(max = 10)
	String name,
	@NotBlank
	@Size(max = 30)
	String email,
	@NotBlank
	@Size(min = 8, max = 100)
	String password,
	@NotNull
	LocalDate birth,
	@NotBlank
	@Size(min = 11, max = 11)
	String contact
) {
	public UpdateUserInfoRequest encodePassword(PasswordEncoder passwordEncoder) {
		return new UpdateUserInfoRequest(name, email, passwordEncoder.encode(password), birth, contact);
	}
}
