package com.nhnacademy.bookstoreback.order.domain.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.nhnacademy.bookstoreback.order.domain.entity.Order;

import lombok.Builder;

@Builder
public record GetNonOrderByInfoResponse(
	Long orderId,
	LocalDateTime orderDate,
	BigDecimal orderPrice,
	String orderInfoId,
	String name
) {
	public static GetNonOrderByInfoResponse from(Order order) {
		return GetNonOrderByInfoResponse.builder()
			.orderId(order.getOrderId())
			.orderDate(order.getOrderDate())
			.orderPrice(order.getOrderPrice())
			.orderInfoId(order.getOrderInfoId())
			.name(order.getOrderStatus().getOrderStatusName())
			.build();
	}
}
