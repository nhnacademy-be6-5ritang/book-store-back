package com.nhnacademy.bookstoreback.order.domain.dto.response;

import java.math.BigDecimal;

import com.nhnacademy.bookstoreback.order.domain.entity.PaperType;

import lombok.Builder;

@Builder
public record CreatePaperResponse(
	Long paperTypeId,
	String paperName,
	String paperContent,
	BigDecimal paperPrice
) {
	public static CreatePaperResponse from(PaperType paperType) {
		return CreatePaperResponse.builder()
			.paperTypeId(paperType.getPaperTypeId())
			.paperName(paperType.getPaperName())
			.paperContent(paperType.getPaperContent())
			.paperPrice(paperType.getPaperPrice())
			.build();
	}
}
