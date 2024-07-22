package com.nhnacademy.bookstoreback.point;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.nhnacademy.bookstoreback.auth.jwt.dto.CurrentUserDetails;
import com.nhnacademy.bookstoreback.point.earningpolicy.domain.entity.PointEarningPolicy;
import com.nhnacademy.bookstoreback.point.earningpolicy.exception.PointEarningPolicyNotFoundException;
import com.nhnacademy.bookstoreback.point.earningpolicy.repository.PointEarningPolicyRepository;
import com.nhnacademy.bookstoreback.point.transaction.domain.dto.request.CreatePointTransactionRequest;
import com.nhnacademy.bookstoreback.point.transaction.domain.dto.response.CreatePointTransactionResponse;
import com.nhnacademy.bookstoreback.point.transaction.domain.dto.response.GetAllPointTransactionResponse;
import com.nhnacademy.bookstoreback.point.transaction.domain.dto.response.GetPointTransactionResponse;
import com.nhnacademy.bookstoreback.point.transaction.domain.entity.PointTransaction;
import com.nhnacademy.bookstoreback.point.transaction.repository.PointTransactionRepository;
import com.nhnacademy.bookstoreback.point.transaction.service.impl.PointTransactionServiceImpl;
import com.nhnacademy.bookstoreback.user.domain.entity.User;
import com.nhnacademy.bookstoreback.user.exception.UserNotFoundException;
import com.nhnacademy.bookstoreback.user.repository.UserRepository;
import com.nhnacademy.bookstoreback.usergrade.domain.entity.UserGrade;

class PointTransactionServiceImplTest {

	@InjectMocks
	private PointTransactionServiceImpl pointTransactionService;

	@Mock
	private PointTransactionRepository pointTransactionRepository;

	@Mock
	private PointEarningPolicyRepository pointEarningPolicyRepository;

	@Mock
	private UserRepository userRepository;

