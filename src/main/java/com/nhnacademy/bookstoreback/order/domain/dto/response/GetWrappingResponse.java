package com.nhnacademy.bookstoreback.order.domain.dto.response;

import java.math.BigDecimal;

import com.nhnacademy.bookstoreback.order.domain.entity.WrappingPaper;

import lombok.Builder;

@Builder
public record GetWrappingResponse(
	Long wrappingId,
	String name,
	BigDecimal price,
	Integer quantity
) {
	public static GetWrappingResponse from(WrappingPaper wrappingPaper) {
		return GetWrappingResponse.builder()
			.wrappingId(wrappingPaper.getWrappingPaperId())
			.name(wrappingPaper.getPaperType().getPaperName())
			.price(wrappingPaper.getPaperType().getPaperPrice())
			.quantity(wrappingPaper.getPaperQuantity())
			.build();
	}
}
