package com.nhnacademy.bookstoreback.order.domain.dto.response;

import com.nhnacademy.bookstoreback.order.domain.entity.OrderStatus;

import lombok.Builder;

@Builder
public record GetOrderStatusResponse(
	String orderStatusName
) {
	public static GetOrderStatusResponse from(OrderStatus orderStatus) {
		return GetOrderStatusResponse.builder()
			.orderStatusName(orderStatus.getOrderStatusName())
			.build();
	}
}
