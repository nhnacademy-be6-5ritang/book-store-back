package com.nhnacademy.bookstoreback.review.domain.dto.request;

public record CreateReviewRequest(
	Long bookId,
	int reviewScore,
	String reviewComment,
	String fileName) {
}
