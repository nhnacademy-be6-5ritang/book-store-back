package com.nhnacademy.bookstoreback.point.transaction.domain.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.nhnacademy.bookstoreback.point.transaction.domain.entity.PointTransaction;

import lombok.Builder;

@Builder
public record CreatePointTransactionResponse(
	Long pointTransactionId,
	Long pointEarningPolicyId,
	BigDecimal pointTransactionAmount,
	LocalDateTime pointTransactionDate
) {
	public static CreatePointTransactionResponse fromEntity(PointTransaction savedPointTransaction) {
		return CreatePointTransactionResponse.builder()
			.pointTransactionId(savedPointTransaction.getId())
			.pointEarningPolicyId(savedPointTransaction.getPointEarningPolicy().getId())
			.pointTransactionAmount(savedPointTransaction.getPointTransactionAmount())
			.pointTransactionDate(savedPointTransaction.getPointTransactionDate())
			.build();
	}
}
