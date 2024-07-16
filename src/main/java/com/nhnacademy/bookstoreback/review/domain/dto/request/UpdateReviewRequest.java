package com.nhnacademy.bookstoreback.review.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UpdateReviewRequest(
	@NotNull int reviewScore,
	@NotBlank @Size(max = 400) String reviewComment) {
}
