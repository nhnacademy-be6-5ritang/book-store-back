package com.nhnacademy.bookstoreback.usergrade.domain.dto.response;

import java.math.BigDecimal;

import com.nhnacademy.bookstoreback.usergrade.domain.entity.UserGrade;

import lombok.Builder;

@Builder
public record CreateUserGradeResponse(
	String userGradeName,
	BigDecimal userGradeMinAmount,
	BigDecimal userGradeMaxAmount,
	BigDecimal userGradePointRate
) {
	public static CreateUserGradeResponse fromEntity(UserGrade userGrade) {
		return CreateUserGradeResponse.builder()
			.userGradeName(userGrade.getUserGradeName())
			.userGradeMinAmount(userGrade.getUserGradeMinAmount())
			.userGradeMaxAmount(userGrade.getUserGradeMaxAmount())
			.userGradePointRate(userGrade.getUserGradePointRate())
			.build();
	}
}
