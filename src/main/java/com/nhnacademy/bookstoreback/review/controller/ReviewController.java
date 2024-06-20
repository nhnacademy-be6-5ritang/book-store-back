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

import com.nhnacademy.bookstoreback.review.domain.dto.request.CreateReviewRequest;
import com.nhnacademy.bookstoreback.review.domain.dto.request.UpdateReviewRequest;
import com.nhnacademy.bookstoreback.review.domain.dto.response.CreateReviewResponse;
import com.nhnacademy.bookstoreback.review.domain.dto.response.GetReviewResponse;
import com.nhnacademy.bookstoreback.review.domain.dto.response.UpdateReviewResponse;
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
		Page<GetReviewResponse> reviews = reviewService.findReviewsByBookId(userId, bookId, pageable);

		return ResponseEntity.status(HttpStatus.OK).body(reviews);
	}

	@PostMapping
	public ResponseEntity<CreateReviewResponse> createReview(@PathVariable Long bookId,
		@RequestBody CreateReviewRequest request) {
		CreateReviewResponse response = reviewService.saveReview(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@GetMapping("/{reviewId}")
	public ResponseEntity<GetReviewResponse> getReview(@PathVariable Long bookId, @PathVariable Long reviewId) {
		GetReviewResponse response = reviewService.findReviewById(reviewId);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@PutMapping("/{reviewId}")
	public ResponseEntity<UpdateReviewResponse> updateReview(@PathVariable Long bookId,
		@RequestBody UpdateReviewRequest request,
		@PathVariable Long reviewId) {
		UpdateReviewResponse response = reviewService.updateReview(reviewId, request);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@DeleteMapping("/{reviewId}")
	public void deleteReview(@PathVariable Long bookId, @PathVariable Long reviewId) {
		reviewService.deleteReview(reviewId);
	}

}
