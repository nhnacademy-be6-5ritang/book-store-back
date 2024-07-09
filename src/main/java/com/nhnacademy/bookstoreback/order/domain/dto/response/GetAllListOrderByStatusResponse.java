package com.nhnacademy.bookstoreback.order.domain.dto.response;

import java.util.ArrayList;
import java.util.List;

import com.nhnacademy.bookstoreback.order.domain.entity.Order;

import lombok.Builder;

@Builder
public record GetAllListOrderByStatusResponse(
	List<GetAllOrderByStatusResponse> orders
) {

	public static GetAllListOrderByStatusResponse from(List<Order> all) {
		List<GetAllOrderByStatusResponse> orders = new ArrayList<>();
		for (Order order : all) {
			orders.add(GetAllOrderByStatusResponse.from(order));
		}
		return GetAllListOrderByStatusResponse.builder()
			.orders(orders)
			.build();
	}
}
