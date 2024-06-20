package com.nhnacademy.bookstoreback.review.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nhnacademy.bookstoreback.review.domain.dto.request.CreateReviewRequest;
import com.nhnacademy.bookstoreback.review.domain.dto.request.UpdateReviewRequest;
import com.nhnacademy.bookstoreback.review.domain.dto.response.CreateReviewResponse;
import com.nhnacademy.bookstoreback.review.domain.dto.response.GetReviewResponse;
import com.nhnacademy.bookstoreback.review.domain.dto.response.UpdateReviewResponse;
import com.nhnacademy.bookstoreback.review.domain.entity.Review;

public interface ReviewService {
	List<Review> findAllReviews();

	Page<GetReviewResponse> findReviewsByBookId(Long userId, Long bookId, Pageable pageable);

	CreateReviewResponse saveReview(CreateReviewRequest request);

	GetReviewResponse findReviewById(Long id);

	UpdateReviewResponse updateReview(Long reviewId, UpdateReviewRequest request);

	void deleteReview(Long reviewId);
}
