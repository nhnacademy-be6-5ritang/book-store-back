package com.nhnacademy.bookstoreback.review.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.nhnacademy.bookstoreback.address.domain.entity.Address;
import com.nhnacademy.bookstoreback.auth.jwt.dto.CurrentUserDetails;
import com.nhnacademy.bookstoreback.book.domain.entity.Book;
import com.nhnacademy.bookstoreback.book.exception.BookNotFoundException;
import com.nhnacademy.bookstoreback.book.repository.BookRepository;
import com.nhnacademy.bookstoreback.image.domain.entity.Image;
import com.nhnacademy.bookstoreback.image.repository.ImageRepository;
import com.nhnacademy.bookstoreback.review.domain.dto.request.CreateReviewRequest;
import com.nhnacademy.bookstoreback.review.domain.dto.response.GetReviewResponse;
import com.nhnacademy.bookstoreback.review.domain.entity.Review;
import com.nhnacademy.bookstoreback.review.domain.entity.ReviewImage;
import com.nhnacademy.bookstoreback.review.repository.ReviewImageRepository;
import com.nhnacademy.bookstoreback.review.repository.ReviewRepository;
import com.nhnacademy.bookstoreback.role.domain.entity.Role;
import com.nhnacademy.bookstoreback.user.domain.dto.response.UserTokenInfo;
import com.nhnacademy.bookstoreback.user.domain.entity.User;
import com.nhnacademy.bookstoreback.user.exception.UserNotFoundException;
import com.nhnacademy.bookstoreback.user.repository.UserRepository;
import com.nhnacademy.bookstoreback.usergrade.domain.entity.UserGrade;
import com.nhnacademy.bookstoreback.userrole.domain.entity.UserRole;
import com.nhnacademy.bookstoreback.userstatus.domain.entity.UserStatus;

@ExtendWith(MockitoExtension.class)
class ReviewServiceImplTest {

	@Mock
	private ReviewRepository reviewRepository;
	@Mock
	private BookRepository bookRepository;
	@Mock
	private UserRepository userRepository;
	@Mock
	private ImageRepository imageRepository;
	@Mock
	private ReviewImageRepository reviewImageRepository;

	@InjectMocks
	private ReviewServiceImpl reviewService;

	private CreateReviewRequest createReviewRequest;
	private CurrentUserDetails currentUser;
	private User user;
	private Book book;
	private Review review;
	private ReviewImage reviewImage;
	private Image image;
	private GetReviewResponse getReviewResponse;
	private Page<GetReviewResponse> reviewPage;

	@BeforeEach
	void setUp() {
		createReviewRequest = new CreateReviewRequest(1L, 5, "Great book!", "filename.jpg");
		UserTokenInfo userTokenInfo = new UserTokenInfo(1L, "password", Arrays.asList("HEAD_ADMIN"), "ACTIVE");
		currentUser = new CurrentUserDetails(userTokenInfo);

		user = new User(1L, mock(UserGrade.class), new UserStatus("ACTIVE"),
			List.of(new UserRole(mock(User.class), new Role(1L, "HEAD_ADMIN"))),
			Collections.singletonList(mock(Address.class)), "John Doe", "user@example.com", "password123",
			LocalDate.of(1990, 1, 1),
			"123-456-7890", new BigDecimal("100.00"), "sso123", LocalDateTime.now(), LocalDateTime.now(),
			LocalDateTime.now());
		book = new Book();
		review = new Review(5, "Great book!", book, user);
		image = new Image("ImageName", "filename.jpg");
		reviewImage = new ReviewImage(image, review);
		getReviewResponse = GetReviewResponse.fromEntity(review, reviewImage);
		reviewPage = new PageImpl<>(Collections.singletonList(getReviewResponse));
	}

	@Test
	void testFindAllReviews() {
		PageRequest pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "reviewCreatedAt"));
		Page<Review> reviewPage = new PageImpl<>(List.of(review), pageable, 1);

		given(reviewRepository.findAll(pageable)).willReturn(reviewPage);
		given(reviewImageRepository.findByReviewReviewId(1L)).willReturn(reviewImage);

		Page<GetReviewResponse> result = reviewService.findAllReviews(pageable);

		assertNotNull(result);
		assertEquals(1, result.getTotalElements());
		assertEquals(getReviewResponse, result.getContent().get(0));
		verify(reviewRepository, times(1)).findAll(pageable);
		verify(reviewImageRepository, times(1)).findByReviewReviewId(1L);
	}

	@Test
	void testCreateReview() {
		given(bookRepository.findById(anyLong())).willReturn(Optional.of(book));
		given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
		given(reviewRepository.save(any(Review.class))).willReturn(review);
		given(imageRepository.save(any(Image.class))).willReturn(image);
		given(reviewImageRepository.save(any(ReviewImage.class))).willReturn(reviewImage);

		reviewService.createReview(createReviewRequest, currentUser);

		verify(bookRepository, times(1)).findById(anyLong());
		verify(userRepository, times(1)).findById(anyLong());
		verify(reviewRepository, times(1)).save(any(Review.class));
		verify(imageRepository, times(1)).save(any(Image.class));
		verify(reviewImageRepository, times(1)).save(any(ReviewImage.class));
	}

	@Test
	void testCreateReviewBookNotFoundException() {
		given(bookRepository.findById(anyLong())).willReturn(Optional.empty());

		assertThrows(BookNotFoundException.class, () -> reviewService.createReview(createReviewRequest, currentUser));

		verify(bookRepository, times(1)).findById(anyLong());
		verify(userRepository, times(0)).findById(anyLong());
		verify(reviewRepository, times(0)).save(any(Review.class));
		verify(imageRepository, times(0)).save(any(Image.class));
		verify(reviewImageRepository, times(0)).save(any(ReviewImage.class));
	}

	@Test
	void testCreateReviewUserNotFoundException() {
		given(bookRepository.findById(anyLong())).willReturn(Optional.of(book));
		given(userRepository.findById(anyLong())).willReturn(Optional.empty());

		assertThrows(UserNotFoundException.class, () -> reviewService.createReview(createReviewRequest, currentUser));

		verify(bookRepository, times(1)).findById(anyLong());
		verify(userRepository, times(1)).findById(anyLong());
		verify(reviewRepository, times(0)).save(any(Review.class));
		verify(imageRepository, times(0)).save(any(Image.class));
		verify(reviewImageRepository, times(0)).save(any(ReviewImage.class));
	}
}