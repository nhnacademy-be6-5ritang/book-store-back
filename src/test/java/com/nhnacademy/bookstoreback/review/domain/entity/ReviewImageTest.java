package com.nhnacademy.bookstoreback.review.domain.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.nhnacademy.bookstoreback.image.domain.entity.Image;

class ReviewImageTest {

	@Test
	void testReviewImageCreation() {
		Image image = Image.builder().build();
		Review review = new Review();

		ReviewImage reviewImage = ReviewImage.toEntity(review, image);

		assertNull(reviewImage.getReviewImageId());
		assertNotNull(reviewImage);
		assertEquals(review, reviewImage.getReview());
		assertEquals(image, reviewImage.getImage());
	}

	@Test
	void testReviewImageBuilder() {
		Image image = Image.builder().build();
		Review review = new Review();

		ReviewImage reviewImage = ReviewImage.builder()
			.review(review)
			.image(image)
			.build();

		assertNotNull(reviewImage);
		assertEquals(review, reviewImage.getReview());
		assertEquals(image, reviewImage.getImage());
	}
}
