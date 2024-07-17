package com.nhnacademy.bookstoreback.order.domain.dto.response;

import lombok.Builder;

@Builder
public record GetBookOrderByInfoIdResponse(
	String title
) {
	public static GetBookOrderByInfoIdResponse from(String title) {
		return GetBookOrderByInfoIdResponse.builder()
			.title(title)
			.build();
	}
}
