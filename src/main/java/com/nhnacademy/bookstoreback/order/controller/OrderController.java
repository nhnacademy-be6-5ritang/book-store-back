package com.nhnacademy.bookstoreback.order.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nhnacademy.bookstoreback.auth.annotation.AuthorizeRole;
import com.nhnacademy.bookstoreback.auth.annotation.CurrentUser;
import com.nhnacademy.bookstoreback.auth.jwt.dto.CurrentUserDetails;
import com.nhnacademy.bookstoreback.book.service.impl.BookServiceImpl;
import com.nhnacademy.bookstoreback.order.domain.dto.request.CreateBookOrderRequest;
import com.nhnacademy.bookstoreback.order.domain.dto.request.CreateOrderRequest;
import com.nhnacademy.bookstoreback.order.domain.dto.request.CreateOrderStatusRequest;
import com.nhnacademy.bookstoreback.order.domain.dto.request.CreateRefundPolicyRequest;
import com.nhnacademy.bookstoreback.order.domain.dto.request.CreateWrappingTypeRequest;
import com.nhnacademy.bookstoreback.order.domain.dto.request.OrderCheckNonRequest;
import com.nhnacademy.bookstoreback.order.domain.dto.request.UpdateRefundPolicyRequest;
import com.nhnacademy.bookstoreback.order.domain.dto.request.UpdateWrappingTypeRequest;
import com.nhnacademy.bookstoreback.order.domain.dto.response.CreateBookOrderResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.CreateCartOrderResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.CreateOrderResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.CreatePaperResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetAdminAllPaperResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetAllListOrderByStatusResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetAllListOrderResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetAllPaperResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetAllRefundResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetBookByOrderCouponResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetBookOrderResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetListWrappingResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetNonOrderByInfoResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetOrderByInfoResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetOrderResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetOrderStatusResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetPaperResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetUserPointOrderResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetWrappingResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.UpdateBookOrderResponse;
import com.nhnacademy.bookstoreback.order.service.impl.BookOrderServiceImpl;
import com.nhnacademy.bookstoreback.order.service.impl.OrderServiceImpl;
import com.nhnacademy.bookstoreback.order.service.impl.OrderStatusServiceImpl;
import com.nhnacademy.bookstoreback.order.service.impl.PaperTypeServiceImpl;
import com.nhnacademy.bookstoreback.order.service.impl.RefundPolicyServiceImpl;
import com.nhnacademy.bookstoreback.order.service.impl.WrappingPaperServiceImpl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/orders")
public class OrderController {

	private final OrderServiceImpl orderServiceImpl;

	private final OrderStatusServiceImpl orderStatusServiceImpl;

	private final WrappingPaperServiceImpl wrappingPaperServiceImpl;

	private final BookOrderServiceImpl bookOrderServiceImpl;

	private final PaperTypeServiceImpl paperTypeServiceImpl;

	private final BookServiceImpl bookService;

	private final RefundPolicyServiceImpl refundPolicyServiceImpl;

	//TODO 주문

	/**
	 * 특정 주문 가져오기
	 * @param orderId 주문 아이디
	 * @return 주문 정보 리턴
	 */
	@GetMapping("/{order_id}")
	public ResponseEntity<GetOrderResponse> getOrder(@PathVariable("order_id") Long orderId) {
		return ResponseEntity.ok().body(orderServiceImpl.getOrder(orderId));
	}

	/**
	 * 주문 상태 업데이트
	 * @param orderId 주문 아이디
	 * @param orderStatusId 주문 상태 아이디
	 * @return 주문 정보 리턴
	 */
	@PutMapping("/{order_id}/orderStatus/{order_status_id}")
	public ResponseEntity<GetOrderResponse> updateOrderStatus(@PathVariable("order_id") Long orderId,
		@PathVariable("order_status_id") Long orderStatusId) {
		return ResponseEntity.ok(orderServiceImpl.updateOrderStatus(orderId, orderStatusId));
	}

	//TODO 주문상태

