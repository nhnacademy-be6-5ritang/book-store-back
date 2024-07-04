package com.nhnacademy.bookstoreback.category.domain.dto.respnse;

import com.nhnacademy.bookstoreback.category.domain.entity.Category;

import lombok.Builder;

@Builder
public record UpdateCategoryResponse(
	String categoryName,
	String parentCategoryName) {
	public static UpdateCategoryResponse fromEntity(Category category) {
		String parentCategoryName =
			(category.getParentCategory() != null) ? category.getParentCategory().getCategoryName() : null;

		return UpdateCategoryResponse.builder()
			.categoryName(category.getCategoryName())
			.parentCategoryName(parentCategoryName)
			.build();
	}
}
