package com.nhnacademy.bookstoreback.payment.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nhnacademy.bookstoreback.auth.annotation.CurrentUser;
import com.nhnacademy.bookstoreback.auth.jwt.dto.CurrentUserDetails;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetBookOrderByInfoIdResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetOrderByInfoResponse;
import com.nhnacademy.bookstoreback.payment.dto.response.CancelResponse;
import com.nhnacademy.bookstoreback.payment.dto.response.PaymentSaveResponse;
import com.nhnacademy.bookstoreback.payment.dto.response.TransactionsResponse;
import com.nhnacademy.bookstoreback.payment.dto.response.UpdatePaymentResponse;
import com.nhnacademy.bookstoreback.payment.service.Impl.PaymentServiceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

	private final PaymentServiceImpl paymentServiceImpl;

	/**
	 * 주문 보안 아이디로 주문 리스트 가져오기
	 * @param orderInfoId 주문 보안 아이디
	 * @return 주문 리스트 리턴
	 */
	@GetMapping("/books-orders/{order_info_id}")
	public ResponseEntity<GetBookOrderByInfoIdResponse> bookOrder(@PathVariable("order_info_id") String orderInfoId) {
		return ResponseEntity.status(HttpStatus.OK).body(paymentServiceImpl.findByOrderInfoId(orderInfoId));
	}

	/**
	 * 주문 보안 아이디로 주문 가져오기
	 * @param orderInfoId 주문 보안 아이디
	 * @return 주문 정보
	 */
	@GetMapping("/order-info/{order_info_id}")
	public ResponseEntity<GetOrderByInfoResponse> findByOrderInfoId(@PathVariable("order_info_id") String orderInfoId) {
		return ResponseEntity.status(HttpStatus.OK).body(paymentServiceImpl.findByOrder(orderInfoId));
	}

	/**
	 * 결제 생성
	 * @param paymentResponseJson 결제 요청으로 받은 Json 객체
	 * @return 페이먼츠 키 리턴
	 */
	@PostMapping
	public ResponseEntity<PaymentSaveResponse> savePayment(@RequestBody String paymentResponseJson, @CurrentUser
	CurrentUserDetails currentUser) {
		return ResponseEntity.status(HttpStatus.OK)
			.body(paymentServiceImpl.savePaymentResponse(paymentResponseJson, currentUser));
	}

	/**
	 * 결제 조회
	 * @param paymentResponseJson 결제 조회로 받은 Json 객체
	 * @return 결제 정보 리턴
	 */
	@PostMapping("/transactions")
	public ResponseEntity<TransactionsResponse> transactions(@RequestBody String paymentResponseJson) {
		return ResponseEntity.status(HttpStatus.OK).body(paymentServiceImpl.transactions(paymentResponseJson));
	}

	/**
	 * 주문 보안 아이디 결제 취소 하기
	 * @param orderInfoId 주문 보안 아이디
	 * @return 결제 페이먼츠 키 , 결제 아이디 리턴
	 */
	@GetMapping("/cancel/{order_info_id}")
	public ResponseEntity<CancelResponse> cancel(@PathVariable("order_info_id") String orderInfoId) {
		return ResponseEntity.status(HttpStatus.OK).body(paymentServiceImpl.paymentFindByOrderInfoId(orderInfoId));
	}

	/**
	 * 결제 업데이트
	 * @param paymentResponseJson  결제 취소로 받은 Json 객체
	 * @param paymentId 결제 아이디 리턴
	 * @return 결제 취소된 결제 정보 리턴
	 */
	@PostMapping("/cancel/test/{payment_id}")
	public ResponseEntity<UpdatePaymentResponse> cancel(@RequestBody String paymentResponseJson,
		@PathVariable("payment_id") Long paymentId) {
		return ResponseEntity.status(HttpStatus.OK)
			.body(paymentServiceImpl.updatePayment(paymentResponseJson, paymentId));
	}

}
