package com.nhnacademy.bookstoreback.order.domain.dto.request;

import java.util.List;

import lombok.Builder;

@Builder
public record CreateOrderListPost(
	List<Long> paperId,
	List<Integer> quantity
) {
}
