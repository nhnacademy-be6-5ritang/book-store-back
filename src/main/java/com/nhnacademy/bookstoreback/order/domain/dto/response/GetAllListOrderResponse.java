package com.nhnacademy.bookstoreback.order.domain.dto.response;

import java.util.ArrayList;
import java.util.List;

import com.nhnacademy.bookstoreback.order.domain.entity.Order;

import lombok.Builder;

@Builder
public record GetAllListOrderResponse(
	List<GetAllOrderResponse> orders
) {

	public static GetAllListOrderResponse from(List<Order> all) {
		List<GetAllOrderResponse> orders = new ArrayList<>();
		for (Order order : all) {
			orders.add(GetAllOrderResponse.from(order));
		}
		return GetAllListOrderResponse.builder()
			.orders(orders)
			.build();
	}
}
