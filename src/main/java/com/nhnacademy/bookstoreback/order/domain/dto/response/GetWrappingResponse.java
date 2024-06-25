package com.nhnacademy.bookstoreback.order.domain.dto.response;

import com.nhnacademy.bookstoreback.order.domain.entity.Order;
import com.nhnacademy.bookstoreback.order.domain.entity.PaperType;
import com.nhnacademy.bookstoreback.order.domain.entity.WrappingPaper;

import lombok.Builder;

@Builder
public record GetWrappingResponse(
	Order order,
	PaperType paperType
) {
	public static GetWrappingResponse from(WrappingPaper wrappingPaper) {
		return GetWrappingResponse.builder()
			.order(wrappingPaper.getOrder())
			.paperType(wrappingPaper.getPaperType())
			.build();
	}
}
