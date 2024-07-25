package com.nhnacademy.bookstoreback.order.service;

import java.math.BigDecimal;

import org.springframework.data.domain.Pageable;

import com.nhnacademy.bookstoreback.auth.annotation.CurrentUser;
import com.nhnacademy.bookstoreback.auth.jwt.dto.CurrentUserDetails;
import com.nhnacademy.bookstoreback.order.domain.dto.request.CreateOrderRequest;
import com.nhnacademy.bookstoreback.order.domain.dto.response.CreateCartOrderResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.CreateOrderResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetAllListOrderByStatusResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetAllListOrderResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetNonOrderByInfoResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetOrderByInfoResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetOrderByStatusIdResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetOrderResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetUserPointOrderResponse;

public interface OrderService {

	/**
	 * 주문 생성
	 * @param createOrderRequest 입력 받은 주문 정보
	 * @return 일부 주문 정보 리턴
	 */
	CreateOrderResponse createOrder(CreateOrderRequest createOrderRequest,
		@CurrentUser CurrentUserDetails currentUserDetails);

	/**
	 * 주문 가져오기
	 * @param orderId 주문 아이디로 가져오기
	 * @return 일부 주문 정보 리턴
	 */
	GetOrderResponse getOrder(Long orderId);

	/**
	 * 주문 상태 아이디를 지니고 있는 주문을 가져오기
	 * 페이징 처리 수정 예정
	 * @param orderStatusId 주문 상태 아이디
	 * @param pageable 페이징 처리
	 * @return 일부 주문 정보 리턴 및 페이지 정보 리턴
	 */
	GetOrderByStatusIdResponse findByOrderStatus_OrderStatusId(Long orderStatusId, Pageable pageable);

	/**
	 * 주문의 주문상태 업데이트
	 * @param orderId 주문 아이디
	 * @param orderStatusId 주문 상태 아이디
	 * @return 일부 주문 정보 리턴
	 */
	GetOrderResponse updateOrderStatus(Long orderId, Long orderStatusId);

	/**
	 * 카트 아이디로 모든 주문 가져오기
	 * 페이징 처리 예정
	 * @param userId 유저 아이디
	 * @return 일부 주문 정보를 가진 리스트 리턴
	 */
	GetAllListOrderResponse findAllByUserId(Long userId);

	/**
	 * 주문 보안 아이디로 주문 찾기
	 * @param orderInfoId 주문 보안 아이디
	 * @return 일부 주문 정보 리턴
	 */
	GetOrderByInfoResponse findByOrderInfoId(String orderInfoId);

	/**
	 * 로그인 된 회원 아이디로 모든 주문 가져오기
	 * @param currentUserDetails 로그인 된 회원 정보
	 * @return 일부 주문 정보 리스트 리턴
	 */
	GetAllListOrderResponse findAllUserId(@CurrentUser CurrentUserDetails currentUserDetails);

	/**
	 * 주문 상태가 대기인 모든 주문 가져오기
	 * @param orderStatusId 주문 상태 아이디
	 * @return 주문 상태가 대기인 주문 리턴
	 */
	GetAllListOrderByStatusResponse findByOrderStatus(Long orderStatusId);

	/**
	 * 비회원 주문 확인
	 * @param orderInfoId 주문 보안 아이디
	 * @param email 결제자 이메일
	 * @return 조회된 주문 정보
	 */
	GetNonOrderByInfoResponse findByOrderInfoIdByEmail(String orderInfoId, String email);

	/**
	 * 회원 포인트 가져오기
	 * @param currentUserDetails 로그인 된 회원
	 * @return 회원 보유 포인트
	 */
	GetUserPointOrderResponse getUserPoint(@CurrentUser CurrentUserDetails currentUserDetails);

	/**
	 * 반품
	 * @param orderInfoId 주문 보안 아이디
	 */
	void refundedOrder(String orderInfoId);

	/**
	 * 반품 요청중
	 * @param orderInfoId 주문 보안 아이디
	 */
	void refundingOrder(String orderInfoId);

	/**
	 * 회원의 총 주문 금액 조회
	 * @param currentUser 로그인 된 회원
	 * @return 회원 주문 금액 총합
	 */
	BigDecimal getTotalOrderPrice(CurrentUserDetails currentUser);

	/**
	 * 카트 주문 생성
	 * @param currentUser 로그인된 회원
	 * @return 주문 아이디
	 */
	CreateCartOrderResponse createCartOrder(@CurrentUser CurrentUserDetails currentUser);

	/**
	 * 카트 주문 주문 업데이트
	 * @param createOrderRequest 카트 주문 업데이트 내용
	 * @param orderId 주문 아이디
	 * @return 주문 완료 페이지 내용
	 */
	CreateOrderResponse updateCartOrder(CreateOrderRequest createOrderRequest, Long orderId);
}
