package com.nhnacademy.bookstoreback.order.domain.dto.response;

import java.math.BigDecimal;

import com.nhnacademy.bookstoreback.order.domain.entity.WrappingPaper;

import lombok.Builder;

@Builder
public record GetWrappingResponse(
	String wrappingPaperName,
	String wrappingPaperContent,
	BigDecimal wrappingPaperPrice
) {
	public static GetWrappingResponse from(WrappingPaper wrappingPaper) {
		return GetWrappingResponse.builder()
			.wrappingPaperName(wrappingPaper.getWrappingPaperName())
			.wrappingPaperContent(wrappingPaper.getWrappingPaperContent())
			.wrappingPaperPrice(wrappingPaper.getWrappingPaperPrice())
			.build();
	}
}
