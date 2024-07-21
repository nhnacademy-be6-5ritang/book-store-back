package com.nhnacademy.bookstoreback.review.domain.dto.request;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

class ReviewRequestTest {

	private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
	private final Validator validator = validatorFactory.getValidator();

	@Test
	void testCreateReviewRequest() {
		CreateReviewRequest request = new CreateReviewRequest(1L, 5, "Excellent book!", "cover.jpg");

		assertEquals(1L, request.bookId());
		assertEquals(5, request.reviewScore());
		assertEquals("Excellent book!", request.reviewComment());
		assertEquals("cover.jpg", request.fileName());
	}

	@Test
	void testUpdateReviewRequest() {
		UpdateReviewRequest request = UpdateReviewRequest.builder()
			.reviewScore(4)
			.reviewComment("Good book!")
			.build();

		assertEquals(4, request.reviewScore());
		assertEquals("Good book!", request.reviewComment());
	}

	@Test
	void testCreateReviewRequestValidation() {
		CreateReviewRequest request = new CreateReviewRequest(null, 5, "Valid comment", "cover.jpg");
		Set<ConstraintViolation<CreateReviewRequest>> violations = validator.validate(request);
		assertTrue(violations.stream().anyMatch(v -> v.getMessage() != null), "Expected validation error for bookId");

		request = new CreateReviewRequest(1L, 5, "", "cover.jpg");
		violations = validator.validate(request);
		assertTrue(violations.stream().anyMatch(v -> v.getMessage() != null),
			"Expected validation error for reviewComment");

		String longComment = "This comment is intentionally made very long to exceed the maximum allowed size of 400 characters in the validation annotations. This should throw an exception.".repeat(
			10);
		request = new CreateReviewRequest(1L, 5, longComment, "cover.jpg");
		violations = validator.validate(request);
		assertTrue(violations.stream().anyMatch(v -> v.getMessage() != null),
			"Expected validation error for reviewComment size");
	}

	@Test
	void testUpdateReviewRequestValidation() {
		UpdateReviewRequest request = UpdateReviewRequest.builder()
			.reviewScore(5)
			.reviewComment("")
			.build();
		Set<ConstraintViolation<UpdateReviewRequest>> violations = validator.validate(request);
		assertTrue(violations.stream().anyMatch(v -> v.getMessage() != null),
			"Expected validation error for reviewComment");

		String longComment = "This comment is intentionally made very long to exceed the maximum allowed size of 400 characters in the validation annotations.".repeat(
			10);
		request = UpdateReviewRequest.builder()
			.reviewScore(5)
			.reviewComment(longComment)
			.build();
		violations = validator.validate(request);
		assertTrue(violations.stream().anyMatch(v -> v.getMessage() != null),
			"Expected validation error for reviewComment size");
	}
}
