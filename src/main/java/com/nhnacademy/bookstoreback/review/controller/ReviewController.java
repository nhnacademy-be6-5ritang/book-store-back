package com.nhnacademy.bookstoreback.review.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nhnacademy.bookstoreback.review.domain.entity.Review;
import com.nhnacademy.bookstoreback.review.service.ReviewService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/books/{bookId}/reviews")
public class ReviewController {
	private final ReviewService reviewService;

	@GetMapping
	public List<Review> getReviews(@PathVariable Long bookId) {
		return reviewService.findAllReviews();
	}

	@PostMapping
	public Review createReview(@PathVariable Long bookId, @RequestBody Review review) {
		return reviewService.saveReview(review);
	}

	@GetMapping("/{reviewId}")
	public Review getReview(@PathVariable Long bookId, @PathVariable Long reviewId) {
		return reviewService.findReviewById(reviewId);
	}

	@PutMapping
	public Review updateReview(@PathVariable Long bookId, @RequestBody Review review) {
		return reviewService.updateReview(review);
	}

	@DeleteMapping("/{reviewId}")
	public void deleteReview(@PathVariable Long bookId, @PathVariable Long reviewId) {
		reviewService.deleteReview(reviewId);
	}
	
}
