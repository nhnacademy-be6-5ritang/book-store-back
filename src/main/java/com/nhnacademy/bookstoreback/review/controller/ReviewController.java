package com.nhnacademy.bookstoreback.review.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.UUID;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.client.HttpClientErrorException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookstoreback.auth.annotation.CurrentUser;
import com.nhnacademy.bookstoreback.auth.jwt.dto.CurrentUserDetails;
import com.nhnacademy.bookstoreback.book.domain.entity.Book;
import com.nhnacademy.bookstoreback.book.repository.BookRepository;
import com.nhnacademy.bookstoreback.image.domain.entity.Image;
import com.nhnacademy.bookstoreback.image.repository.ImageRepository;
import com.nhnacademy.bookstoreback.review.domain.dto.request.CreateReviewRequest;
import com.nhnacademy.bookstoreback.review.domain.dto.request.UpdateReviewRequest;
import com.nhnacademy.bookstoreback.review.domain.dto.response.GetReviewResponse;
import com.nhnacademy.bookstoreback.review.domain.entity.Review;
import com.nhnacademy.bookstoreback.review.repository.ReviewRepository;
import com.nhnacademy.bookstoreback.review.service.ReviewService;
import com.nhnacademy.bookstoreback.reviewimage.domain.entity.ReviewImage;
import com.nhnacademy.bookstoreback.reviewimage.repository.ReviewImageRepository;
import com.nhnacademy.bookstoreback.user.domain.entity.User;
import com.nhnacademy.bookstoreback.user.repository.UserRepository;

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

	@GetMapping("/books/{bookId}/reviews/page")
	public ResponseEntity<Page<GetReviewResponse>> getReviewsByBookId(
		@PageableDefault(page = 1, size = 5) Pageable pageable,
		@PathVariable Long bookId) {

		Page<GetReviewResponse> reviews = reviewService.findReviewsByBookId(bookId, pageable);

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
		@ModelAttribute CreateReviewRequest request,
		@RequestPart(value = "image", required = false) MultipartFile image,
		@CurrentUser CurrentUserDetails currentUser) {
		reviewService.saveReview(request, currentUser, image);
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
