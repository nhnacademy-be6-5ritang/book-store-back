package com.nhnacademy.bookstoreback.point.transaction.domain.dto.request;

import java.math.BigDecimal;

import lombok.Builder;

@Builder
public record CreatePointTransactionRequest(
	Long pointEarningPolicyId,
	BigDecimal pointTransactionAmount
) {
}
