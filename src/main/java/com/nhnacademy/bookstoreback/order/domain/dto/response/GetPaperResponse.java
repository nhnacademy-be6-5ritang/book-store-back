package com.nhnacademy.bookstoreback.order.domain.dto.response;

import java.math.BigDecimal;

import com.nhnacademy.bookstoreback.order.domain.entity.PaperType;

import lombok.Builder;

@Builder
public record GetPaperResponse(
	Long paperId,
	String paperName,
	String paperContent,
	BigDecimal paperPrice
) {
	public static GetPaperResponse from(PaperType paperType) {
		return GetPaperResponse.builder()
			.paperId(paperType.getPaperTypeId())
			.paperName(paperType.getPaperName())
			.paperContent(paperType.getPaperContent())
			.paperPrice(paperType.getPaperPrice())
			.build();
	}
}
