package com.nhnacademy.bookstoreback.category.domain.dto.respnse;

import com.nhnacademy.bookstoreback.category.domain.entity.Category;

import lombok.Builder;

@Builder
public record GetCategoryResponse(
	Long categoryId,
	String categoryName,
	String parentCategoryName) {
	public static GetCategoryResponse fromEntity(Category category) {
		String parentCategoryName =
			(category.getParentCategory() != null) ? category.getParentCategory().getCategoryName() : null;

		return GetCategoryResponse.builder()
			.categoryId(category.getCategoryId())
			.categoryName(category.getCategoryName())
			.parentCategoryName(parentCategoryName)
			.build();
	}
}
