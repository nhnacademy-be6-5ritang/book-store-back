package com.nhnacademy.bookstoreback.order.domain.dto.response;

import lombok.Builder;

@Builder
public record GetBookOrderByInfoIdResponse(
	Long orderListId,
	FindByInfoIdBookOrderGetBookResponse getBookResponse,
	FindByInfoIdBookOrderGetOrderResponse getOrderResponse,
	Integer quantity
) {
	public static GetBookOrderByInfoIdResponse from(Long orderListId,
		FindByInfoIdBookOrderGetBookResponse getBookResponse,
		FindByInfoIdBookOrderGetOrderResponse getOrderResponse,
		Integer quantity) {
		return GetBookOrderByInfoIdResponse.builder()
			.orderListId(orderListId)
			.getBookResponse(getBookResponse)
			.getOrderResponse(getOrderResponse)
			.quantity(quantity)
			.build();
	}
}
