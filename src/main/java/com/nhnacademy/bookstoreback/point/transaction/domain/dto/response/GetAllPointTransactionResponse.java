package com.nhnacademy.bookstoreback.point.transaction.domain.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.nhnacademy.bookstoreback.point.transaction.domain.entity.PointTransaction;

import lombok.Builder;

@Builder
public record GetAllPointTransactionResponse(
	Long userId,
	Long policyId,
	BigDecimal amount,
	LocalDateTime date
) {
	public static GetAllPointTransactionResponse fromEntity(PointTransaction pointTransaction) {
		return GetAllPointTransactionResponse.builder()
			.userId(pointTransaction.getUser().getId())
			.policyId(pointTransaction.getPointEarningPolicy().getId())
			.amount(pointTransaction.getPointTransactionAmount())
			.date(pointTransaction.getPointTransactionDate())
			.build();
	}
}
