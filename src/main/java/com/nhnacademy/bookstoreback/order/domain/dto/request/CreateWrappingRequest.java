package com.nhnacademy.bookstoreback.order.domain.dto.request;

import java.math.BigDecimal;

import lombok.Builder;

@Builder
public record CreateWrappingRequest(
	String wrappingPaperName,
	String wrappingPaperContent,
	BigDecimal wrappingPaperPrice
) {
}
