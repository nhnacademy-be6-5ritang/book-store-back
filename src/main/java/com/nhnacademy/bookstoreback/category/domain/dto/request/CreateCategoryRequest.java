package com.nhnacademy.bookstoreback.category.domain.dto.request;

import com.nhnacademy.bookstoreback.category.domain.entity.Category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record CreateCategoryRequest(
	@NotBlank @Size(max = 20) String categoryName,
	Long parentCategoryId) {
	public static CreateCategoryRequest fromEntity(Category category) {
		return CreateCategoryRequest.builder()
			.categoryName(category.getCategoryName())
			.parentCategoryId(category.getParentCategory().getCategoryId())
			.build();
	}
}
