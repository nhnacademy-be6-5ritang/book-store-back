package com.nhnacademy.bookstoreback.order.domain.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.nhnacademy.bookstoreback.order.domain.entity.Order;

import lombok.Builder;

@Builder
public record CreateOrderResponse(
	String infoId,
	BigDecimal orderPrice,
	LocalDateTime orderDate,
	BigDecimal pointSale,
	BigDecimal couponSale
) {
	public static CreateOrderResponse from(Order order) {
		return CreateOrderResponse.builder()
			.infoId(order.getOrderInfoId())
			.orderPrice(order.getOrderPrice())
			.orderDate(order.getOrderDate())
			.pointSale(order.getOrderPointSale())
			.couponSale(order.getOrderCouponSale())
			.build();
	}
}
