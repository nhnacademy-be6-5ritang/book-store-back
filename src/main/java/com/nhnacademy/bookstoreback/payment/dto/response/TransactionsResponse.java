package com.nhnacademy.bookstoreback.payment.dto.response;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Builder;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public record TransactionsResponse(
	String orderId,
	String orderName,
	BigDecimal amount,
	String status,
	String provider
) {
	public static TransactionsResponse from(String orderId, BigDecimal amount, String status, String provider,
		String orderName) {
		return TransactionsResponse.builder()
			.orderId(orderId)
			.amount(amount)
			.status(status)
			.provider(provider)
			.orderName(orderName)
			.build();
	}
}
