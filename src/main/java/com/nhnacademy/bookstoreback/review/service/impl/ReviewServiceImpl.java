package com.nhnacademy.bookstoreback.review.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

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
	public List<Review> findReviewsByBookId(Long bookId) {
		return List.of();
	}

	@Override
	public List<Review> findReviewsByUserId(Long userId) {
		return reviewRepository.findAllByUserId(userId);
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
