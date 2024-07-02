package com.nhnacademy.bookstoreback.payment.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nhnacademy.bookstoreback.order.domain.dto.response.GetBookOrderByInfoIdResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetOrderByInfoResponse;
import com.nhnacademy.bookstoreback.payment.dto.response.PaymentSaveResponse;
import com.nhnacademy.bookstoreback.payment.dto.response.TransactionsResponse;
import com.nhnacademy.bookstoreback.payment.service.PaymentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

	private final PaymentService paymentService;

	//주문 보안 아이디로 주문리스트 가져오기
	@GetMapping("/books-orders/{order_info_id}")
	public ResponseEntity<GetBookOrderByInfoIdResponse> bookOrder(@PathVariable("order_info_id") String orderInfoId) {
		return ResponseEntity.status(HttpStatus.OK).body(paymentService.findByOrderInfoId(orderInfoId));
	}

	@GetMapping("/order-info/{order_info_id}")
	public ResponseEntity<GetOrderByInfoResponse> findByOrderInfoId(@PathVariable("order_info_id") String orderInfoId) {
		return ResponseEntity.status(HttpStatus.OK).body(paymentService.findByOrder(orderInfoId));
	}

	@PostMapping
	public ResponseEntity<PaymentSaveResponse> savePayment(@RequestBody String paymentResponseJson) {
		return ResponseEntity.status(HttpStatus.OK).body(paymentService.savePaymentResponse(paymentResponseJson));
	}

	@PostMapping("/transactions")
	public ResponseEntity<TransactionsResponse> transactions(@RequestBody String paymentResponseJson) {
		return ResponseEntity.status(HttpStatus.OK).body(paymentService.transactions(paymentResponseJson));
	}
}
