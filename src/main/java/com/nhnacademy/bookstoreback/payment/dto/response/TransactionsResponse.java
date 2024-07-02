package com.nhnacademy.bookstoreback.payment.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Builder;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public record TransactionsResponse(
	String orderId,
	String orderName,
	BigDecimal amount,
	String status,
	String provider,
	LocalDateTime date
) {
	public static TransactionsResponse from(String orderId, BigDecimal amount, String status, String provider,
		String orderName, LocalDateTime date) {
		return TransactionsResponse.builder()
			.orderId(orderId)
			.amount(amount)
			.status(status)
			.provider(provider)
			.orderName(orderName)
			.date(date)
			.build();
	}
}
