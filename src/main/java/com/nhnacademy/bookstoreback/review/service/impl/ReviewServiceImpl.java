package com.nhnacademy.bookstoreback.review.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.nhnacademy.bookstoreback.review.domain.dto.response.GetReviewResponse;
import com.nhnacademy.bookstoreback.review.domain.entity.Review;
import com.nhnacademy.bookstoreback.review.repository.ReviewRepository;
import com.nhnacademy.bookstoreback.review.service.ReviewService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
	private final ReviewRepository reviewRepository;

	@Override
	public List<Review> findAllReviews() {
		return reviewRepository.findAll();
	}

	@Override
	public Page<GetReviewResponse> findReviewsByBookId(Long bookId, Pageable pageable) {
		Page<Review> reviews = reviewRepository.findAllByBookId(bookId, pageable);
		return reviews
			.map(review -> new GetReviewResponse(bookId, review.getReviewScore(), review.getReviewComment(),
				review.getReviewCreatedAt()));
	}

	@Override
	public Review saveReview(Review review) {
		return reviewRepository.save(review);
	}

	@Override
	public Review findReviewById(Long id) {
		return reviewRepository.findById(id).orElse(null);
	}

	@Override
	public Review updateReview(Review review) {
		return null;
	}

	@Override
	public void deleteReview(Long reviewId) {
		reviewRepository.deleteById(reviewId);
	}
}
