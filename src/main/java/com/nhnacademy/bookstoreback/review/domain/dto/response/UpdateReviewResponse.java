package com.nhnacademy.bookstoreback.review.domain.dto.response;

import com.nhnacademy.bookstoreback.review.domain.entity.Review;

import lombok.Builder;

@Builder
public record UpdateReviewResponse(
	int reviewScore,
	String reviewComment) {

	public static UpdateReviewResponse fromEntity(Review review) {
		return UpdateReviewResponse.builder()
			.reviewComment(review.getReviewComment())
			.reviewScore(review.getReviewScore())
			.build();
	}
}
