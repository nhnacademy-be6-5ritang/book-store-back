package com.nhnacademy.bookstoreback.usergrade.domain.dto.request;

import java.math.BigDecimal;

import lombok.Builder;

@Builder
public record CreateUserGradeRequest(
	String userGradeName,
	BigDecimal userGradeMinAmount,
	BigDecimal userGradeMaxAmount,
	BigDecimal userGradePointRate
) {
}
