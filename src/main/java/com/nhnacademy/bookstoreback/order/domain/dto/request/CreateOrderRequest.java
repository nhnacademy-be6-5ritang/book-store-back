package com.nhnacademy.bookstoreback.order.domain.dto.request;

import java.math.BigDecimal;

import lombok.Builder;

@Builder
public record CreateOrderRequest(
	String payerName,
	String payerEmail,
	String payerNumber,
	String payerAddress,
	BigDecimal orderPrice,
	BigDecimal pointSale,
	BigDecimal couponSale
) {
}
