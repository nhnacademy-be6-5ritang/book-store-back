package com.nhnacademy.bookstoreback.review.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateReviewRequest(
	@NotNull Long orderListId,
	@NotNull int reviewScore,
	@NotBlank @Size(max = 400) String reviewComment,
	String fileName) {
}
