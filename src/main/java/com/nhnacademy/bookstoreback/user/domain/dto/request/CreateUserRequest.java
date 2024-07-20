package com.nhnacademy.bookstoreback.user.domain.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record CreateUserRequest(
	@NotBlank
	@Size(max = 10)
	String name,

	@NotBlank
	@Size(max = 30)
	String email,

	@NotBlank
	@Size(min = 8, max = 100)
	String password,

	// @NotNull
	// LocalDate birth,
	@NotNull(message = "태어난 연도를 입력해주세요.")
	Integer year,

	@NotNull(message = "태어난 월을 입력해주세요.")
	Integer month,

	@NotNull(message = "태어난 일을 입력해주세요.")
	Integer day,

	@NotBlank
	@Size(min = 11, max = 11)
	String contact
) {
	public LocalDate getBirthDate() {
		return LocalDate.of(year, month, day);
	}
}
