package com.nhnacademy.bookstoreback.review.domain.dto.request;

import com.nhnacademy.bookstoreback.review.domain.entity.Review;

import lombok.Builder;

@Builder
public record CreateReviewRequest(
	Long bookId,
	int reviewScore,
	String reviewComment) {

	public static CreateReviewRequest fromEntity(Review review) {
		return CreateReviewRequest.builder()
			.bookId(review.getBook().getBookId())
			.reviewScore(review.getReviewScore())
			.reviewComment(review.getReviewComment())
			.build();
	}
}
