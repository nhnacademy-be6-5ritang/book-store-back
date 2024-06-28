package com.nhnacademy.bookstoreback.order.domain.dto.response;

import com.nhnacademy.bookstoreback.order.domain.entity.WrappingPaper;

import lombok.Builder;

@Builder
public record UpdateWrappingResponse(
	String paperName
) {
	public static UpdateWrappingResponse from(WrappingPaper wrappingPaper) {
		return UpdateWrappingResponse.builder()
			.paperName(wrappingPaper.getPaperType().getPaperName())
			.build();
	}
}
