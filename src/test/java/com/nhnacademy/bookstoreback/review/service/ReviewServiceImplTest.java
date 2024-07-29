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

import com.nhnacademy.bookstoreback.address.domain.entity.Address;
import com.nhnacademy.bookstoreback.auth.jwt.dto.CurrentUserDetails;
import com.nhnacademy.bookstoreback.book.domain.entity.Book;
import com.nhnacademy.bookstoreback.book.exception.BookNotFoundException;
import com.nhnacademy.bookstoreback.image.domain.entity.Image;
import com.nhnacademy.bookstoreback.image.repository.ImageRepository;
import com.nhnacademy.bookstoreback.order.domain.entity.BookOrder;
import com.nhnacademy.bookstoreback.order.domain.entity.Order;
import com.nhnacademy.bookstoreback.order.repository.BookOrderRepository;
import com.nhnacademy.bookstoreback.review.domain.dto.request.CreateReviewRequest;
import com.nhnacademy.bookstoreback.review.domain.dto.request.UpdateReviewRequest;
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
		createReviewRequest = new CreateReviewRequest(1L, 5, "Great book!", "filename.jpg");
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
		image = new Image("ImageName", "filename.jpg");
		reviewImage = new ReviewImage(image, review);
		getReviewResponse = GetReviewResponse.fromEntity(review, reviewImage);
		reviewResponsePage = new PageImpl<>(Collections.singletonList(getReviewResponse));
	}

	// @Test
	// void testGetReviews() {
	// 	Pageable pageable = PageRequest.of(0, 10);
	// 	when(reviewRepository.findAll(pageable)).thenReturn(reviewPage);
	//
	// 	Page<GetReviewResponse> result = reviewService.getReviews(pageable);
	//
	// 	assertThat(result).isEqualTo(reviewResponsePage);
	// }
	//
	// @Test
	// void testGetPhotoReviews() {
	// 	Pageable pageable = PageRequest.of(0, 10);
	// 	when(reviewRepository.findAllByReviewImagesNotEmpty(pageable)).thenReturn(reviewPage);
	//
	// 	Page<GetReviewResponse> result = reviewService.getPhotoReviews(pageable);
	//
	// 	assertThat(result).isEqualTo(reviewResponsePage);
	// }
	//
	// @Test
	// void testGetGeneralReviews() {
	// 	Pageable pageable = PageRequest.of(0, 10);
	// 	when(reviewRepository.findAllByReviewImagesEmpty(pageable)).thenReturn(reviewPage);
	//
	// 	Page<GetReviewResponse> result = reviewService.getGeneralReviews(pageable);
	//
	// 	assertThat(result).isEqualTo(reviewResponsePage);
	// }
	//
	// @Test
	// void testGetReviewsByBookId() {
	// 	Pageable pageable = PageRequest.of(0, 10);
	// 	when(reviewRepository.findAllByBookOrderBookBookId(1L, pageable)).thenReturn(reviewPage);
	//
	// 	Page<GetReviewResponse> result = reviewService.getReviewsByBookId(1L, pageable);
	//
	// 	assertThat(result).isEqualTo(reviewResponsePage);
	// }
	//
	// @Test
	// void testCreateReview() {
	// 	when(bookOrderRepository.findById(1L)).thenReturn(Optional.of(bookOrder));
	// 	when(reviewRepository.existsByBookOrderOrderListId(1L)).thenReturn(false);
	// 	when(userRepository.findById(1L)).thenReturn(Optional.of(user));
	// 	when(reviewRepository.save(any(Review.class))).thenReturn(review);
	// 	when(imageRepository.save(any(Image.class))).thenReturn(image);
	// 	when(reviewImageRepository.save(any(ReviewImage.class))).thenReturn(reviewImage);
	//
	// 	reviewService.createReview(createReviewRequest, currentUser);
	//
	// 	verify(reviewRepository, times(1)).save(any(Review.class));
	// 	verify(imageRepository, times(1)).save(any(Image.class));
	// 	verify(reviewImageRepository, times(1)).save(any(ReviewImage.class));
	// }

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
	// 	UpdateReviewRequest updateReviewRequest = new UpdateReviewRequest(5, "Updated comment", "newFilename.jpg");
	//
	// 	when(userRepository.findById(1L)).thenReturn(Optional.of(user));
	// 	when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
	// 	when(imageRepository.save(any(Image.class))).thenReturn(image);
	//
	// 	reviewService.updateReview(1L, updateReviewRequest, currentUser);
	//
	// 	verify(reviewImageRepository, times(1)).deleteAllByReview_ReviewId(1L);
	// 	verify(reviewRepository, times(1)).save(any(Review.class));
	// 	verify(imageRepository, times(1)).save(any(Image.class));
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

	// @Test
	// void testUpdateReviewThrowsExceptionWhenAccessDenied() {
	// 	UpdateReviewRequest updateReviewRequest = new UpdateReviewRequest(5, "Updated comment", "newFilename.jpg");
	//
	// 	when(userRepository.findById(1L)).thenReturn(Optional.of(user));
	// 	when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
	//
	// 	assertThatThrownBy(() -> reviewService.updateReview(1L, updateReviewRequest, currentUser))
	// 		.isInstanceOf(AccessDeniedException.class);
	// }

	@Test
	void testDeleteReview() {
		reviewService.deleteReview(1L);

		verify(reviewRepository, times(1)).deleteById(1L);
		verify(reviewImageRepository, times(1)).deleteAllByReview_ReviewId(1L);
	}

	// @Test
	// void testFindReviewById() {
	// 	when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
	// 	when(reviewImageRepository.findByReviewReviewId(1L)).thenReturn(reviewImage);
	//
	// 	GetReviewResponse result = reviewService.findReviewById(1L);
	//
	// 	assertThat(result).isEqualTo(getReviewResponse);
	// }

	@Test
	void testFindReviewByIdThrowsExceptionWhenReviewNotFound() {
		when(reviewRepository.findById(1L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> reviewService.findReviewById(1L))
			.isInstanceOf(ReviewNotFoundException.class);
	}

	// @Test
	// void testGetReviewsByUserId() {
	// 	Pageable pageable = PageRequest.of(0, 10);
	// 	when(reviewRepository.findAllByUserId(1L, pageable)).thenReturn(reviewPage);
	//
	// 	Page<GetReviewResponse> result = reviewService.getReviewsByUserId(pageable, currentUser);
	//
	// 	assertThat(result).isEqualTo(reviewResponsePage);
	// }
	//
	// @Test
	// void testGetGeneralReviewsByUserId() {
	// 	Pageable pageable = PageRequest.of(0, 10);
	// 	when(reviewRepository.findAllByUserIdAndReviewImagesEmpty(1L, pageable)).thenReturn(reviewPage);
	//
	// 	Page<GetReviewResponse> result = reviewService.getGeneralReviewsByUserId(pageable, currentUser);
	//
	// 	assertThat(result).isEqualTo(reviewResponsePage);
	// }
	//
	// @Test
	// void testGetPhotoReviewsByUserId() {
	// 	Pageable pageable = PageRequest.of(0, 10);
	// 	when(reviewRepository.findAllByUserIdAndReviewImagesNotEmpty(1L, pageable)).thenReturn(reviewPage);
	//
	// 	Page<GetReviewResponse> result = reviewService.getPhotoReviewsByUserId(pageable, currentUser);
	//
	// 	assertThat(result).isEqualTo(reviewResponsePage);
	// }
	//
	// @Test
	// void testGetReviewsAverageScoreByBookId_ReviewsExist() {
	// 	when(reviewRepository.findAll()).thenReturn(Arrays.asList(review, review2));
	//
	// 	double averageScore = reviewService.getReviewsAverageScoreByBookId(1L);
	//
	// 	assertThat(averageScore).isEqualTo(3.5);
	// }
	//
	// @Test
	// void testGetBooksWithoutReviews() {
	// 	List<BookOrder> bookOrders = Arrays.asList(bookOrder);
	// 	List<Review> reviews = Arrays.asList(review);
	//
	// 	when(bookOrderRepository.findAllByOrder_User_IdAndOrder_OrderStatus_OrderStatusName(1L, "배송 완료")).thenReturn(
	// 		bookOrders);
	// 	when(reviewRepository.findAllByUserId(1L)).thenReturn(reviews);
	//
	// 	List<GetBookOrderWithoutReviewResponse> result = reviewService.getBooksWithoutReviews(currentUser);
	//
	// 	assertThat(result).isNotEmpty();
	// }
}
