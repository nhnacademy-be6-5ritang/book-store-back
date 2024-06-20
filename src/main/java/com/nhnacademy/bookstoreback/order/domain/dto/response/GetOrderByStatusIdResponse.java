package com.nhnacademy.bookstoreback.order.domain.dto.response;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;

import com.nhnacademy.bookstoreback.order.domain.entity.Order;

import lombok.Builder;

@Builder
public record GetOrderByStatusIdResponse(
	List<GetOrderResponse> orders,
	List<Integer> pageNumber
) {
	public static GetOrderByStatusIdResponse from(Page<Order> order) {
		List<GetOrderResponse> orderResponse = new ArrayList<>();
		List<Integer> pageNumbers = new ArrayList<>();
		for (Order orderItem : order) {
			orderResponse.add(GetOrderResponse.from(orderItem));
			pageNumbers.add(order.getPageable().getPageNumber());
		}
		return GetOrderByStatusIdResponse.builder()
			.orders(orderResponse)
			.pageNumber(pageNumbers)
			.build();
	}
}
