package com.nhnacademy.bookstoreback.order.domain.dto.response;

import lombok.Builder;

@Builder
public record GetBookOrderResponse(
	GetBookOrderGetBookResponse getBookResponse,
	Integer quantity,
	Long orderListId,
	Long orderId
) {
	public static GetBookOrderResponse from(GetBookOrderGetBookResponse getBookOrderGetBookResponse,
		Integer quantity, Long orderListId, Long orderId) {
		return GetBookOrderResponse.builder()
			.getBookResponse(getBookOrderGetBookResponse)
			.quantity(quantity)
			.orderListId(orderListId)
			.orderId(orderId)
			.build();
	}

	public static GetBookOrderResponse from(GetBookOrderGetBookResponse getBookOrderGetBookResponse,
		Integer quantity) {
		return GetBookOrderResponse.builder()
			.getBookResponse(getBookOrderGetBookResponse)
			.quantity(quantity)
			.build();
	}

	public static GetBookOrderResponse from(GetBookOrderGetBookResponse getBookOrderGetBookResponse,
		Integer quantity, Long orderListId) {
		return GetBookOrderResponse.builder()
			.getBookResponse(getBookOrderGetBookResponse)
			.quantity(quantity)
			.orderListId(orderListId)
			.build();
	}
}
