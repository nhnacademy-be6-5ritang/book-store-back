package com.nhnacademy.bookstoreback.order.domain.dto.response;

import java.util.Collections;
import java.util.List;

import org.springframework.data.domain.Page;

import com.nhnacademy.bookstoreback.order.domain.entity.Order;

import lombok.Builder;

@Builder
public record GetOrderByStatusIdResponse(
	List<GetOrderResponse> orders,
	int pageNumber
) {
	public static GetOrderByStatusIdResponse from(Page<Order> order) {
		GetOrderResponse orderResponse = null;
		for (Order orderItem : order) {
			orderResponse = GetOrderResponse.from(orderItem);
		}
		return GetOrderByStatusIdResponse.builder()
			.orders(Collections.singletonList(orderResponse))
			.pageNumber(order.getNumber())
			.build();
	}
}
