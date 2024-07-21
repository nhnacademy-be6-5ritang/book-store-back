package com.nhnacademy.bookstoreback.review.domain.dto.response;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.nhnacademy.bookstoreback.book.domain.entity.Book;
import com.nhnacademy.bookstoreback.image.domain.entity.Image;
import com.nhnacademy.bookstoreback.review.domain.entity.Review;
import com.nhnacademy.bookstoreback.review.domain.entity.ReviewImage;
import com.nhnacademy.bookstoreback.user.domain.entity.User;

class ReviewResponseTest {

	@Test
	void testFromEntity_withReviewImage() {
		User user = mock(User.class);
		when(user.getName()).thenReturn("John Doe");

		Book book = mock(Book.class);
		when(book.getBookId()).thenReturn(123L);

		Review review = mock(Review.class);
		when(review.getUser()).thenReturn(user);
		when(review.getBook()).thenReturn(book);
		when(review.getReviewScore()).thenReturn(5);
		when(review.getReviewComment()).thenReturn("Great book!");
		when(review.getReviewCreatedAt()).thenReturn(LocalDateTime.now());

		Image image = mock(Image.class);
		when(image.getImageUrl()).thenReturn("http://example.com/image.jpg");

		ReviewImage reviewImage = mock(ReviewImage.class);
		when(reviewImage.getImage()).thenReturn(image);

		GetReviewResponse response = GetReviewResponse.fromEntity(review, reviewImage);

		assertEquals(123L, response.bookId());
		assertEquals("John Doe", response.userName());
		assertEquals(5, response.reviewScore());
		assertEquals("Great book!", response.reviewComment());
		assertEquals(review.getReviewCreatedAt(), response.reviewCreatedAt());
		assertEquals("http://example.com/image.jpg", response.reviewImageUrl());
	}

	@Test
	void testFromEntity_withoutReviewImage() {
		User user = mock(User.class);
		when(user.getName()).thenReturn("Jane Doe");

		Book book = mock(Book.class);
		when(book.getBookId()).thenReturn(456L);

		Review review = mock(Review.class);
		when(review.getUser()).thenReturn(user);
		when(review.getBook()).thenReturn(book);
		when(review.getReviewScore()).thenReturn(4);
		when(review.getReviewComment()).thenReturn("Good read.");
		when(review.getReviewCreatedAt()).thenReturn(LocalDateTime.now());

		GetReviewResponse response = GetReviewResponse.fromEntity(review, null);

		assertEquals(456L, response.bookId());
		assertEquals("Jane Doe", response.userName());
		assertEquals(4, response.reviewScore());
		assertEquals("Good read.", response.reviewComment());
		assertEquals(review.getReviewCreatedAt(), response.reviewCreatedAt());
		assertNull(response.reviewImageUrl());
	}
}
