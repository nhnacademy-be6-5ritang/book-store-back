package com.nhnacademy.bookstoreback.point.transaction.domain.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.nhnacademy.bookstoreback.point.transaction.domain.entity.PointTransaction;

import lombok.Builder;

@Builder
public record GetPointTransactionResponse(
	String pointEarningPolicyType,
	BigDecimal pointTransactionAmount,
	LocalDateTime pointTransactionDate
) {
	public static GetPointTransactionResponse fromEntity(PointTransaction pointTransaction) {
		return GetPointTransactionResponse.builder()
			.pointEarningPolicyType(pointTransaction.getPointEarningPolicy().getPointEarningPolicyType())
			.pointTransactionAmount(pointTransaction.getPointTransactionAmount())
			.pointTransactionDate(pointTransaction.getPointTransactionDate())
			.build();
	}
}
