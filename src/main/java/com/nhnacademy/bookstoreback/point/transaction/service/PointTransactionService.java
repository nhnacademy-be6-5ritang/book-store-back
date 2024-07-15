package com.nhnacademy.bookstoreback.point.transaction.service;

import java.math.BigDecimal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nhnacademy.bookstoreback.auth.jwt.dto.CurrentUserDetails;
import com.nhnacademy.bookstoreback.point.transaction.domain.dto.request.CreatePointTransactionRequest;
import com.nhnacademy.bookstoreback.point.transaction.domain.dto.response.CreatePointTransactionResponse;
import com.nhnacademy.bookstoreback.point.transaction.domain.dto.response.GetAllPointTransactionResponse;
import com.nhnacademy.bookstoreback.point.transaction.domain.dto.response.GetPointTransactionResponse;
import com.nhnacademy.bookstoreback.user.domain.entity.User;

public interface PointTransactionService {
	CreatePointTransactionResponse createPointTransaction(
		CurrentUserDetails currentUser, CreatePointTransactionRequest createPointTransactionRequest
	);

	Page<GetPointTransactionResponse> getPointTransactions(CurrentUserDetails currentUser, Pageable pageable);

	void signUpPointTransaction(User user);

	// 이 밑에 포인트 거래 생성 메서드들은 @CurrentUser로 수정해도 된다.

	GetPointTransactionResponse reviewPointTransaction(CurrentUserDetails currentUser, String reviewType);

	/**
	 * 주문 시 주문자의 회원 등급에 따른 포인트 거래 생성
	 * @param user 주문자
	 * @param totalPrice 주문 총 가격
	 *
	 * @author 김태환
	 */
	void orderPointTransaction(User user, BigDecimal totalPrice);

	/**
	 * 관리자용 모든 포인트 거래 정보
	 * @param pageable 페이징 처리
	 * @return 모든 포인트 거래 정보
	 */
	Page<GetAllPointTransactionResponse> getAllPointTransaction(Pageable pageable);
}
