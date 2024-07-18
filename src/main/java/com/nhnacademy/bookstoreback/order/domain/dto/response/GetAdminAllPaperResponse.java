package com.nhnacademy.bookstoreback.order.domain.dto.response;

import java.util.ArrayList;
import java.util.List;

import com.nhnacademy.bookstoreback.order.domain.entity.PaperType;

import lombok.Builder;

@Builder
public record GetAdminAllPaperResponse(
	List<GetPaperResponse> papers
) {

	public static GetAdminAllPaperResponse from(List<PaperType> all) {
		List<GetPaperResponse> papers = new ArrayList<>();
		for (PaperType paperType : all) {
			papers.add(GetPaperResponse.from(paperType));
		}
		return GetAdminAllPaperResponse.builder()
			.papers(papers)
			.build();
	}
}
