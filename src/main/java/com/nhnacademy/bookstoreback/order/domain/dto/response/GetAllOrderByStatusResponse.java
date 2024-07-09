package com.nhnacademy.bookstoreback.order.domain.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.nhnacademy.bookstoreback.order.domain.entity.Order;

import lombok.Builder;

@Builder
public record GetAllOrderByStatusResponse(
	Long orderId,
	LocalDateTime orderDate,
	BigDecimal orderPrice,
	String orderInfoId,
	String orderStatusName
) {
	public static GetAllOrderByStatusResponse from(Order order) {
		return GetAllOrderByStatusResponse.builder()
			.orderId(order.getOrderId())
			.orderDate(order.getOrderDate())
			.orderPrice(order.getOrderPrice())
			.orderInfoId(order.getOrderInfoId())
			.orderStatusName(order.getOrderStatus().getOrderStatusName())
			.build();
	}
}
