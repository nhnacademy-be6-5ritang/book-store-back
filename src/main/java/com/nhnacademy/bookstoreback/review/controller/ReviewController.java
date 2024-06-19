package com.nhnacademy.bookstoreback.review.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nhnacademy.bookstoreback.review.domain.dto.response.GetReviewResponse;
import com.nhnacademy.bookstoreback.review.domain.entity.Review;
import com.nhnacademy.bookstoreback.review.service.ReviewService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/books/{bookId}/reviews")
public class ReviewController {
	private final ReviewService reviewService;

	@GetMapping
	public ResponseEntity<Page<GetReviewResponse>> getReviews(@RequestParam("userId") Long userId,
		@RequestParam("page") int page, @RequestParam("size") int size, @RequestParam(required = false) String sort,
		@PathVariable Long bookId) {

		Pageable pageable;
		if (sort != null) {
			pageable = PageRequest.of(page, size, Sort.by(sort));
		} else {
			pageable = PageRequest.of(page, size);
		}
		Page<GetReviewResponse> reviews = reviewService.findReviewsByBookId(userId, pageable);

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
