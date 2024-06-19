package com.nhnacademy.bookstoreback.review.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nhnacademy.bookstoreback.review.domain.dto.response.GetReviewResponse;
import com.nhnacademy.bookstoreback.review.domain.entity.Review;

public interface ReviewService {
	List<Review> findAllReviews();

	Page<GetReviewResponse> findReviewsByBookId(Long bookId, Pageable pageable);

	Review saveReview(Review review);

	Review findReviewById(Long id);

	Review updateReview(Review review);

	void deleteReview(Long reviewId);

}