	/**
	 * 주문 상태 만들기
	 * @param createOrderStatusRequest 주문 정보
	 * @return 주문 정보 리턴
	 */
	@AuthorizeRole({"ORDER_STATUS_ADMIN", "HEAD_ADMIN"})
	@PostMapping("/orderStatus")
	public ResponseEntity<GetOrderStatusResponse> createOrderStatus(
		@Valid @RequestBody CreateOrderStatusRequest createOrderStatusRequest) {
		return ResponseEntity.ok(orderStatusServiceImpl.create(createOrderStatusRequest));
	}

	/**
	 * 주문 상태 업데이트
	 * @param orderStatusId 주문 상태 아이디
	 * @param createOrderStatusRequest 주문 상태 정보
	 * @return 주문 상태 정보
	 */
	@AuthorizeRole({"ORDER_STATUS_ADMIN", "HEAD_ADMIN"})
	@PutMapping("/orderStatus/{order_status_id}")
	public ResponseEntity<GetOrderStatusResponse> updateOrderStatus(@PathVariable("order_status_id") Long orderStatusId,
		@Valid @RequestBody CreateOrderStatusRequest createOrderStatusRequest) {
		return ResponseEntity.ok(orderStatusServiceImpl.update(createOrderStatusRequest, orderStatusId));
	}

