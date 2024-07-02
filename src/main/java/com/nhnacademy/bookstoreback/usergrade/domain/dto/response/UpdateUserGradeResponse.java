package com.nhnacademy.bookstoreback.usergrade.domain.dto.response;

import java.math.BigDecimal;

import com.nhnacademy.bookstoreback.usergrade.domain.entity.UserGrade;

import lombok.Builder;

@Builder
public record UpdateUserGradeResponse(
	String userGradeName,
	BigDecimal userGradeMinAmount,
	BigDecimal userGradeMaxAmount,
	BigDecimal userGradePointRate
) {
	public static UpdateUserGradeResponse fromEntity(UserGrade userGrade) {
		return UpdateUserGradeResponse.builder()
			.userGradeName(userGrade.getUserGradeName())
			.userGradeMinAmount(userGrade.getUserGradeMinAmount())
			.userGradeMaxAmount(userGrade.getUserGradeMaxAmount())
			.userGradePointRate(userGrade.getUserGradePointRate())
			.build();
	}
}
