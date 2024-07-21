package com.nhnacademy.bookstoreback.usergrade.domain.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record CreateUserGradeRequest(
	@NotBlank
	@Size(max = 10)
	String userGradeName,
	@NotNull
	BigDecimal userGradeMinAmount,
	@NotNull
	BigDecimal userGradeMaxAmount,
	@NotNull
	BigDecimal userGradePointRate
) {
}
