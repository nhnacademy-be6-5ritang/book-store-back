package com.nhnacademy.bookstoreback.usergrade.domain.dto.request;

import java.math.BigDecimal;

import lombok.Builder;

@Builder
public record UpdateUserGradeRequest(
	BigDecimal userGradeMinAmount,
	BigDecimal userGradeMaxAmount,
	BigDecimal userGradePointRate
) {
}
