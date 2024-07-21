package com.nhnacademy.bookstoreback.usergrade.domain.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record UpdateUserGradeRequest(
	@NotNull
	BigDecimal userGradeMinAmount,
	@NotNull
	BigDecimal userGradeMaxAmount,
	@NotNull
	BigDecimal userGradePointRate
) {
}
