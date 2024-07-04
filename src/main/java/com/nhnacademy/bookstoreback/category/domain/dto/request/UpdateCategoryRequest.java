package com.nhnacademy.bookstoreback.category.domain.dto.request;

import com.nhnacademy.bookstoreback.category.domain.entity.Category;

import lombok.Builder;

@Builder
public record UpdateCategoryRequest(
	String categoryName,
	Long parentCategoryId) {
	public static UpdateCategoryRequest fromEntity(Category category) {
		return UpdateCategoryRequest.builder()
			.categoryName(category.getCategoryName())
			.parentCategoryId(category.getParentCategory().getCategoryId())
			.build();
	}
}
