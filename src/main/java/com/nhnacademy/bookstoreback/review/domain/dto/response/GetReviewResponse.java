package com.nhnacademy.bookstoreback.review.domain.dto.response;

import java.time.LocalDateTime;

import com.nhnacademy.bookstoreback.review.domain.entity.Review;
import com.nhnacademy.bookstoreback.review.domain.entity.ReviewImage;

import lombok.Builder;

@Builder
public record GetReviewResponse(
	Long bookId,
	String userName,
	int reviewScore,
	String reviewComment,
	LocalDateTime reviewCreatedAt,
	String reviewImageUrl) {

	public static GetReviewResponse fromEntity(Review review, ReviewImage reviewImage) {
		return GetReviewResponse.builder()
			.userName(review.getUser().getName())
			.bookId(review.getBook().getBookId())
			.reviewScore(review.getReviewScore())
			.reviewComment(review.getReviewComment())
			.reviewCreatedAt(review.getReviewCreatedAt())
			.reviewImageUrl(reviewImage != null ? reviewImage.getImage().getImageUrl() : null)
			.build();
	}
}
