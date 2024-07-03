package com.nhnacademy.bookstoreback.order.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nhnacademy.bookstoreback.book.service.impl.BookServiceImpl;
import com.nhnacademy.bookstoreback.order.domain.dto.request.CreateBookOrderRequest;
import com.nhnacademy.bookstoreback.order.domain.dto.request.CreateOrderRequest;
import com.nhnacademy.bookstoreback.order.domain.dto.request.CreateOrderStatusRequest;
import com.nhnacademy.bookstoreback.order.domain.dto.request.CreateWrappingTypeRequest;
import com.nhnacademy.bookstoreback.order.domain.dto.request.UpdateWrappingTypeRequest;
import com.nhnacademy.bookstoreback.order.domain.dto.response.CreateBookOrderResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.CreateOrderResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.CreatePaperResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetAllListOrderResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetAllPaperResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetBookOrderResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetListWrappingResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetOrderByInfoResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetOrderByStatusIdResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetOrderResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetOrderStatusResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetPaperResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetWrappingResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.UpdateBookOrderResponse;
import com.nhnacademy.bookstoreback.order.service.impl.BookOrderServiceImpl;
import com.nhnacademy.bookstoreback.order.service.impl.OrderServiceImpl;
import com.nhnacademy.bookstoreback.order.service.impl.OrderStatusServiceImpl;
import com.nhnacademy.bookstoreback.order.service.impl.PaperTypeServiceImpl;
import com.nhnacademy.bookstoreback.order.service.impl.WrappingPaperServiceImpl;

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
	 * 특정 상태의 주문 가져오기
	 * @param orderStatusId 주문 상태 아이디
	 * @param pageable 페이징 처리
	 * @return 주문 정보와 페이징 정보 리턴
	 */
	@GetMapping("/OrderByStatus/{order_status_id}")
	public ResponseEntity<GetOrderByStatusIdResponse> getOrderStatus(
		@PathVariable("order_status_id") Long orderStatusId,
		Pageable pageable) {
		return ResponseEntity.ok(orderServiceImpl.findByOrderStatus_OrderStatusId(orderStatusId, pageable));
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
	@PostMapping("/orderStatus")
	public ResponseEntity<GetOrderStatusResponse> createOrderStatus(
		@RequestBody CreateOrderStatusRequest createOrderStatusRequest) {
		return ResponseEntity.ok(orderStatusServiceImpl.create(createOrderStatusRequest));
	}

	/**
	 * 주문 상태 업데이트
	 * @param orderStatusId 주문 상태 아이디
	 * @param createOrderStatusRequest 주문 상태 정보
	 * @return 주문 상태 정보
	 */
	@PutMapping("/orderStatus/{order_status_id}")
	public ResponseEntity<GetOrderStatusResponse> updateOrderStatus(@PathVariable("order_status_id") Long orderStatusId,
		@RequestBody CreateOrderStatusRequest createOrderStatusRequest) {
		return ResponseEntity.ok(orderStatusServiceImpl.update(createOrderStatusRequest, orderStatusId));
	}

	/**
	 * 주문 상태 삭제
	 * @param orderStatusId 주문 상태 아이디
	 */
	@DeleteMapping("/orderStatus/{order_status_id}")
	public void deleteOrderStatus(@PathVariable("order_status_id") Long orderStatusId) {
		orderStatusServiceImpl.delete(orderStatusId);
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
	@GetMapping("/wrapping/{paper_id}")
	public ResponseEntity<GetPaperResponse> getWrappingPaper(
		@PathVariable("paper_id") Long paperId) {
		return ResponseEntity.ok(paperTypeServiceImpl.getPaperTypeById(paperId));
	}

	//포장지 삭제

	/**
	 * 포장지 삭제
	 * @param wrappingPaperId 설정된 포장지 삭제
	 */
	@DeleteMapping("/wrapping/{wrapping_paper_id}")
	public void deleteWrappingPaper(@PathVariable("wrapping_paper_id") Long wrappingPaperId) {
		wrappingPaperServiceImpl.deleteWrappingPapers(wrappingPaperId);
	}

	//포장지 종류 생성

	/**
	 * 포장지 종류 생성
	 * @param createWrappingTypeRequest 포장지 종류 정보
	 * @return 포장지 종류 정보
	 */
	@PostMapping("/papers")
	public ResponseEntity<CreatePaperResponse> createPaper(
		@RequestBody CreateWrappingTypeRequest createWrappingTypeRequest) {
		return ResponseEntity.ok(paperTypeServiceImpl.createPaper(createWrappingTypeRequest));
	}

	/**
	 * 포장지 종류 업데이트
	 * @param updateWrappingTypeRequest 포장지 종류 정보
	 * @param paperTypeId 포장지 종류 아이디
	 * @return 포장지 종류 정보
	 */
	@PutMapping("/papers/{paper_type_id}")
	public ResponseEntity<GetPaperResponse> updatePaper(
		@ModelAttribute UpdateWrappingTypeRequest updateWrappingTypeRequest,
		@PathVariable("paper_type_id") Long paperTypeId) {
		return ResponseEntity.ok(paperTypeServiceImpl.updatePaperTypeById(paperTypeId, updateWrappingTypeRequest));
	}

	/**
	 * 포장지 종류 삭제
	 * @param paperTypeId 포장지 종류 아이디
	 */
	@DeleteMapping("/papers/{paper_type_id}")
	public void deletePaper(@PathVariable("paper_type_id") Long paperTypeId) {
		paperTypeServiceImpl.deletePaperTypeById(paperTypeId);
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

	//도서 주문 생성

	/**
	 * 주문리스트 생성
	 * @param createBookOrderRequest 주문리스트 정보
	 * @return 주문리스트 정보
	 */
	@PostMapping("/books-orders")
	public ResponseEntity<CreateBookOrderResponse> createBookOrder(
		@RequestBody CreateBookOrderRequest createBookOrderRequest) {
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
	public ResponseEntity<CreateOrderResponse> createOrder(@RequestBody CreateOrderRequest createOrderRequest
	) {
		return ResponseEntity.status(HttpStatus.OK).body(orderServiceImpl.createOrder(createOrderRequest));
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
	 * 카트아이디로 주문 전부 가져오기
	 * 페이징 처리 예정
	 * @param cartId 카트 아이디
	 * @return 카트아이디를 가지고 있는 주문 전부 가져오기
	 */
	@GetMapping("/carts/{cart_id}/orders/all")
	public ResponseEntity<GetAllListOrderResponse> findAllByCartId(@PathVariable("cart_id") Long cartId) {
		return ResponseEntity.status(HttpStatus.OK).body(orderServiceImpl.findAllByCartId(cartId));
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
}
