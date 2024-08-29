package com.nhnacademy.bookstoreback.review.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

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
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.nhnacademy.bookstoreback.address.domain.entity.Address;
import com.nhnacademy.bookstoreback.auth.jwt.dto.CurrentUserDetails;
import com.nhnacademy.bookstoreback.book.domain.entity.Book;
import com.nhnacademy.bookstoreback.book.exception.BookNotFoundException;
import com.nhnacademy.bookstoreback.book.repository.BookRepository;
import com.nhnacademy.bookstoreback.global.exception.AccessDeniedException;
import com.nhnacademy.bookstoreback.image.domain.entity.Image;
import com.nhnacademy.bookstoreback.image.repository.ImageRepository;
import com.nhnacademy.bookstoreback.order.domain.entity.BookOrder;
import com.nhnacademy.bookstoreback.order.domain.entity.Order;
import com.nhnacademy.bookstoreback.order.repository.BookOrderRepository;
import com.nhnacademy.bookstoreback.review.domain.dto.request.CreateReviewRequest;
import com.nhnacademy.bookstoreback.review.domain.dto.request.UpdateReviewRequest;
import com.nhnacademy.bookstoreback.review.domain.dto.response.GetBookOrderWithoutReviewResponse;
import com.nhnacademy.bookstoreback.review.domain.dto.response.GetReviewResponse;
import com.nhnacademy.bookstoreback.review.domain.entity.Review;
import com.nhnacademy.bookstoreback.review.domain.entity.ReviewImage;
import com.nhnacademy.bookstoreback.review.exception.ReviewAlreadyExistsException;
import com.nhnacademy.bookstoreback.review.exception.ReviewNotFoundException;
import com.nhnacademy.bookstoreback.review.repository.ReviewImageRepository;
import com.nhnacademy.bookstoreback.review.repository.ReviewRepository;
import com.nhnacademy.bookstoreback.review.service.impl.ReviewServiceImpl;
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
	private BookOrderRepository bookOrderRepository;
	@Mock
	private UserRepository userRepository;
	@Mock
	private ImageRepository imageRepository;
	@Mock
	private ReviewImageRepository reviewImageRepository;

	@Mock
	private BookRepository bookRepository;

	@InjectMocks
	private ReviewServiceImpl reviewService;

	private CreateReviewRequest createReviewRequest;
	private CurrentUserDetails currentUser;
	private User user;
	private User anotherUser;
	private BookOrder bookOrder;
	private Review review;
	private Review review2;
	private ReviewImage reviewImage;
	private Image image;
	private GetReviewResponse getReviewResponse;
	Page<Review> reviewPage;
	private Page<GetReviewResponse> reviewResponsePage;

	@BeforeEach
	void setUp() {
		createReviewRequest = new CreateReviewRequest(1L, 5, "Great book!", null);
		currentUser = new CurrentUserDetails(new UserTokenInfo(1L, "password", Arrays.asList("HEAD_ADMIN"), "ACTIVE"));

		user = new User(1L, mock(UserGrade.class), new UserStatus("ACTIVE"),
			List.of(new UserRole(mock(User.class), new Role(1L, "HEAD_ADMIN"))),
			Collections.singletonList(mock(Address.class)), "John Doe", "user@example.com", "password123",
			LocalDate.of(1990, 1, 1), "123-456-7890", new BigDecimal("100.00"), "sso123", LocalDateTime.now(),
			LocalDateTime.now(), LocalDateTime.now());

		anotherUser = new User(2L, mock(UserGrade.class), new UserStatus("ACTIVE"),
			List.of(new UserRole(mock(User.class), new Role(1L, "HEAD_ADMIN"))),
			Collections.singletonList(mock(Address.class)), "John Doe", "user@example.com", "password123",
			LocalDate.of(1990, 1, 1), "123-456-7890", new BigDecimal("100.00"), "sso123", LocalDateTime.now(),
			LocalDateTime.now(), LocalDateTime.now());

		bookOrder = BookOrder.toEntity(3, mock(Book.class), mock(Order.class));
		review = new Review(5, "Great book!", bookOrder, user);
		review2 = new Review(2, "Great book!", bookOrder, user);
		reviewPage = new PageImpl<>(Collections.singletonList(review));
		image = new Image("ImageName", null);
		reviewImage = new ReviewImage(image, review);
		getReviewResponse = GetReviewResponse.fromEntity(review, reviewImage);
		reviewResponsePage = new PageImpl<>(Collections.singletonList(getReviewResponse));
	}

	@Test
	void testGetReviews() {
		Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "reviewCreatedAt");
		when(reviewRepository.findAll(pageable)).thenReturn(reviewPage);

		Page<GetReviewResponse> result = reviewService.getReviews(pageable);

		assertThat(result).isEqualTo(reviewResponsePage);
	}

	@Test
	void testGetPhotoReviews() {
		Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "reviewCreatedAt");
		when(reviewRepository.getPhotoReviews(pageable)).thenReturn(reviewResponsePage);

		Page<GetReviewResponse> result = reviewService.getPhotoReviews(pageable);

		assertThat(result).isEqualTo(reviewResponsePage);
	}

	@Test
	void testGetGeneralReviews() {
		Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "reviewCreatedAt");
		when(reviewRepository.getGeneralReviews(pageable)).thenReturn(reviewResponsePage);

		Page<GetReviewResponse> result = reviewService.getGeneralReviews(pageable);

		assertThat(result).isEqualTo(reviewResponsePage);
	}

	@Test
	void testGetReviewsByBookId() {
		Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "reviewCreatedAt");
		when(reviewRepository.getReviewsByBookId(1L, pageable)).thenReturn(reviewResponsePage);
		when(bookRepository.findById(anyLong())).thenReturn(Optional.of(mock(Book.class))); // 추가: bookRepository 모킹

		Page<GetReviewResponse> result = reviewService.getReviewsByBookId(1L, pageable);

		// 페이지의 내용만 비교합니다.
		assertThat(result.getContent()).isEqualTo(reviewResponsePage.getContent());

		// 페이지 메타데이터(페이지 번호, 페이지 크기 등)도 비교할 수 있습니다 (필요시).
		assertThat(result.getNumber()).isEqualTo(reviewResponsePage.getNumber());
		assertThat(result.getSize()).isEqualTo(reviewResponsePage.getSize());
		assertThat(result.getTotalPages()).isEqualTo(reviewResponsePage.getTotalPages());
		assertThat(result.getTotalElements()).isEqualTo(reviewResponsePage.getTotalElements());
	}

	@Test
	void testCreateReview() {
		// Given
		when(bookOrderRepository.findById(1L)).thenReturn(Optional.of(bookOrder));
		when(reviewRepository.existsByBookOrderOrderListId(1L)).thenReturn(false);
		when(userRepository.findById(1L)).thenReturn(Optional.of(user));
		when(reviewRepository.save(any(Review.class))).thenReturn(review);
		when(imageRepository.save(any(Image.class))).thenReturn(image);
		when(reviewImageRepository.save(any(ReviewImage.class))).thenReturn(reviewImage);

		// CreateReviewRequest with fileName set to trigger image saving
		CreateReviewRequest requestWithImage = new CreateReviewRequest(1L, 5, "Great book!",
			"http:test//23r234-2353253-2352_filename.jpg");

		// When
		reviewService.createReview(requestWithImage, currentUser);

		// Then
		verify(bookOrderRepository, times(1)).findById(1L);
		verify(reviewRepository, times(1)).existsByBookOrderOrderListId(1L);
		verify(userRepository, times(1)).findById(1L);
		verify(reviewRepository, times(1)).save(any(Review.class));
		verify(imageRepository, times(1)).save(any(Image.class)); // Ensure this is called
		verify(reviewImageRepository, times(1)).save(any(ReviewImage.class));
	}

	@Test
	void testCreateReviewThrowsExceptionWhenBookNotFound() {
		when(bookOrderRepository.findById(1L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> reviewService.createReview(createReviewRequest, currentUser))
			.isInstanceOf(BookNotFoundException.class);
	}

	@Test
	void testCreateReviewThrowsExceptionWhenReviewAlreadyExists() {
		when(bookOrderRepository.findById(1L)).thenReturn(Optional.of(bookOrder));
		when(reviewRepository.existsByBookOrderOrderListId(1L)).thenReturn(true);

		assertThatThrownBy(() -> reviewService.createReview(createReviewRequest, currentUser))
			.isInstanceOf(ReviewAlreadyExistsException.class);
	}

	// @Test
	// void testUpdateReview() {
	// 	// Given
	// 	UpdateReviewRequest updateReviewRequest = new UpdateReviewRequest(5, "Updated comment",
	// 		"http:test//23r234-2353253-2352_filename.jpg");
	//
	// 	// Mocking 사용자 및 리뷰 조회
	// 	when(userRepository.findById(1L)).thenReturn(Optional.of(user));
	// 	when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
	//
	// 	// Mocking 리뷰 및 이미지 저장
	// 	when(reviewRepository.save(any(Review.class))).thenReturn(review);
	// 	when(imageRepository.save(any(Image.class))).thenReturn(image);
	// 	when(reviewImageRepository.save(any(ReviewImage.class))).thenReturn(reviewImage);
	//
	// 	// When
	// 	reviewService.updateReview(1L, updateReviewRequest, currentUser);
	//
	// 	// Then
	// 	// 리뷰 이미지가 이전에 저장된 모든 이미지를 삭제하도록 검증
	// 	verify(reviewImageRepository, times(1)).deleteAllByReview_ReviewId(1L);
	//
	// 	// 리뷰 업데이트가 호출되었는지 검증
	// 	verify(reviewRepository, times(1)).save(any(Review.class));
	//
	// 	// 새로운 이미지 저장이 호출되었는지 검증
	// 	verify(imageRepository, times(1)).save(any(Image.class));
	//
	// 	// 새로운 리뷰 이미지 저장이 호출되었는지 검증
	// 	verify(reviewImageRepository, times(1)).save(any(ReviewImage.class));
	// }

	@Test
	void testUpdateReviewThrowsExceptionWhenUserNotFound() {
		UpdateReviewRequest updateReviewRequest = new UpdateReviewRequest(5, "Updated comment", "newFilename.jpg");

		when(userRepository.findById(1L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> reviewService.updateReview(1L, updateReviewRequest, currentUser))
			.isInstanceOf(UserNotFoundException.class);
	}

	@Test
	void testUpdateReviewThrowsExceptionWhenReviewNotFound() {
		UpdateReviewRequest updateReviewRequest = new UpdateReviewRequest(5, "Updated comment", "newFilename.jpg");

		when(userRepository.findById(1L)).thenReturn(Optional.of(user));
		when(reviewRepository.findById(1L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> reviewService.updateReview(1L, updateReviewRequest, currentUser))
			.isInstanceOf(ReviewNotFoundException.class);
	}

	@Test
	void testUpdateReviewThrowsExceptionWhenAccessDenied() {
		UpdateReviewRequest updateReviewRequest = new UpdateReviewRequest(5, "Updated comment", "newFilename.jpg");

		// Setting up the scenario where the current user is not the author of the review
		User differentUser = new User(3L, mock(UserGrade.class), new UserStatus("ACTIVE"),
			List.of(new UserRole(mock(User.class), new Role(1L, "HEAD_ADMIN"))),
			Collections.singletonList(mock(Address.class)), "Jane Doe", "jane@example.com", "password456",
			LocalDate.of(1990, 1, 1), "123-456-7890", new BigDecimal("100.00"), "sso456", LocalDateTime.now(),
			LocalDateTime.now(), LocalDateTime.now());
		Review anotherReview = new Review(5, "Another review", bookOrder, differentUser);

		when(userRepository.findById(1L)).thenReturn(Optional.of(user));
		when(reviewRepository.findById(1L)).thenReturn(Optional.of(anotherReview));

		assertThatThrownBy(() -> reviewService.updateReview(1L, updateReviewRequest, currentUser))
			.isInstanceOf(AccessDeniedException.class);
	}

	@Test
	void testDeleteReview() {
		reviewService.deleteReview(1L);

		verify(reviewRepository, times(1)).deleteById(1L);
		verify(reviewImageRepository, times(1)).deleteAllByReview_ReviewId(1L);
	}

	@Test
	void testFindReviewByIdThrowsExceptionWhenReviewNotFound() {
		when(reviewRepository.findById(1L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> reviewService.getReview(1L))
			.isInstanceOf(ReviewNotFoundException.class);
	}

	@Test
	void testGetReviewsByUserId() {
		Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "reviewCreatedAt");
		when(reviewRepository.getReviewsByUserId(1L, pageable)).thenReturn(reviewResponsePage);

		Page<GetReviewResponse> result = reviewService.getReviewsByUserId(pageable, currentUser);

		// 페이지의 내용만 비교합니다.
		assertThat(result.getContent()).isEqualTo(reviewResponsePage.getContent());

		// 페이지 메타데이터(페이지 번호, 페이지 크기 등)도 비교할 수 있습니다 (필요시).
		assertThat(result.getNumber()).isEqualTo(reviewResponsePage.getNumber());
		assertThat(result.getSize()).isEqualTo(reviewResponsePage.getSize());
		assertThat(result.getTotalPages()).isEqualTo(reviewResponsePage.getTotalPages());
		assertThat(result.getTotalElements()).isEqualTo(reviewResponsePage.getTotalElements());
	}

	@Test
	void testGetGeneralReviewsByUserId() {
		Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "reviewCreatedAt");
		when(reviewRepository.getGeneralReviewsByUserId(1L, pageable)).thenReturn(reviewResponsePage);

		Page<GetReviewResponse> result = reviewService.getGeneralReviewsByUserId(pageable, currentUser);

		assertThat(result).isEqualTo(reviewResponsePage);
	}

	@Test
	void testGetPhotoReviewsByUserId() {
		Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "reviewCreatedAt");
		when(reviewRepository.getPhotoReviewsByUserId(1L, pageable)).thenReturn(reviewResponsePage);

		Page<GetReviewResponse> result = reviewService.getPhotoReviewsByUserId(pageable, currentUser);

		assertThat(result).isEqualTo(reviewResponsePage);
	}

	// @Test
	// void testGetReviewsAverageScoreByBookId_ReviewsExist() {
	// 	// Create a mock Book object
	// 	Book book = mock(Book.class);
	// 	when(book.getBookId()).thenReturn(1L);
	//
	// 	// Create a mock BookOrder object
	// 	BookOrder bookOrder = mock(BookOrder.class);
	// 	when(bookOrder.getBook()).thenReturn(book);
	//
	// 	// Create mock Review objects
	// 	Review review1 = mock(Review.class);
	// 	when(review1.getReviewScore()).thenReturn(3);
	// 	when(review1.getBookOrder()).thenReturn(bookOrder);
	//
	// 	Review review2 = mock(Review.class);
	// 	when(review2.getReviewScore()).thenReturn(4);
	// 	when(review2.getBookOrder()).thenReturn(bookOrder);
	//
	// 	// List of reviews to return
	// 	List<Review> reviews = Arrays.asList(review1, review2);
	//
	// 	// Create a Page object containing the reviews
	// 	Pageable pageable = PageRequest.of(0, 10);
	// 	Page<Review> reviewPage = new PageImpl<>(reviews, pageable, reviews.size());
	//
	// 	// Mock the repository methods
	// 	when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
	// 	when(reviewRepository.findAllByBookOrderBookBookId(1L, pageable)).thenReturn(reviewPage);
	//
	// 	// When
	// 	double averageScore = reviewService.getReviewsAverageScoreByBookId(1L);
	//
	// 	// Then
	// 	assertThat(averageScore).isEqualTo(3.5);
	// }

	@Test
	void testGetBooksWithoutReviews() {
		// User 정보가 필요할 수 있음
		when(userRepository.findById(1L)).thenReturn(Optional.of(user));

		// When
		List<GetBookOrderWithoutReviewResponse> result = reviewService.getBooksWithoutReviewsByUserId(currentUser);

		// Then
		assertThat(result).isEmpty();
	}
}