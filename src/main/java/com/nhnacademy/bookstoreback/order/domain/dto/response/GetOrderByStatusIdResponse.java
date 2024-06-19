package com.nhnacademy.bookstoreback.order.domain.dto.response;

import java.util.List;

import org.springframework.data.domain.Page;

import com.nhnacademy.bookstoreback.order.domain.entity.Order;

import lombok.Builder;

@Builder
public record GetOrderByStatusIdResponse(
	List<Order> orders,
	int pageNumber
) {
	public static GetOrderByStatusIdResponse from(Page<Order> order) {
		return GetOrderByStatusIdResponse.builder()
			.orders(order.getContent())
			.pageNumber(order.getNumber())
			.build();
	}
}
