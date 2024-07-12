package com.nhnacademy.bookstoreback.point.transaction.service.impl;

import java.math.BigDecimal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nhnacademy.bookstoreback.auth.annotation.CurrentUser;
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
import com.nhnacademy.bookstoreback.point.transaction.service.PointTransactionService;
import com.nhnacademy.bookstoreback.user.domain.entity.User;
import com.nhnacademy.bookstoreback.user.exception.UserNotFoundException;
import com.nhnacademy.bookstoreback.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PointTransactionServiceImpl implements PointTransactionService {
	private final PointTransactionRepository pointTransactionRepository;
	private final PointEarningPolicyRepository pointEarningPolicyRepository;
	private final UserRepository userRepository;

	@Override
	public CreatePointTransactionResponse createPointTransaction(
		CurrentUserDetails currentUser,
		CreatePointTransactionRequest createPointTransactionRequest
	) {
		PointEarningPolicy pointEarningPolicy = pointEarningPolicyRepository.findById(
			createPointTransactionRequest.pointEarningPolicyId()
		).orElseThrow(() -> new PointEarningPolicyNotFoundException(
			createPointTransactionRequest.pointEarningPolicyId()
		));

		User user = userRepository.findById(currentUser.getUserId())
			.orElseThrow(() -> new UserNotFoundException(currentUser.getUserId()));

		PointTransaction pointTransaction = PointTransaction.toEntity(user, pointEarningPolicy,
			createPointTransactionRequest);
		PointTransaction savedPointTransaction = pointTransactionRepository.save(pointTransaction);

		return CreatePointTransactionResponse.fromEntity(savedPointTransaction);
	}

	@Override
	public Page<GetPointTransactionResponse> getPointTransactions(@CurrentUser CurrentUserDetails currentUser,
		Pageable pageable) {
		int page = pageable.getPageNumber() > 0 ? pageable.getPageNumber() - 1 : 0;
		int size = pageable.isPaged() && pageable.getPageSize() > 0 ? pageable.getPageSize() : 10;

		Page<PointTransaction> pointTransactionPage = pointTransactionRepository.findAllByUserId(
			currentUser.getUserId(), PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "pointTransactionDate"))
		);

		return pointTransactionPage.map(GetPointTransactionResponse::fromEntity);
	}

	@Override
	public void signUpPointTransaction(User user) {
		PointEarningPolicy pointEarningPolicy = pointEarningPolicyRepository.findByPointEarningPolicyType("SIGN_UP")
			.orElseThrow(() -> new PointEarningPolicyNotFoundException("SIGN_UP"));

		PointTransaction pointTransaction = PointTransaction.builder()
			.user(user)
			.pointEarningPolicy(pointEarningPolicy)
			.pointTransactionAmount(pointEarningPolicy.getPointEarningAmount())
			.build();

		pointTransactionRepository.save(pointTransaction);

		user.updatePoints(pointEarningPolicy.getPointEarningAmount());

		userRepository.save(user);
	}

	@Override
	public void reviewPointTransaction(User user) {
		PointEarningPolicy pointEarningPolicy = pointEarningPolicyRepository.findByPointEarningPolicyType("REVIEW")
			.orElseThrow(() -> new PointEarningPolicyNotFoundException("REVIEW"));

		PointTransaction pointTransaction = PointTransaction.builder()
			.user(user)
			.pointEarningPolicy(pointEarningPolicy)
			.pointTransactionAmount(pointEarningPolicy.getPointEarningAmount())
			.build();

		pointTransactionRepository.save(pointTransaction);

		user.updatePoints(pointEarningPolicy.getPointEarningAmount());

		userRepository.save(user);
	}

	@Override
	public void photoReviewPointTransaction(User user) {
		PointEarningPolicy pointEarningPolicy = pointEarningPolicyRepository.findByPointEarningPolicyType(
				"PHOTO_REVIEW")
			.orElseThrow(() -> new PointEarningPolicyNotFoundException("PHOTO_REVIEW"));

		PointTransaction pointTransaction = PointTransaction.builder()
			.user(user)
			.pointEarningPolicy(pointEarningPolicy)
			.pointTransactionAmount(pointEarningPolicy.getPointEarningAmount())
			.build();

		pointTransactionRepository.save(pointTransaction);

		user.updatePoints(pointEarningPolicy.getPointEarningAmount());

		userRepository.save(user);
	}

	@Override
	public void orderPointTransaction(User user, BigDecimal totalPrice) {
		PointEarningPolicy pointEarningPolicy = switch (user.getUserGrade().getUserGradeName()) {
			case "REGULAR" -> pointEarningPolicyRepository.findByPointEarningPolicyType("ORDER")
				.orElseThrow(() -> new PointEarningPolicyNotFoundException("ORDER_REGULAR"));
			case "ROYAL" -> pointEarningPolicyRepository.findByPointEarningPolicyType("ORDER")
				.orElseThrow(() -> new PointEarningPolicyNotFoundException("ORDER_ROYAL"));
			case "GRAND" -> pointEarningPolicyRepository.findByPointEarningPolicyType("ORDER")
				.orElseThrow(() -> new PointEarningPolicyNotFoundException("ORDER_GRAND"));
			case "PRESTIGE" -> pointEarningPolicyRepository.findByPointEarningPolicyType("ORDER")
				.orElseThrow(() -> new PointEarningPolicyNotFoundException("ORDER_PRESTIGE"));
			case null, default -> pointEarningPolicyRepository.findByPointEarningPolicyType("ORDER")
				.orElseThrow(() -> new PointEarningPolicyNotFoundException("ORDER_GRADE"));
		};

		BigDecimal pointTransactionAmount = totalPrice.multiply(pointEarningPolicy.getPointEarningAmount());
		PointTransaction pointTransaction = PointTransaction.builder()
			.user(user)
			.pointEarningPolicy(pointEarningPolicy)
			.pointTransactionAmount(pointTransactionAmount)
			.build();

		pointTransactionRepository.save(pointTransaction);

		user.updatePoints(pointTransactionAmount);

		userRepository.save(user);
	}

	@Override
	public Page<GetAllPointTransactionResponse> getAllPointTransaction(Pageable pageable) {
		int page = pageable.getPageNumber() > 0 ? pageable.getPageNumber() - 1 : 0;
		int size = pageable.isPaged() && pageable.getPageSize() > 0 ? pageable.getPageSize() : 10;

		Page<PointTransaction> pointTransactionPage = pointTransactionRepository.findAll(
			PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "pointTransactionDate")));
		return pointTransactionPage.map(GetAllPointTransactionResponse::fromEntity);
	}

	public void refundPointTransaction(User user, BigDecimal totalPrice) {
		PointEarningPolicy pointEarningPolicy = pointEarningPolicyRepository.findByPointEarningPolicyType(
				"반품")
			.orElseThrow(() -> new PointEarningPolicyNotFoundException("반품"));
		PointTransaction pointTransaction = PointTransaction.builder()
			.user(user)
			.pointEarningPolicy(pointEarningPolicy)
			.pointTransactionAmount(totalPrice)
			.build();
		pointTransactionRepository.save(pointTransaction);
		user.updatePoints(totalPrice);
		userRepository.save(user);
	}
}
