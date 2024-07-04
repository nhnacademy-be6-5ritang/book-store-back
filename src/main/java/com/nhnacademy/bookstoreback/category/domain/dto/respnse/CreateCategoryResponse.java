package com.nhnacademy.bookstoreback.category.domain.dto.respnse;

import com.nhnacademy.bookstoreback.category.domain.entity.Category;

import lombok.Builder;

@Builder
public record CreateCategoryResponse(
	String categoryName,
	Long parentCategoryId) {
	public static CreateCategoryResponse fromEntity(Category category) {
		Long parentCategoryId =
			(category.getParentCategory() != null) ? category.getParentCategory().getCategoryId() : null;

		return CreateCategoryResponse.builder()
			.categoryName(category.getCategoryName())
			.parentCategoryId(parentCategoryId)
			.build();
	}
}
