package com.nhnacademy.bookstoreback.order.domain.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.nhnacademy.bookstoreback.order.domain.entity.Order;

import lombok.Builder;

@Builder
public record GetOrderByInfoResponse(
	Long orderId,
	String infoId,
	String payername,
	String payerEmail,
	String payerAddress,
	LocalDateTime orderDate,
	String status,
	BigDecimal price,
	BigDecimal couponSale,
	BigDecimal pointSale
) {
	public static GetOrderByInfoResponse from(Order order) {
		return GetOrderByInfoResponse.builder()
			.orderId(order.getOrderId())
			.infoId(order.getOrderInfoId())
			.payername(order.getOrderPayerName())
			.payerEmail(order.getOrderPayerEmail())
			.payerAddress(order.getOrderPayerAddress())
			.orderDate(order.getOrderDate())
			.status(order.getOrderStatus().getOrderStatusName())
			.price(order.getOrderPrice())
			.couponSale(order.getOrderCouponSale())
			.pointSale(order.getOrderPointSale())
			.build();
	}
}
