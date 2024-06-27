package com.nhnacademy.bookstoreback.review.domain.dto.response;

import java.time.LocalDateTime;

import com.nhnacademy.bookstoreback.book.domain.entity.Book;
import com.nhnacademy.bookstoreback.review.domain.entity.Review;
import com.nhnacademy.bookstoreback.user.domain.entity.User;

import lombok.Builder;

@Builder
public record CreateReviewResponse(
	Long reviewId,
	Long bookId,
	Long userId,
	int reviewScore,
	String reviewComment,
	LocalDateTime reviewCreatedAt) {

	public static CreateReviewResponse fromEntity(Review review, User user, Book book) {
		return CreateReviewResponse.builder()
			.reviewId(review.getReviewId())
			.reviewScore(review.getReviewScore())
			.reviewComment(review.getReviewComment())
			.reviewCreatedAt(review.getReviewCreatedAt())
			.userId(user.getId())
			.bookId(book.getBookId())
			.build();
	}
}
