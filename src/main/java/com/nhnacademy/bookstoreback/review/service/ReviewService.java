package com.nhnacademy.bookstoreback.review.service;

import java.util.List;

import com.nhnacademy.bookstoreback.review.domain.entity.Review;

public interface ReviewService {
	List<Review> findAllReviews();

	List<Review> findReviewsByBookId(Long bookId);

	Review saveReview(Review review);

	Review findReviewById(Long id);

	Review updateReview(Review review);

	void deleteReview(Long reviewId);

}
