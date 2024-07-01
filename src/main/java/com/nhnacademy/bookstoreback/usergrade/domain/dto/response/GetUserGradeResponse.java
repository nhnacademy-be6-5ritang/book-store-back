package com.nhnacademy.bookstoreback.usergrade.domain.dto.response;

import java.math.BigDecimal;

import com.nhnacademy.bookstoreback.usergrade.domain.entity.UserGrade;

import lombok.Builder;

@Builder
public record GetUserGradeResponse(
	String userGradeName,
	BigDecimal userGradeMinAmount,
	BigDecimal userGradeMaxAmount,
	BigDecimal userGradePointRate
) {
	public static GetUserGradeResponse fromEntity(UserGrade userGrade) {
		return GetUserGradeResponse.builder()
			.userGradeName(userGrade.getUserGradeName())
			.userGradeMinAmount(userGrade.getUserGradeMinAmount())
			.userGradeMaxAmount(userGrade.getUserGradeMaxAmount())
			.userGradePointRate(userGrade.getUserGradePointRate())
			.build();
	}
}
