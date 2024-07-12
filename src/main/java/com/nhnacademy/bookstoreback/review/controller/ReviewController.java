package com.nhnacademy.bookstoreback.review.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

import com.nhnacademy.bookstoreback.auth.annotation.CurrentUser;
import com.nhnacademy.bookstoreback.auth.jwt.dto.CurrentUserDetails;
import com.nhnacademy.bookstoreback.review.domain.dto.request.CreateReviewRequest;
import com.nhnacademy.bookstoreback.review.domain.dto.request.UpdateReviewRequest;
import com.nhnacademy.bookstoreback.review.domain.dto.response.GetReviewResponse;
import com.nhnacademy.bookstoreback.review.service.ReviewService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReviewController {

	private final ReviewService reviewService;

	@GetMapping("/reviews/page")
	public ResponseEntity<Page<GetReviewResponse>> getReviews(@PageableDefault(page = 1, size = 5) Pageable pageable) {
		return ResponseEntity.status(HttpStatus.OK).body(reviewService.findAllReviews(pageable));
	}

	@GetMapping("/books/{bookId}/reviews/all/page")
	public ResponseEntity<Page<GetReviewResponse>> getReviewsByBookId(
		@PageableDefault(page = 1, size = 5) Pageable pageable,
		@PathVariable Long bookId) {

		Page<GetReviewResponse> reviews = reviewService.findReviewsByBookId(bookId, pageable);

		return ResponseEntity.status(HttpStatus.OK).body(reviews);
	}

	@GetMapping("/books/{bookId}/reviews/photo/page")
	public ResponseEntity<Page<GetReviewResponse>> getPhotoReviewsByBookId(
		@PageableDefault(page = 1, size = 5) Pageable pageable,
		@PathVariable Long bookId) {

		Page<GetReviewResponse> reviews = reviewService.getPhotoReviewsByBookId(bookId, pageable);

		return ResponseEntity.status(HttpStatus.OK).body(reviews);
	}

	@GetMapping("/books/{bookId}/reviews/general/page")
	public ResponseEntity<Page<GetReviewResponse>> getGeneralReviewsByBookId(
		@PageableDefault(page = 1, size = 5) Pageable pageable,
		@PathVariable Long bookId) {

		Page<GetReviewResponse> reviews = reviewService.getGeneralReviewsByBookId(bookId, pageable);

		return ResponseEntity.status(HttpStatus.OK).body(reviews);
	}

	@GetMapping("/users/me/reviews/page")
	public ResponseEntity<Page<GetReviewResponse>> getReviewsByUserId(
		@PageableDefault(page = 1, size = 5) Pageable pageable, @CurrentUser CurrentUserDetails currentUser) {

		Page<GetReviewResponse> reviews = reviewService.findReviewsByUserId(pageable, currentUser);

		return ResponseEntity.status(HttpStatus.OK).body(reviews);
	}

	@PostMapping("/reviews")
	public ResponseEntity<Void> createReview(
		@RequestBody CreateReviewRequest request,
		@CurrentUser CurrentUserDetails currentUser) {
		reviewService.createReview(request, currentUser);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@GetMapping("/reviews/{reviewId}")
	public ResponseEntity<GetReviewResponse> getReview(@PathVariable Long reviewId) {
		return ResponseEntity.status(HttpStatus.OK).body(reviewService.findReviewById(reviewId));
	}

	@PutMapping("/reviews/{reviewId}")
	public ResponseEntity<Void> updateReview(@RequestBody UpdateReviewRequest request,
		@PathVariable Long reviewId) {
		reviewService.updateReview(reviewId, request);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@DeleteMapping("/reviews/{reviewId}")
	public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId) {
		reviewService.deleteReview(reviewId);
		return ResponseEntity.noContent().build();
	}

}
