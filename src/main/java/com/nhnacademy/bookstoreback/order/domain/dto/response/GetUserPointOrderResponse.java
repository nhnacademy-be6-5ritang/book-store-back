package com.nhnacademy.bookstoreback.order.domain.dto.response;

import java.math.BigDecimal;

import lombok.Builder;

@Builder
public record GetUserPointOrderResponse(
	BigDecimal userPoint
) {
	public static GetUserPointOrderResponse from(BigDecimal userPoint) {
		return GetUserPointOrderResponse.builder()
			.userPoint(userPoint)
			.build();
	}
}
