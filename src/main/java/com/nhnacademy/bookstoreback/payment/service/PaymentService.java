package com.nhnacademy.bookstoreback.payment.service;

import java.math.BigDecimal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nhnacademy.bookstoreback.auth.annotation.CurrentUser;
import com.nhnacademy.bookstoreback.auth.jwt.dto.CurrentUserDetails;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetBookOrderByInfoIdResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetOrderByInfoResponse;
import com.nhnacademy.bookstoreback.payment.dto.response.CancelResponse;
import com.nhnacademy.bookstoreback.payment.dto.response.PaymentResponse;
import com.nhnacademy.bookstoreback.payment.dto.response.PaymentSaveResponse;
import com.nhnacademy.bookstoreback.payment.dto.response.TransactionsResponse;
import com.nhnacademy.bookstoreback.payment.dto.response.UpdatePaymentResponse;
import com.nhnacademy.bookstoreback.user.domain.entity.User;

/**
 * @author 김다운
 * 결제와 관련된 서비스 인터페이스입니다.
 */
public interface PaymentService {
	/**
	 * 결제 생성
	 * @param paymentResponseJson 결제 요청으로 받은 Json 객체
	 * @return 페이먼츠 키 리턴
	 */
	PaymentSaveResponse savePaymentResponse(String paymentResponseJson, @CurrentUser CurrentUserDetails currentUser);

	/**
	 * 결제 조회
	 * @param paymentResponseJson 결제 조회로 받은 Json 객체
	 * @return 결제 정보 리턴
	 */
	TransactionsResponse transactions(String paymentResponseJson);

	/**
	 * 주문 보안 아이디로 주문 리스트 찾기
	 * @param orderInfoId 주문 보안 아이디
	 * @return 주문리스트 리턴
	 */
	GetBookOrderByInfoIdResponse findByOrderInfoId(String orderInfoId);

	/**
	 * 주문 보안 아이디로 주문 찾기
	 * @param orderInfoId 주문 보안 아이디
	 * @return 주문 정보 리턴
	 */
	GetOrderByInfoResponse findByOrder(String orderInfoId);

	/**
	 * 주문 보안 아이디 결제 취소 하기
	 * @param orderInfoId 주문 보안 아이디
	 * @return 결제 페이먼츠 키 , 결제 아이디 리턴
	 */
	CancelResponse paymentFindByOrderInfoId(String orderInfoId);

	/**
	 * 결제 업데이트
	 * @param paymentResponseJson  결제 취소로 받은 Json 객체
	 * @param paymentId 결제 아이디 리턴
	 * @return 결제 취소된 결제 정보 리턴
	 */
	UpdatePaymentResponse updatePayment(String paymentResponseJson, Long paymentId);

	/**
	 * 카트 주문 찾기
	 * @param orderInfoId 주문보안 아이디
	 * @return 책 이름
	 */
	GetBookOrderByInfoIdResponse findByCartOrderInfoId(String orderInfoId);

	/**
	 * 사용자 등급을 업데이트합니다.
	 *
	 * @param updatedOrderPrice 주문 가격
	 * @param user 사용자
	 */
	void updateUserGrade(BigDecimal updatedOrderPrice, User user);

	/**
	 * 결제 응답 JSON 문자열을 파싱하여 {@code PaymentResponse} 객체를 반환합니다.
	 *
	 * @param paymentResponseJson 결제 응답 JSON 문자열
	 * @return 파싱된 결제 응답
	 * @throws JsonProcessingException JSON 파싱 오류 발생 시
	 */
	PaymentResponse parsePaymentResponse(String paymentResponseJson) throws JsonProcessingException;

	/**
	 * 포인트 결제를 저장합니다.
	 *
	 * @param orderInfoId 주문 정보 ID
	 * @param currentUserDetails 현재 사용자 정보
	 */
	void savePointPayment(String orderInfoId, CurrentUserDetails currentUserDetails);

	/**
	 * 결제 정보를 업데이트합니다.
	 *
	 * @param paymentId 결제 ID
	 * @param currentUserDetails 현재 사용자 정보
	 */
	void updatePayment(Long paymentId, CurrentUserDetails currentUserDetails);

	/**
	 * 주문 정보 ID에 해당하는 결제 정보를 조회합니다.
	 *
	 * @param orderInfoId 주문 정보 ID
	 * @return 결제 응답
	 */
	PaymentResponse getPayment(String orderInfoId);
}
