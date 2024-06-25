package com.nhnacademy.bookstoreback.order.domain.dto.response;

import lombok.Builder;

@Builder
public record GetBookOrderByInfoIdResponse(
	FindByInfoIdBookOrderGetBookResponse getBookResponse,
	FindByInfoIdBookOrderGetOrderResponse getOrderResponse,
	Integer quantity
) {
	public static GetBookOrderByInfoIdResponse from(FindByInfoIdBookOrderGetBookResponse getBookResponse,
		FindByInfoIdBookOrderGetOrderResponse getOrderResponse,
		Integer quantity) {
		return GetBookOrderByInfoIdResponse.builder()
			.getBookResponse(getBookResponse)
			.getOrderResponse(getOrderResponse)
			.quantity(quantity)
			.build();
	}
}
