package com.nhnacademy.bookstoreback.category.domain.dto.request;

import com.nhnacademy.bookstoreback.category.domain.entity.Category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UpdateCategoryRequest(
	@NotBlank @Size(max = 20) String categoryName,
	Long parentCategoryId) {
	public static UpdateCategoryRequest fromEntity(Category category) {
		return UpdateCategoryRequest.builder()
			.categoryName(category.getCategoryName())
			.parentCategoryId(category.getParentCategory().getCategoryId())
			.build();
	}
}
