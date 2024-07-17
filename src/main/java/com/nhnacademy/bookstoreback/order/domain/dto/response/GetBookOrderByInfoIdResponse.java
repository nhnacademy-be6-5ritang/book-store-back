package com.nhnacademy.bookstoreback.order.domain.dto.response;

import lombok.Builder;

@Builder
public record GetBookOrderByInfoIdResponse(
	Long orderListId,
	FindByInfoIdBookOrderGetBookResponse getBookResponse,
	FindByInfoIdBookOrderGetOrderResponse getOrderResponse,
	Integer quantity,
	String title
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

	public static GetBookOrderByInfoIdResponse from(String title) {
		return GetBookOrderByInfoIdResponse.builder()
			.title(title)
			.build();
	}

}
