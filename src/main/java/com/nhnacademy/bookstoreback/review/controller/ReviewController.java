package com.nhnacademy.bookstoreback.review.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	public ResponseEntity<List<Review>> getReviews(@PathVariable Long bookId) {
		List<Review> reviews = reviewService.findAllReviews();
		return ResponseEntity.status(HttpStatus.OK).body(reviews);
	}

	@PostMapping
	public ResponseEntity<Review> createReview(@PathVariable Long bookId, @RequestBody Review review) {
		Review review1 = reviewService.saveReview(review);
		return ResponseEntity.status(HttpStatus.CREATED).body(review1);
	}

	@GetMapping("/{reviewId}")
	public ResponseEntity<Review> getReview(@PathVariable Long bookId, @PathVariable Long reviewId) {
		Review review = reviewService.findReviewById(reviewId);
		return ResponseEntity.status(HttpStatus.OK).body(review);
	}

	@PutMapping
	public ResponseEntity<Review> updateReview(@PathVariable Long bookId, @RequestBody Review review) {
		return null;
	}

	@DeleteMapping("/{reviewId}")
	public void deleteReview(@PathVariable Long bookId, @PathVariable Long reviewId) {
		reviewService.deleteReview(reviewId);
	}

}
