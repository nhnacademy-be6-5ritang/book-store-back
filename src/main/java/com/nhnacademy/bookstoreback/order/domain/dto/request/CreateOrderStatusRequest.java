package com.nhnacademy.bookstoreback.order.domain.dto.request;

import lombok.Builder;

@Builder
public record CreateOrderStatusRequest(
	String orderStatusName
) {
}
