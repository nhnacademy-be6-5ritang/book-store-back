package com.nhnacademy.bookstoreback.point;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
import com.nhnacademy.bookstoreback.userstatus.domain.entity.UserStatus;

public class PointTransactionServiceImplTest {

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

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);

		user = mock(User.class);

		pointEarningPolicy = new PointEarningPolicy(BigDecimal.TEN, "REVIEW");

		pointTransaction = PointTransaction.builder()
			.user(user)
			.pointEarningPolicy(pointEarningPolicy)
			.pointTransactionAmount(BigDecimal.TEN)
			.build();
	}

	@Test
	void createPointTransaction_ShouldCreateTransaction_WhenValidRequest() {
		CurrentUserDetails currentUser = mock(CurrentUserDetails.class);

		CreatePointTransactionRequest request = new CreatePointTransactionRequest(
			pointEarningPolicy.getId(), BigDecimal.TEN);

		when(pointEarningPolicyRepository.findById(pointEarningPolicy.getId()))
			.thenReturn(Optional.of(pointEarningPolicy));
		when(userRepository.findById(user.getId()))
			.thenReturn(Optional.of(user));
		when(pointTransactionRepository.save(any(PointTransaction.class)))
			.thenReturn(pointTransaction);

		CreatePointTransactionResponse response = pointTransactionService.createPointTransaction(
			currentUser, request);

		assertEquals(pointTransaction.getPointTransactionAmount(), response.pointTransactionAmount());
		assertEquals(pointTransaction.getPointEarningPolicy().getId(), response.pointEarningPolicyId());
	}

	@Test
	void getPointTransactions_ShouldReturnPageOfTransactions() {
		Pageable pageable = PageRequest.of(0, 10);
		Page<PointTransaction> pointTransactionPage = new PageImpl<>(List.of(pointTransaction));

		when(pointTransactionRepository.findAllByUserId(eq(user.getId()), any(Pageable.class)))
			.thenReturn(pointTransactionPage);

		Page<GetPointTransactionResponse> responsePage = pointTransactionService.getPointTransactions(
			mock(CurrentUserDetails.class), pageable);

		assertEquals(1, responsePage.getTotalElements());
		assertEquals(pointTransaction.getPointTransactionAmount(),
			responsePage.getContent().get(0).pointTransactionAmount());
	}

	@Test
	void signUpPointTransaction_ShouldCreateTransaction_WhenUserIsValid() {
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
	void reviewPointTransaction_ShouldCreateTransaction_WhenReviewTypeIsValid() {
		// Set up UserGrade
		UserGrade userGrade = UserGrade.builder()
			.userGradeName("REGULAR")
			.userGradeMinAmount(BigDecimal.ZERO)
			.userGradeMaxAmount(BigDecimal.valueOf(1000))
			.userGradePointRate(BigDecimal.valueOf(0.1))
			.build();

		// Set up UserStatus
		UserStatus userStatus = new UserStatus("ACTIVE");

		// Set up User
		user = User.builder()
			.id(1L)
			.userGrade(userGrade)
			.status(userStatus)
			.name("John Doe")
			.email("john.doe@example.com")
			.password("password")
			.birth(LocalDate.of(1990, 1, 1))
			.contact("123-456-7890")
			.points(BigDecimal.ZERO)
			.ssoId("sso-id-123")
			.createdAt(LocalDateTime.now())
			.updatedAt(LocalDateTime.now())
			.lastLoginAt(LocalDateTime.now())
			.build();

		// Set up PointEarningPolicy
		pointEarningPolicy = PointEarningPolicy.builder()
			.pointEarningAmount(BigDecimal.TEN)
			.pointEarningPolicyType("REVIEW")
			.build();

		pointTransaction = PointTransaction.builder()
			.user(user)
			.pointEarningPolicy(pointEarningPolicy)
			.pointTransactionAmount(BigDecimal.TEN)
			.build();

		// Mock repository methods
		when(pointEarningPolicyRepository.findByPointEarningPolicyType("REVIEW"))
			.thenReturn(Optional.of(pointEarningPolicy));
		when(pointTransactionRepository.save(any(PointTransaction.class)))
			.thenReturn(pointTransaction);
	}

	@Test
	void orderPointTransaction_ShouldCreateTransaction_WhenOrderIsPlaced() {
		UserGrade userGrade = UserGrade.builder()
			.userGradeName("REGULAR")
			.userGradeMinAmount(BigDecimal.ZERO)
			.userGradeMaxAmount(BigDecimal.valueOf(1000))
			.userGradePointRate(BigDecimal.valueOf(0.1))
			.build();
		// Set up UserStatus
		UserStatus userStatus = new UserStatus("ACTIVE");

		// Set up User
		user = User.builder()
			.id(1L)
			.userGrade(userGrade)
			.status(userStatus)
			.name("John Doe")
			.email("john.doe@example.com")
			.password("password")
			.birth(LocalDate.of(1990, 1, 1))
			.contact("123-456-7890")
			.points(BigDecimal.ZERO)
			.ssoId("sso-id-123")
			.createdAt(LocalDateTime.now())
			.updatedAt(LocalDateTime.now())
			.lastLoginAt(LocalDateTime.now())
			.build();
		when(pointEarningPolicyRepository.findByPointEarningPolicyType("ORDER"))
			.thenReturn(Optional.of(pointEarningPolicy));

		BigDecimal totalPrice = BigDecimal.valueOf(100);
		BigDecimal expectedPointAmount = totalPrice.multiply(pointEarningPolicy.getPointEarningAmount());

		PointTransaction savedTransaction = PointTransaction.builder()
			.user(user)
			.pointEarningPolicy(pointEarningPolicy)
			.pointTransactionAmount(expectedPointAmount)
			.build();
		when(pointTransactionRepository.save(any(PointTransaction.class)))
			.thenReturn(savedTransaction);

		pointTransactionService.orderPointTransaction(user, totalPrice);

		verify(pointTransactionRepository).save(any(PointTransaction.class));
		verify(userRepository).save(user);
	}

	@Test
	void getAllPointTransaction_ShouldReturnPageOfTransactions() {
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
	void refundPointTransaction_ShouldCreateRefundTransaction() {
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
	void createPointTransaction_ShouldThrowException_WhenPolicyNotFound() {
		CreatePointTransactionRequest request = new CreatePointTransactionRequest(
			pointEarningPolicy.getId(), BigDecimal.TEN);

		when(pointEarningPolicyRepository.findById(pointEarningPolicy.getId()))
			.thenReturn(Optional.empty());

		assertThrows(PointEarningPolicyNotFoundException.class, () ->
			pointTransactionService.createPointTransaction(mock(CurrentUserDetails.class), request));
	}

	@Test
	void createPointTransaction_ShouldThrowException_WhenUserNotFound() {
		CreatePointTransactionRequest request = new CreatePointTransactionRequest(
			pointEarningPolicy.getId(), BigDecimal.TEN);

		when(pointEarningPolicyRepository.findById(pointEarningPolicy.getId()))
			.thenReturn(Optional.of(pointEarningPolicy));
		when(userRepository.findById(anyLong()))
			.thenReturn(Optional.empty());

		assertThrows(UserNotFoundException.class, () ->
			pointTransactionService.createPointTransaction(mock(CurrentUserDetails.class), request));
	}

	@Test
	void signUpPointTransaction_ShouldThrowException_WhenPolicyNotFound() {
		when(pointEarningPolicyRepository.findByPointEarningPolicyType("SIGN_UP"))
			.thenReturn(Optional.empty());

		assertThrows(PointEarningPolicyNotFoundException.class, () ->
			pointTransactionService.signUpPointTransaction(user));
	}

	@Test
	void reviewPointTransaction_ShouldThrowException_WhenUserNotFound() {
		// Given
		CurrentUserDetails currentUserDetails = mock(CurrentUserDetails.class);
		when(currentUserDetails.getUserId()).thenReturn(1L); // 적절한 사용자 ID 설정

		// When & Then
		when(pointEarningPolicyRepository.findByPointEarningPolicyType(anyString()))
			.thenReturn(Optional.empty()); // 정책을 찾지 못함

		assertThrows(UserNotFoundException.class, () ->
			pointTransactionService.reviewPointTransaction(currentUserDetails, "REVIEW")
		);
	}

	@Test
	void refundPointTransaction_ShouldThrowException_WhenPolicyNotFound() {
		when(pointEarningPolicyRepository.findByPointEarningPolicyType("반품"))
			.thenReturn(Optional.empty());

		assertThrows(PointEarningPolicyNotFoundException.class, () ->
			pointTransactionService.refundPointTransaction(user, BigDecimal.TEN));
	}

}
