package com.nhnacademy.bookstoreback.reviewimage.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nhnacademy.bookstoreback.reviewimage.domain.entity.ReviewImage;
import com.nhnacademy.bookstoreback.reviewimage.service.ReviewImageService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews/{reviewId}/reviewImages")
public class ReviewImageController {
	private final ReviewImageService reviewImageService;

	@GetMapping
	public ResponseEntity<List<ReviewImage>> getReviewImages(@PathVariable Long reviewId) {
		List<ReviewImage> reviewImages = reviewImageService.getReviewImages(reviewId);
		return ResponseEntity.status(HttpStatus.OK).body(reviewImages);
	}

	@PostMapping
	public ResponseEntity<ReviewImage> createReviewImage(@PathVariable Long reviewId,
		@RequestBody ReviewImage reviewImage) {
		ReviewImage reviewImage1 = reviewImageService.saveReviewImage(reviewImage);
		return ResponseEntity.status(HttpStatus.CREATED).body(reviewImage1);
	}

	@GetMapping("/{reviewImageId}")
	public ResponseEntity<ReviewImage> getReviewImage(@PathVariable Long reviewImageId) {
		ReviewImage reviewImage = reviewImageService.getReviewImageById(reviewImageId);
		return ResponseEntity.status(HttpStatus.OK).body(reviewImage);
	}

	@DeleteMapping
	public ResponseEntity<ReviewImage> deleteReviewImage(@PathVariable Long reviewId) {
		reviewImageService.deleteReviewImage(reviewId);
		return ResponseEntity.noContent().build();
	}

}