	/**
	 * 주문 상태 삭제
	 * @param orderStatusId 주문 상태 아이디
	 */
	@AuthorizeRole({"ORDER_STATUS_ADMIN", "HEAD_ADMIN"})
	@DeleteMapping("/orderStatus/{order_status_id}")
	public ResponseEntity<Void> deleteOrderStatus(@PathVariable("order_status_id") Long orderStatusId) {
		orderStatusServiceImpl.delete(orderStatusId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	/**
	 * 주문 상태 다 가져오기
	 * @return 모든 주문 상태
	 */
	@GetMapping("/orderStatus/all")
	public List<GetOrderStatusResponse> orderStatusAll() {
		return orderStatusServiceImpl.findAll();
	}

	//주문상태 아이디로 주문상태 확인

	/**
	 * 주문 상태 아이디로 주문 상태 확인
	 * @param orderStatusId 주문 상태 아이디
	 * @return 주문 상태 정보
	 */
	@GetMapping("/orderStatus/{order_status_id}")
	public ResponseEntity<GetOrderStatusResponse> getOrderStatus(@PathVariable("order_status_id") Long orderStatusId) {
		return ResponseEntity.ok(orderStatusServiceImpl.findById(orderStatusId));
	}

	//TODO 포장지

	//특정 포장지 가져오기 -> 특정 포장지 종류로 변경 예정 -> 수정 O 테스트 X

	/**
	 * 포장지 종류 가져오기
	 * @param paperId 포장지 종류 아이디
	 * @return 포장지 종류 가져오기
	 */
	@GetMapping("/papers/{paper_id}")
	public ResponseEntity<GetPaperResponse> getWrappingPaper(
		@PathVariable("paper_id") Long paperId) {
		return ResponseEntity.ok(paperTypeServiceImpl.getPaperTypeById(paperId));
	}

	//포장지 삭제

	/**
	 * 포장지 삭제
	 * @param wrappingPaperId 설정된 포장지 삭제
	 */
	@DeleteMapping("/wrapping/{paper_type_id}")
	public ResponseEntity<Void> deleteWrappingPaper(@PathVariable("paper_type_id") Long wrappingPaperId) {
		wrappingPaperServiceImpl.deleteWrappingPapers(wrappingPaperId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	//포장지 종류 생성

	/**
	 * 포장지 종류 생성
	 * @param createWrappingTypeRequest 포장지 종류 정보
	 * @return 포장지 종류 정보
	 */
	@AuthorizeRole({"PAPER_ADMIN", "HEAD_ADMIN"})
	@PostMapping("/papers")
	public ResponseEntity<CreatePaperResponse> createPaper(
		@Valid @RequestBody CreateWrappingTypeRequest createWrappingTypeRequest) {
		return ResponseEntity.ok(paperTypeServiceImpl.createPaper(createWrappingTypeRequest));
	}

	/**
	 * 포장지 종류 업데이트
	 * @param updateWrappingTypeRequest 포장지 종류 정보
	 * @param paperTypeId 포장지 종류 아이디
	 * @return 포장지 종류 정보
	 */
	@AuthorizeRole({"PAPER_ADMIN", "HEAD_ADMIN"})
	@PutMapping("/papers/{paper_type_id}")
	public ResponseEntity<GetPaperResponse> updatePaper(
		@Valid @RequestBody UpdateWrappingTypeRequest updateWrappingTypeRequest,
		@PathVariable("paper_type_id") Long paperTypeId) {
		return ResponseEntity.ok(paperTypeServiceImpl.updatePaperTypeById(paperTypeId, updateWrappingTypeRequest));
	}

	/**
	 * 포장지 종류 삭제
	 * @param paperTypeId 포장지 종류 아이디
	 */
	@AuthorizeRole({"PAPER_ADMIN", "HEAD_ADMIN"})
	@DeleteMapping("/papers/{paper_type_id}")
	public ResponseEntity<Void> deletePaper(@PathVariable("paper_type_id") Long paperTypeId) {
		paperTypeServiceImpl.deletePaperTypeById(paperTypeId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	//TODO 도서 주문

	// feign 클라이언트에 사용된 매핑

	/**
	 * 주문리스트 아이디로 주문리스트 찾기
	 * @param orderListId 주문리스트 아이디
	 * @return 주문리스트 정보
	 */
	@GetMapping("/books-orders/{order_list_id}")
	public ResponseEntity<GetBookOrderResponse> getBookOrder(@PathVariable("order_list_id") Long orderListId) {
		return ResponseEntity.status(HttpStatus.OK).body(bookOrderServiceImpl.getBookOrder(orderListId));
	}

	/**
	 * 주문 보안아이디로 주문리스트 가져오기
	 * @param orderInfoId 주문 보안이이디
	 * @return 주문리스트
	 */
	@GetMapping("/book-orders/cart-order/{orderInfoId}")
	public ResponseEntity<List<GetBookOrderResponse>> getCartOrder(@PathVariable("orderInfoId") String orderInfoId) {
		return ResponseEntity.status(HttpStatus.OK).body(bookOrderServiceImpl.getBookOrderByOrderId(orderInfoId));
	}

	//도서 주문 생성

	/**
	 * 주문리스트 생성
	 * @param createBookOrderRequest 주문리스트 정보
	 * @return 주문리스트 정보
	 */
	@PostMapping("/books-orders")
	public ResponseEntity<CreateBookOrderResponse> createBookOrder(
		@Valid @RequestBody CreateBookOrderRequest createBookOrderRequest) {
		return ResponseEntity.status(HttpStatus.OK).body(bookOrderServiceImpl.createBookOrder(createBookOrderRequest));
	}

	/**
	 * 포장지 종류 전부 가져오기
	 * 페이징 처리 예정
	 * @return 포장지 종류 전부
	 */
	@GetMapping("/wrappings")
	public ResponseEntity<GetAllPaperResponse> getAllWrappingPapers() {
		return ResponseEntity.status(HttpStatus.OK).body(paperTypeServiceImpl.getAllPaperTypes());
	}

	/**
	 * 포장지 종류 전부 가져오기
	 * 페이징 처리 예정
	 * @return 포장지 종류 전부
	 */
	@AuthorizeRole({"PAPER_ADMIN", "HEAD_ADMIN"})
	@GetMapping("/papers/admin")
	public ResponseEntity<GetAdminAllPaperResponse> getAdminAllWrappingPapers() {
		return ResponseEntity.status(HttpStatus.OK).body(paperTypeServiceImpl.getAdminAllPaperTypes());
	}

	/**
	 * 주문리스트 포장지 설정
	 * @param paperId 포장지 종류 아이디
	 * @param bookOrderId 주문리스트 아이디
	 * @param quantity 책 개수
	 * @return 설정된 wrapping_paper 테이블 정보
	 */
	@PostMapping("/wrappings/{paper_id}/{book_order_id}/{quantity}")
	public ResponseEntity<GetWrappingResponse> createWrappingPapers(@PathVariable("paper_id") Long paperId,
		@PathVariable("book_order_id") long bookOrderId, @PathVariable("quantity") int quantity) {
		return ResponseEntity.status(HttpStatus.OK)
			.body(wrappingPaperServiceImpl.createWrappingPapers(paperId, bookOrderId, quantity));
	}

	/**
	 * 주문리스트 아이디로 설정된 포장지 정보 가져오기
	 * @param orderListId 주문리스트 아이디
	 * @return 설정된 wrapping_paper 테이블 정보
	 */
	@GetMapping("/books-orders/{order_list_id}/wrapping-papers")
	public ResponseEntity<GetListWrappingResponse> getWrappingPaperByOrderListId(
		@PathVariable("order_list_id") Long orderListId) {
		return ResponseEntity.status(HttpStatus.OK)
			.body(wrappingPaperServiceImpl.getWrappingPaperByOrderListId(orderListId));
	}

	/**
	 * 주문 생성
	 * @param createOrderRequest 주문 정보
	 * @return 주문 정보
	 */
	@PostMapping("/orders")
	public ResponseEntity<CreateOrderResponse> createOrder(@Valid @RequestBody CreateOrderRequest createOrderRequest,
		@CurrentUser CurrentUserDetails currentUserDetails) {
		return ResponseEntity.status(HttpStatus.OK)
			.body(orderServiceImpl.createOrder(createOrderRequest, currentUserDetails));
	}

	/**
	 * 주문리스트 주문 외래키 설정
	 * @param bookListId 주문리스트
	 * @param orderId 주문 아이디
	 * @return 주문리스트 정보
	 */
	@PutMapping("/books-orders/{book_list_id}/{order_id}")
	public ResponseEntity<UpdateBookOrderResponse> updateBookOrder(@PathVariable("book_list_id") Long bookListId,
		@PathVariable("order_id") Long orderId) {
		return ResponseEntity.status(HttpStatus.OK).body(bookOrderServiceImpl.updateOrder(bookListId, orderId));
	}

	/**
	 * 로그인 된 유저 주문 전부 가져오기
	 * 페이징 처리 예정
	 * @param currentUserDetails 로그인된 사용자 아이디
	 * @return 카트아이디를 가지고 있는 주문 전부 가져오기
	 */
	@GetMapping("/users/all")
	public ResponseEntity<GetAllListOrderResponse> findAllByUserId(@CurrentUser CurrentUserDetails currentUserDetails) {
		return ResponseEntity.status(HttpStatus.OK).body(orderServiceImpl.findAllUserId(currentUserDetails));
	}

	/**
	 * 주문 보안 아이디로 주문 가져오기
	 * @param orderInfoId 주문 보안 아이디
	 * @return 주문 정보
	 */
	@GetMapping("/order-info/{order_info_id}")
	public ResponseEntity<GetOrderByInfoResponse> findByOrderInfoId(@PathVariable("order_info_id") String orderInfoId) {
		return ResponseEntity.status(HttpStatus.OK).body(orderServiceImpl.findByOrderInfoId(orderInfoId));
	}

	@AuthorizeRole({"ORDER_STATUS_ADMIN", "HEAD_ADMIN"})
	@GetMapping("/order-status/wait")
	public ResponseEntity<GetAllListOrderByStatusResponse> getOrderStatusWait() {
		return ResponseEntity.ok(orderServiceImpl.findByOrderStatus(1L));
	}

	@AuthorizeRole({"ORDER_STATUS_ADMIN", "HEAD_ADMIN"})
	@GetMapping("/order-status/going")
	public ResponseEntity<GetAllListOrderByStatusResponse> getOrderStatusGoing() {
		return ResponseEntity.ok(orderServiceImpl.findByOrderStatus(4L));
	}

	@AuthorizeRole({"ORDER_STATUS_ADMIN", "HEAD_ADMIN"})
	@GetMapping("/order-status/complete")
	public ResponseEntity<GetAllListOrderByStatusResponse> getOrderStatusComplete() {
		return ResponseEntity.ok(orderServiceImpl.findByOrderStatus(5L));
	}

	@AuthorizeRole({"ORDER_STATUS_ADMIN", "HEAD_ADMIN"})
	@GetMapping("/order-status/refunded")
	public ResponseEntity<GetAllListOrderByStatusResponse> getOrderStatusRefunded() {
		return ResponseEntity.ok(orderServiceImpl.findByOrderStatus(6L));
	}

	@AuthorizeRole({"ORDER_STATUS_ADMIN", "HEAD_ADMIN"})
	@GetMapping("/order-status/refunding")
	public ResponseEntity<GetAllListOrderByStatusResponse> getOrderStatusRefunding() {
		return ResponseEntity.ok(orderServiceImpl.findByOrderStatus(7L));
	}

	@PostMapping("order-info/Non")
	public ResponseEntity<GetNonOrderByInfoResponse> getOrderByInfoNon(
		@Valid @RequestBody OrderCheckNonRequest orderCheckNonRequest) {
		return ResponseEntity.ok(orderServiceImpl.findByOrderInfoIdByEmail(orderCheckNonRequest.orderInfoId(),
			orderCheckNonRequest.payerEmail()));
	}

	@GetMapping("/orders-points")
	public ResponseEntity<GetUserPointOrderResponse> getUserPointOrders(
		@CurrentUser CurrentUserDetails currentUserDetails) {
		return ResponseEntity.ok(orderServiceImpl.getUserPoint(currentUserDetails));
	}

	/**
	 * 단건주문 bookId, categoryId 가져오는 controller
	 * @author 이기훈
	 * @param orderListId 주문리스트 Id
	 * @return 주문한 bookId, categoryId 가져옴
	 */
	@GetMapping("/{orderListId}/book")
	public ResponseEntity<GetBookByOrderCouponResponse> getBookByOneOrder(
		@PathVariable("orderListId") Long orderListId) {
		return ResponseEntity.status(HttpStatus.OK)
			.body(bookOrderServiceImpl.getBookAndCategoryByOrderListId(orderListId));
	}

	@GetMapping("/refunding/{orderInfoId}")
	public ResponseEntity<Void> refundingOrder(@PathVariable("orderInfoId") String orderInfoId) {
		orderServiceImpl.refundingOrder(orderInfoId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@GetMapping("/refunded/{orderInfoId}")
	public ResponseEntity<Void> refundedOrder(@PathVariable("orderInfoId") String orderInfoId) {
		orderServiceImpl.refundedOrder(orderInfoId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@GetMapping("/refund-policy")
	public ResponseEntity<GetAllRefundResponse> getRefundPolicy() {
		return ResponseEntity.ok(refundPolicyServiceImpl.getAllRefundPolicies());
	}

	@AuthorizeRole({"REFUND_ADMIN", "HEAD_ADMIN"})
	@PutMapping("/refund-policy/{refundPolicyId}")
	public ResponseEntity<Void> updateRefundPolicy(@PathVariable("refundPolicyId") Long refundPolicyId,
		@Valid @RequestBody UpdateRefundPolicyRequest updateRefundPolicyRequest) {
		refundPolicyServiceImpl.updateRefundPolicy(updateRefundPolicyRequest, refundPolicyId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@AuthorizeRole({"REFUND_ADMIN", "HEAD_ADMIN"})
	@PostMapping("/refund-policy")
	public ResponseEntity<Void> createRefundPolicy(@Valid @RequestBody CreateRefundPolicyRequest refundPolicyRequest) {
		refundPolicyServiceImpl.createRefundPolicy(refundPolicyRequest);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@AuthorizeRole({"REFUND_ADMIN", "HEAD_ADMIN"})
	@DeleteMapping("/refund-policy/{refundPolicyId}")
	public ResponseEntity<Void> deleteRefundPolicy(@PathVariable Long refundPolicyId) {
		refundPolicyServiceImpl.deleteRefundPolicy(refundPolicyId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	// 카트로 구매

	@GetMapping("/cart-orders")
	public ResponseEntity<CreateCartOrderResponse> createCartOrders(
		@CurrentUser CurrentUserDetails currentUserDetails) {
		return ResponseEntity.ok(orderServiceImpl.createCartOrder(currentUserDetails));
	}

	@PutMapping("/cart-order/{orderId}")
	public ResponseEntity<CreateOrderResponse> updateCartOrder(
		@Valid @RequestBody CreateOrderRequest createOrderRequest,
		@PathVariable Long orderId) {
		return ResponseEntity.ok(orderServiceImpl.updateCartOrder(createOrderRequest, orderId));
	}
}
