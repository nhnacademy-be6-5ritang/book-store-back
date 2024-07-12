package com.nhnacademy.bookstoreback.review.repository;

public interface CustomReviewRepository {
	double getReviewsAverageScoreByBookId(Long bookId);
}
