package com.nhnacademy.bookstoreback.review.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.nhnacademy.bookstoreback.review.domain.dto.request.CreateReviewRequest;
import com.nhnacademy.bookstoreback.review.domain.dto.request.UpdateReviewRequest;
import com.nhnacademy.bookstoreback.review.domain.dto.response.CreateReviewResponse;
import com.nhnacademy.bookstoreback.review.domain.dto.response.GetReviewResponse;
import com.nhnacademy.bookstoreback.review.domain.dto.response.UpdateReviewResponse;
import com.nhnacademy.bookstoreback.review.service.ReviewService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ReviewController {
	private final ReviewService reviewService;

	@GetMapping("/reviews")
	public ResponseEntity<Page<GetReviewResponse>> getReviews(Pageable pageable) {
		return ResponseEntity.status(HttpStatus.OK).body(reviewService.findAllReviews(pageable));
	}

	@GetMapping("/books/{bookId}/reviews")
	public ResponseEntity<Page<GetReviewResponse>> getReviewsByBookId(Pageable pageable,
		@PathVariable Long bookId) {

		Page<GetReviewResponse> reviews = reviewService.findReviewsByBookId(bookId, pageable);

		return ResponseEntity.status(HttpStatus.OK).body(reviews);
	}

	@GetMapping("/users/me/reviews")
	public ResponseEntity<Page<GetReviewResponse>> getReviewsByUserId(Pageable pageable) {

		Long userId = 2L;

		Page<GetReviewResponse> reviews = reviewService.findReviewsByUserId(userId, pageable);

		return ResponseEntity.status(HttpStatus.OK).body(reviews);
	}

	@PostMapping("/reviews")
	public ResponseEntity<CreateReviewResponse> createReview(@RequestBody CreateReviewRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(reviewService.saveReview(request));
	}

	@GetMapping("/reviews/{reviewId}")
	public ResponseEntity<GetReviewResponse> getReview(@PathVariable Long reviewId) {
		return ResponseEntity.status(HttpStatus.OK).body(reviewService.findReviewById(reviewId));
	}

	@PutMapping("/reviews/{reviewId}")
	public ResponseEntity<UpdateReviewResponse> updateReview(@RequestBody UpdateReviewRequest request,
		@PathVariable Long reviewId) {
		return ResponseEntity.status(HttpStatus.OK).body(reviewService.updateReview(reviewId, request));
	}

	@DeleteMapping("/reviews/{reviewId}")
	public void deleteReview(@PathVariable Long reviewId) {
		reviewService.deleteReview(reviewId);
	}

}