	private User user;
	private PointEarningPolicy pointEarningPolicy;
	private PointTransaction pointTransaction;
	private PointEarningPolicy reviewPolicy;
	private PointEarningPolicy photoReviewPolicy;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		setUpCommonEntities();
	}

	private void setUpCommonEntities() {
		user = User.builder()
			.id(1L)
			.points(BigDecimal.ZERO)
			.userGrade(new UserGrade("userGradeName", new BigDecimal(1), new BigDecimal(1), new BigDecimal(1)))
			.build();

		pointEarningPolicy = PointEarningPolicy.builder()
			.pointEarningAmount(BigDecimal.TEN)
			.pointEarningPolicyType("REVIEW")
			.build();

		pointTransaction = PointTransaction.builder()
			.user(user)
			.pointEarningPolicy(pointEarningPolicy)
			.pointTransactionAmount(BigDecimal.TEN)
			.build();

		reviewPolicy = PointEarningPolicy.builder()
			.pointEarningAmount(BigDecimal.TEN)
			.pointEarningPolicyType("REVIEW")
			.build();

		photoReviewPolicy = PointEarningPolicy.builder()
			.pointEarningAmount(BigDecimal.TEN)
			.pointEarningPolicyType("PHOTO_REVIEW")
			.build();
	}

	private CurrentUserDetails mockCurrentUser(Long userId) {
		CurrentUserDetails currentUser = mock(CurrentUserDetails.class);
		when(currentUser.getUserId()).thenReturn(userId);
		return currentUser;
	}

	@Test
	void createPointTransaction_ValidRequest_CreatesTransaction() {
		CreatePointTransactionRequest request = new CreatePointTransactionRequest(
			pointEarningPolicy.getId(), BigDecimal.TEN);

		when(pointEarningPolicyRepository.findById(pointEarningPolicy.getId()))
			.thenReturn(Optional.of(pointEarningPolicy));
		when(userRepository.findById(user.getId()))
			.thenReturn(Optional.of(user));
		when(pointTransactionRepository.save(any(PointTransaction.class)))
			.thenReturn(pointTransaction);

		CreatePointTransactionResponse response = pointTransactionService.createPointTransaction(
			mockCurrentUser(user.getId()), request);

		assertEquals(pointTransaction.getPointTransactionAmount(), response.pointTransactionAmount());
		assertEquals(pointTransaction.getPointEarningPolicy().getId(), response.pointEarningPolicyId());
	}

	@Test
	void getPointTransactions_ReturnsPageOfTransactions() {
		Pageable pageable = PageRequest.of(0, 10);
		Page<PointTransaction> pointTransactionPage = new PageImpl<>(List.of(pointTransaction));

		when(pointTransactionRepository.findAllByUserId(eq(user.getId()), any(Pageable.class)))
			.thenReturn(pointTransactionPage);

		Page<GetPointTransactionResponse> responsePage = pointTransactionService.getPointTransactions(
			mockCurrentUser(user.getId()), pageable);

		assertEquals(1, responsePage.getTotalElements());
		assertEquals(pointTransaction.getPointTransactionAmount(),
			responsePage.getContent().get(0).pointTransactionAmount());
	}

	@Test
	void signUpPointTransaction_ValidUser_CreatesTransaction() {
		when(pointEarningPolicyRepository.findByPointEarningPolicyType("SIGN_UP"))
			.thenReturn(Optional.of(pointEarningPolicy));

		PointTransaction savedTransaction = PointTransaction.builder()
			.user(user)
			.pointEarningPolicy(pointEarningPolicy)
			.pointTransactionAmount(pointEarningPolicy.getPointEarningAmount())
			.build();
		when(pointTransactionRepository.save(any(PointTransaction.class)))
			.thenReturn(savedTransaction);

		pointTransactionService.signUpPointTransaction(user);

		verify(pointTransactionRepository).save(any(PointTransaction.class));
		verify(userRepository).save(user);
	}

	@Test
	void orderPointTransaction_RegularUser_CreatesTransaction() {
		User regularUser = User.builder()
			.id(2L)
			.points(BigDecimal.ZERO)
			.userGrade(new UserGrade("REGULAR", BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE))
			.build();

		PointEarningPolicy regularPolicy = PointEarningPolicy.builder()
			.pointEarningAmount(BigDecimal.valueOf(0.05))
			.pointEarningPolicyType("ORDER")
			.build();

		when(pointEarningPolicyRepository.findByPointEarningPolicyType("ORDER"))
			.thenReturn(Optional.of(regularPolicy));
		when(pointTransactionRepository.save(any(PointTransaction.class)))
			.thenAnswer(invocation -> invocation.getArgument(0));

		BigDecimal totalPrice = BigDecimal.valueOf(100);
		pointTransactionService.orderPointTransaction(regularUser, totalPrice);

		verify(pointTransactionRepository).save(any(PointTransaction.class));
		verify(userRepository).save(regularUser);
		assertEquals(totalPrice.multiply(regularPolicy.getPointEarningAmount()), regularUser.getPoints());
	}

	@Test
	void orderPointTransaction_RoyalUser_CreatesTransaction() {
		User royalUser = User.builder()
			.id(3L)
			.points(BigDecimal.ZERO)
			.userGrade(new UserGrade("ROYAL", BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE))
			.build();

		PointEarningPolicy royalPolicy = PointEarningPolicy.builder()
			.pointEarningAmount(BigDecimal.valueOf(0.07))
			.pointEarningPolicyType("ORDER")
			.build();

		when(pointEarningPolicyRepository.findByPointEarningPolicyType("ORDER"))
			.thenReturn(Optional.of(royalPolicy));
		when(pointTransactionRepository.save(any(PointTransaction.class)))
			.thenAnswer(invocation -> invocation.getArgument(0));

		BigDecimal totalPrice = BigDecimal.valueOf(100);
		pointTransactionService.orderPointTransaction(royalUser, totalPrice);

		verify(pointTransactionRepository).save(any(PointTransaction.class));
		verify(userRepository).save(royalUser);
		assertEquals(totalPrice.multiply(royalPolicy.getPointEarningAmount()), royalUser.getPoints());
	}

	@Test
	void orderPointTransaction_GrandUser_CreatesTransaction() {
		User grandUser = User.builder()
			.id(4L)
			.points(BigDecimal.ZERO)
			.userGrade(new UserGrade("GRAND", BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE))
			.build();

		PointEarningPolicy grandPolicy = PointEarningPolicy.builder()
			.pointEarningAmount(BigDecimal.valueOf(0.1))
			.pointEarningPolicyType("ORDER")
			.build();

		when(pointEarningPolicyRepository.findByPointEarningPolicyType("ORDER"))
			.thenReturn(Optional.of(grandPolicy));
		when(pointTransactionRepository.save(any(PointTransaction.class)))
			.thenAnswer(invocation -> invocation.getArgument(0));

		BigDecimal totalPrice = BigDecimal.valueOf(100);
		pointTransactionService.orderPointTransaction(grandUser, totalPrice);

		verify(pointTransactionRepository).save(any(PointTransaction.class));
		verify(userRepository).save(grandUser);
		assertEquals(totalPrice.multiply(grandPolicy.getPointEarningAmount()), grandUser.getPoints());
	}

	@Test
	void orderPointTransaction_PrestigeUser_CreatesTransaction() {
		User prestigeUser = User.builder()
			.id(5L)
			.points(BigDecimal.ZERO)
			.userGrade(new UserGrade("PRESTIGE", BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE))
			.build();

		PointEarningPolicy prestigePolicy = PointEarningPolicy.builder()
			.pointEarningAmount(BigDecimal.valueOf(0.15))
			.pointEarningPolicyType("ORDER")
			.build();

		when(pointEarningPolicyRepository.findByPointEarningPolicyType("ORDER"))
			.thenReturn(Optional.of(prestigePolicy));
		when(pointTransactionRepository.save(any(PointTransaction.class)))
			.thenAnswer(invocation -> invocation.getArgument(0));

		BigDecimal totalPrice = BigDecimal.valueOf(100);
		pointTransactionService.orderPointTransaction(prestigeUser, totalPrice);

		verify(pointTransactionRepository).save(any(PointTransaction.class));
		verify(userRepository).save(prestigeUser);
		assertEquals(totalPrice.multiply(prestigePolicy.getPointEarningAmount()), prestigeUser.getPoints());
	}

	@Test
	void orderPointTransaction_GradeNotFound_ThrowsException() {
		User userWithUnknownGrade = User.builder()
			.id(6L)
			.points(BigDecimal.ZERO)
			.userGrade(new UserGrade("UNKNOWN", BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE))
			.build();

		when(pointEarningPolicyRepository.findByPointEarningPolicyType("ORDER"))
			.thenReturn(Optional.empty());

		BigDecimal totalPrice = BigDecimal.valueOf(100);

		assertThrows(PointEarningPolicyNotFoundException.class, () ->
			pointTransactionService.orderPointTransaction(userWithUnknownGrade, totalPrice)
		);
	}

	@Test
	void getAllPointTransaction_ReturnsPageOfTransactions() {
		Pageable pageable = PageRequest.of(0, 10);
		Page<PointTransaction> pointTransactionPage = new PageImpl<>(List.of(pointTransaction));

		when(pointTransactionRepository.findAll(any(Pageable.class)))
			.thenReturn(pointTransactionPage);

		Page<GetAllPointTransactionResponse> responsePage = pointTransactionService.getAllPointTransaction(pageable);

		assertEquals(1, responsePage.getTotalElements());
		assertEquals(pointTransaction.getPointTransactionAmount(),
			responsePage.getContent().get(0).amount());
	}

	@Test
	void refundPointTransaction_ValidRequest_CreatesRefundTransaction() {
		PointEarningPolicy refundPolicy = new PointEarningPolicy(BigDecimal.TEN, "반품");
		when(pointEarningPolicyRepository.findByPointEarningPolicyType("반품"))
			.thenReturn(Optional.of(refundPolicy));

		BigDecimal totalPrice = BigDecimal.valueOf(50);
		PointTransaction refundTransaction = PointTransaction.builder()
			.user(user)
			.pointEarningPolicy(refundPolicy)
			.pointTransactionAmount(totalPrice)
			.build();
		when(pointTransactionRepository.save(any(PointTransaction.class)))
			.thenReturn(refundTransaction);

		pointTransactionService.refundPointTransaction(user, totalPrice);

		verify(pointTransactionRepository).save(any(PointTransaction.class));
		verify(userRepository).save(user);
	}

	@Test
	void createPointTransaction_PolicyNotFound_ThrowsException() {
		CreatePointTransactionRequest request = new CreatePointTransactionRequest(
			pointEarningPolicy.getId(), BigDecimal.TEN);

		when(pointEarningPolicyRepository.findById(pointEarningPolicy.getId()))
			.thenReturn(Optional.empty());

		assertThrows(PointEarningPolicyNotFoundException.class, () ->
			pointTransactionService.createPointTransaction(mockCurrentUser(user.getId()), request));
	}

	@Test
	void createPointTransaction_UserNotFound_ThrowsException() {
		CreatePointTransactionRequest request = new CreatePointTransactionRequest(
			pointEarningPolicy.getId(), BigDecimal.TEN);

		when(pointEarningPolicyRepository.findById(pointEarningPolicy.getId()))
			.thenReturn(Optional.of(pointEarningPolicy));
		when(userRepository.findById(anyLong()))
			.thenReturn(Optional.empty());

		assertThrows(UserNotFoundException.class, () ->
			pointTransactionService.createPointTransaction(mockCurrentUser(user.getId()), request));
	}

	@Test
	void signUpPointTransaction_PolicyNotFound_ThrowsException() {
		when(pointEarningPolicyRepository.findByPointEarningPolicyType("SIGN_UP"))
			.thenReturn(Optional.empty());

		assertThrows(PointEarningPolicyNotFoundException.class, () ->
			pointTransactionService.signUpPointTransaction(user));
	}

	@Test
	void reviewPointTransaction_UserNotFound_ThrowsException() {
		CurrentUserDetails currentUserDetails = mock(CurrentUserDetails.class);
		when(currentUserDetails.getUserId()).thenReturn(1L);

		when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

		assertThrows(UserNotFoundException.class, () ->
			pointTransactionService.reviewPointTransaction(currentUserDetails, "REVIEW")
		);
	}

	@Test
	void refundPointTransaction_PolicyNotFound_ThrowsException() {
		when(pointEarningPolicyRepository.findByPointEarningPolicyType("반품"))
			.thenReturn(Optional.empty());

		assertThrows(PointEarningPolicyNotFoundException.class, () ->
			pointTransactionService.refundPointTransaction(user, BigDecimal.TEN));
	}

	@Test
	void reviewPointTransaction_ReviewTypeIsReview_CreatesTransaction() {
		CurrentUserDetails currentUser = mockCurrentUser(user.getId());

		when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
		when(pointEarningPolicyRepository.findByPointEarningPolicyType("REVIEW"))
			.thenReturn(Optional.of(reviewPolicy));
		when(pointTransactionRepository.save(any(PointTransaction.class))).thenAnswer(
			invocation -> invocation.getArgument(0));

		pointTransactionService.reviewPointTransaction(currentUser, "REVIEW");

		verify(pointTransactionRepository).save(any(PointTransaction.class));
		verify(userRepository).save(user);
		assertEquals(reviewPolicy.getPointEarningAmount(), user.getPoints());
	}

	@Test
	void reviewPointTransaction_ReviewTypeIsPhotoReview_CreatesTransaction() {
		CurrentUserDetails currentUser = mockCurrentUser(user.getId());

		when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
		when(pointEarningPolicyRepository.findByPointEarningPolicyType("PHOTO_REVIEW"))
			.thenReturn(Optional.of(photoReviewPolicy));
		when(pointTransactionRepository.save(any(PointTransaction.class))).thenAnswer(
			invocation -> invocation.getArgument(0));

		pointTransactionService.reviewPointTransaction(currentUser, "PHOTO_REVIEW");

		verify(pointTransactionRepository).save(any(PointTransaction.class));
		verify(userRepository).save(user);
		assertEquals(photoReviewPolicy.getPointEarningAmount(), user.getPoints());
	}

	@Test
	void reviewPointTransaction_PointEarningPolicyNotFound_ThrowsException() {
		CurrentUserDetails currentUser = mockCurrentUser(user.getId());

		when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
		when(pointEarningPolicyRepository.findByPointEarningPolicyType("REVIEW"))
			.thenReturn(Optional.empty());

		assertThrows(PointEarningPolicyNotFoundException.class, () ->
			pointTransactionService.reviewPointTransaction(currentUser, "REVIEW")
		);
	}

	@Test
	void reviewPointTransaction_InvalidReviewType_ThrowsException() {
		CurrentUserDetails currentUser = mockCurrentUser(user.getId());

		when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
		when(pointEarningPolicyRepository.findByPointEarningPolicyType("INVALID_TYPE"))
			.thenReturn(Optional.empty());

		assertThrows(PointEarningPolicyNotFoundException.class, () ->
			pointTransactionService.reviewPointTransaction(currentUser, "INVALID_TYPE")
		);
	}
}
