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

/**
 * @author 김태환
 * 포인트 거래 관련 서비스를 제공하는 인터페이스입니다.
 */
public interface PointTransactionService {
	/**
	 * 포인트 거래를 생성합니다.
	 *
	 * @param currentUser 현재 사용자의 상세 정보
	 * @param createPointTransactionRequest 생성할 포인트 거래 정보
	 * @return 생성된 포인트 거래 정보
	 */
	CreatePointTransactionResponse createPointTransaction(
		CurrentUserDetails currentUser, CreatePointTransactionRequest createPointTransactionRequest
	);

	/**
	 * 현재 사용자의 포인트 거래 내역을 페이징하여 조회합니다.
	 *
	 * @param currentUser 현재 사용자의 상세 정보
	 * @param pageable 페이징 정보
	 * @return 현재 사용자의 포인트 거래 내역 페이지
	 */
	Page<GetPointTransactionResponse> getPointTransactions(CurrentUserDetails currentUser, Pageable pageable);

	/**
	 * 회원 가입 시 초기 포인트 거래를 생성합니다.
	 *
	 * @param user 초기 포인트 거래를 생성할 사용자
	 */
	void signUpPointTransaction(User user);

	/**
	 * 현재 사용자에게 해당 리뷰 타입에 따른 포인트 거래를 처리합니다.
	 *
	 * @param currentUser 현재 사용자의 상세 정보.
	 * @param reviewType 리뷰 타입입니다. 포인트 적립 정책을 결정합니다.
	 */
	void reviewPointTransaction(CurrentUserDetails currentUser, String reviewType);

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
