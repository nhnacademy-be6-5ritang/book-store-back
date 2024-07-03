package com.nhnacademy.bookstoreback.payment.service;

import com.nhnacademy.bookstoreback.order.domain.dto.response.GetBookOrderByInfoIdResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetOrderByInfoResponse;
import com.nhnacademy.bookstoreback.payment.dto.response.CancelResponse;
import com.nhnacademy.bookstoreback.payment.dto.response.PaymentSaveResponse;
import com.nhnacademy.bookstoreback.payment.dto.response.TransactionsResponse;
import com.nhnacademy.bookstoreback.payment.dto.response.UpdatePaymentResponse;

public interface PaymentService {
	/**
	 * 결제 생성
	 * @param paymentResponseJson 결제 요청으로 받은 Json 객체
	 * @return 페이먼츠 키 리턴
	 */
	PaymentSaveResponse savePaymentResponse(String paymentResponseJson);

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
}
