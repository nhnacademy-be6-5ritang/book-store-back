package com.nhnacademy.bookstoreback.review.domain.dto.response;

import java.time.LocalDateTime;

import com.nhnacademy.bookstoreback.review.domain.entity.Review;

import lombok.Builder;

@Builder
public record GetReviewResponse(
	Long bookId,
	Long userId,
	int reviewScore,
	String reviewComment,
	LocalDateTime reviewCreatedAt) {

	public static GetReviewResponse fromEntity(Review review) {
		return GetReviewResponse.builder()
			.userId(review.getUser().getId())
			.bookId(review.getBook().getBookId())
			.reviewScore(review.getReviewScore())
			.reviewComment(review.getReviewComment())
			.reviewCreatedAt(review.getReviewCreatedAt())
			.build();
	}
}
