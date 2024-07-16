package com.nhnacademy.bookstoreback.order.domain.dto.response;

import com.nhnacademy.bookstoreback.order.domain.entity.Order;

import lombok.Builder;

@Builder
public record CreateCartOrderResponse(
	Long orderId,
	String orderInfoId
) {
	public static CreateCartOrderResponse from(Order order) {
		return CreateCartOrderResponse.builder()
			.orderId(order.getOrderId())
			.orderInfoId(order.getOrderInfoId())
			.build();
	}
}
