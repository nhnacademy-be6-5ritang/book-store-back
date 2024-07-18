package com.nhnacademy.bookstoreback.order.domain.dto.response;

import com.nhnacademy.bookstoreback.order.domain.entity.OrderStatus;

import lombok.Builder;

@Builder
public record GetOrderStatusResponse(
	Long orderStatusId,
	String orderStatusName
) {
	public static GetOrderStatusResponse from(OrderStatus orderStatus) {
		return GetOrderStatusResponse.builder()
			.orderStatusId(orderStatus.getOrderStatusId())
			.orderStatusName(orderStatus.getOrderStatusName())
			.build();
	}
}
