package com.nhnacademy.bookstoreback.order.domain.dto.response;

import java.util.ArrayList;
import java.util.List;

import com.nhnacademy.bookstoreback.order.domain.entity.PaperType;

import lombok.Builder;

@Builder
public record GetAllPaperResponse(
	List<GetPaperResponse> papers
) {

	public static GetAllPaperResponse from(List<PaperType> all) {
		List<GetPaperResponse> papers = new ArrayList<>();
		for (PaperType paperType : all) {
			if (paperType.getPaperName() != null && !"포장불가".equals(paperType.getPaperName()) && !"포장지 선택 X".equals(
				paperType.getPaperName())) {
				papers.add(GetPaperResponse.from(paperType));
			}
		}
		return GetAllPaperResponse.builder()
			.papers(papers)
			.build();
	}
}
