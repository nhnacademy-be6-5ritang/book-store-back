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
import com.nhnacademy.bookstoreback.payment.dto.response.PaymentResponse;
import com.nhnacademy.bookstoreback.payment.dto.response.PaymentSaveResponse;
import com.nhnacademy.bookstoreback.payment.dto.response.TransactionsResponse;
import com.nhnacademy.bookstoreback.payment.dto.response.UpdatePaymentResponse;
import com.nhnacademy.bookstoreback.payment.service.Impl.PaymentServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * @author 김다운
 * 결제 관련 HTTP 요청을 처리하는 컨트롤러입니다.
 */
@Tag(name = "Payment", description = "결제 API")
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
	@Operation(
		summary = "주문 보안 아이디로 주문 리스트 가져오기",
		description = "주문 보안 아이디로 주문리스트를 가져옵니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "404", description = "주문리스트를 찾을 수 없습니다.")
	})
	@GetMapping("/books-orders/{order_info_id}")
	public ResponseEntity<GetBookOrderByInfoIdResponse> bookOrder(@PathVariable("order_info_id") String orderInfoId) {
		return ResponseEntity.status(HttpStatus.OK).body(paymentServiceImpl.findByCartOrderInfoId(orderInfoId));
	}

	/**
	 * 주문 보안 아이디로 주문 가져오기
	 * @param orderInfoId 주문 보안 아이디
	 * @return 주문 정보
	 */
	@Operation(
		summary = "주문 보안 아이디로 주문 가져오기",
		description = "주문 보안 아이디로 주문을 가져옵니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "404", description = "주문리스트를 찾을 수 없습니다.")
	})
	@GetMapping("/order-info/{order_info_id}")
	public ResponseEntity<GetOrderByInfoResponse> findByOrderInfoId(@PathVariable("order_info_id") String orderInfoId) {
		return ResponseEntity.status(HttpStatus.OK).body(paymentServiceImpl.findByOrder(orderInfoId));
	}

	/**
	 * 결제 생성
	 * @param paymentResponseJson 결제 요청으로 받은 Json 객체
	 * @return 페이먼츠 키 리턴
	 */
	@Operation(
		summary = "결제 정보 저장하기",
		description = "토스 페이먼츠 결제 완료 후 Payment 객체 파싱 후 데이터를 저장하고 ."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "404", description = "주문 정보를 찾을 수 없습니다."),
		@ApiResponse(responseCode = "500", description = "Json 파싱 실패")
	})
	@PostMapping
	public ResponseEntity<PaymentSaveResponse> savePayment(@RequestBody String paymentResponseJson,
		@CurrentUser CurrentUserDetails currentUser) {
		return ResponseEntity.status(HttpStatus.OK)
			.body(paymentServiceImpl.savePaymentResponse(paymentResponseJson, currentUser));
	}

	/**
	 * 결제 조회
	 * @param paymentResponseJson 결제 조회로 받은 Json 객체
	 * @return 결제 정보 리턴
	 */
	@Operation(
		summary = "거래 조회",
		description = "토스 페이먼츠 거래 조회"
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "500", description = "Json 파싱 실패")
	})
	@PostMapping("/transactions")
	public ResponseEntity<TransactionsResponse> transactions(@RequestBody String paymentResponseJson) {
		return ResponseEntity.status(HttpStatus.OK).body(paymentServiceImpl.transactions(paymentResponseJson));
	}

	/**
	 * 주문 보안 아이디 결제 취소 하기
	 * @param orderInfoId 주문 보안 아이디
	 * @return 결제 페이먼츠 키 , 결제 아이디 리턴
	 */
	@Operation(
		summary = "거래 취소",
		description = "토스 페이먼츠 주문 보안 아이디로 거래 취소"
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "404", description = "거래 정보를 찾을 수 없습니다")
	})
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
	@Operation(
		summary = "거래 정보 업데이트",
		description = "토스 페이먼츠 거래 취소 상태 업데이트"
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "404", description = "거래 정보를 찾을 수 없습니다")
	})
	@PostMapping("/cancel/{payment_id}")
	public ResponseEntity<UpdatePaymentResponse> cancel(@RequestBody String paymentResponseJson,
		@PathVariable("payment_id") Long paymentId) {
		return ResponseEntity.status(HttpStatus.OK)
			.body(paymentServiceImpl.updatePayment(paymentResponseJson, paymentId));
	}

	@Operation(
		summary = "거래 정보 업데이트",
		description = "포인트 결제 취소"
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "404", description = "거래 정보를 찾을 수 없습니다")
	})
	@GetMapping("/cancel/pointSale/{payment_id}")
	public ResponseEntity<Void> cancelPointSale(@PathVariable("payment_id") Long paymentId,
		@CurrentUser CurrentUserDetails currentUser) {
		paymentServiceImpl.updatePayment(paymentId, currentUser);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@Operation(
		summary = "거래 정보 생성",
		description = "포인트 결제 생성"
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "404", description = "거래 정보를 찾을 수 없습니다")
	})
	@GetMapping("/pointSale/{orderInfoId}")
	public ResponseEntity<Void> pointSale(@PathVariable String orderInfoId,
		@CurrentUser CurrentUserDetails currentUser) {
		paymentServiceImpl.savePointPayment(orderInfoId, currentUser);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@Operation(
		summary = "결제 정보 조회",
		description = "주문 보안 아이디로 결제 정보 조회"
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "404", description = "거래 정보를 찾을 수 없습니다")
	})
	@GetMapping("/pointSale/info/{orderInfoId}")
	public ResponseEntity<PaymentResponse> pointSaleInfo(@PathVariable String orderInfoId) {
		return ResponseEntity.status(HttpStatus.OK)
			.body(paymentServiceImpl.getPayment(orderInfoId));
	}
}
