package com.nhnacademy.bookstoreback.review.domain.entity;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import com.nhnacademy.bookstoreback.auth.jwt.dto.CurrentUserDetails;
import com.nhnacademy.bookstoreback.author.domain.entity.Author;
import com.nhnacademy.bookstoreback.book.domain.entity.Book;
import com.nhnacademy.bookstoreback.book.domain.entity.BookImage;
import com.nhnacademy.bookstoreback.image.domain.entity.Image;
import com.nhnacademy.bookstoreback.publisher.domain.entity.Publisher;
import com.nhnacademy.bookstoreback.review.domain.dto.request.CreateReviewRequest;
import com.nhnacademy.bookstoreback.user.domain.dto.response.UserTokenInfo;
import com.nhnacademy.bookstoreback.user.domain.entity.User;
import com.nhnacademy.bookstoreback.wishlist.domain.entity.WishList;

class ReviewTest {
	private WishList wishList;
	private CurrentUserDetails currentUser;
	private User user;
	private Book book;
	private Author author;
	private Publisher publisher;
	private Image image;
	private BookImage bookImage;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		UserTokenInfo userTokenInfo = new UserTokenInfo(1L, "password", Arrays.asList("HEAD_ADMIN"), "ACTIVE");
		currentUser = new CurrentUserDetails(userTokenInfo);
		user = mock(User.class);
		when(user.getId()).thenReturn(1L);

		author = mock(Author.class);
		publisher = mock(Publisher.class);

		book = mock(Book.class);
		when(book.getBookId()).thenReturn(1L);
		when(book.getBookTitle()).thenReturn("Book Title");
		when(book.getAuthor()).thenReturn(author);
		when(author.getAuthorName()).thenReturn("Author Name");
		when(book.getPublisher()).thenReturn(publisher);
		when(publisher.getPublisherName()).thenReturn("Publisher Name");
		when(book.getBookSalePrice()).thenReturn(BigDecimal.valueOf(100));
		when(book.getBookSalePercent()).thenReturn(BigDecimal.valueOf(10));

		image = mock(Image.class);
		when(image.getImageUrl()).thenReturn("http://example.com/image.jpg");

		bookImage = mock(BookImage.class);
		when(bookImage.getImage()).thenReturn(image);

		when(book.getBookImages()).thenReturn(Collections.singletonList(bookImage));

		wishList = mock(WishList.class);
		when(wishList.getWishListId()).thenReturn(1L);
		when(wishList.getBook()).thenReturn(book);
		when(wishList.getUser()).thenReturn(user);
	}

	@Test
	void testNoArgsConstructorIsProtected() {
		try {
			Constructor<Review> constructor = Review.class.getDeclaredConstructor();
			assertTrue(java.lang.reflect.Modifier.isProtected(constructor.getModifiers()),
				"Default constructor is not protected");
		} catch (NoSuchMethodException e) {
			fail("No default constructor found");
		}
		new Review();
	}

	@Test
	void testReviewBuilder() {
		int reviewScore = 5;
		String reviewComment = "Great book!";

		// When
		Review review = Review.builder()
			.reviewScore(reviewScore)
			.reviewComment(reviewComment)
			.book(book)
			.user(user)
			.build();

		assertThat(review.getReviewScore()).isEqualTo(reviewScore);
		assertThat(review.getReviewComment()).isEqualTo(reviewComment);
		assertThat(review.getBook()).isEqualTo(book);
		assertThat(review.getUser()).isEqualTo(user);
		assertNull(review.getReviewId(), "reviewId should be null upon creation");
	}

	@Test
	void testToEntity() {
		CreateReviewRequest request = new CreateReviewRequest(1L, 3, "test review", "reviewPhoto.png");

		Review review = Review.toEntity(request, book, user);

		assertThat(review.getReviewScore()).isEqualTo(request.reviewScore());
		assertThat(review.getReviewComment()).isEqualTo(request.reviewComment());
		assertThat(review.getBook()).isEqualTo(book);
		assertThat(review.getUser()).isEqualTo(user);
		assertThat(review.getReviewCreatedAt()).isBeforeOrEqualTo(LocalDateTime.now());
	}

	@Test
	void testUpdateReviewScore() {
		Review review = Review.builder()
			.reviewScore(3)
			.reviewComment("Good book.")
			.book(book)
			.user(user)
			.build();

		review.updateReviewScore(4, "Even better book!");

		assertThat(review.getReviewScore()).isEqualTo(4);
		assertThat(review.getReviewComment()).isEqualTo("Even better book!");
	}
}
