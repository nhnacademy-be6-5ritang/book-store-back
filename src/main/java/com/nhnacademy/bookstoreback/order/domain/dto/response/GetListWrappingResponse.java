package com.nhnacademy.bookstoreback.order.domain.dto.response;

import java.util.ArrayList;
import java.util.List;

import com.nhnacademy.bookstoreback.order.domain.entity.WrappingPaper;

import lombok.Builder;

@Builder
public record GetListWrappingResponse(
	List<GetWrappingResponse> wrapping
) {

	public static GetListWrappingResponse from(List<WrappingPaper> all) {
		List<GetWrappingResponse> wrapping = new ArrayList<>();
		for (WrappingPaper wrappingPaper : all) {
			wrapping.add(GetWrappingResponse.from(wrappingPaper));
		}
		return GetListWrappingResponse.builder()
			.wrapping(wrapping)
			.build();
	}
}
