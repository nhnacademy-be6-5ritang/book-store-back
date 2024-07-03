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

import com.nhnacademy.bookstoreback.book.service.BookService;
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
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetBookResponse;
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

	private final BookService bookService;

	//TODO 주문

	//특정 주문 가져오기
	@GetMapping("/{order_id}")
	public ResponseEntity<GetOrderResponse> getOrder(@PathVariable("order_id") Long orderId) {
		return ResponseEntity.ok().body(orderServiceImpl.getOrder(orderId));
	}

	//특정 상태의 주문 가져오기
	@GetMapping("/OrderByStatus/{order_status_id}")
	public ResponseEntity<GetOrderByStatusIdResponse> getOrderStatus(
		@PathVariable("order_status_id") Long orderStatusId,
		Pageable pageable) {
		return ResponseEntity.ok(orderServiceImpl.findByOrderStatus_OrderStatusId(orderStatusId, pageable));
	}

	//주문 상태 업데이트
	@PutMapping("/{order_id}/orderStatus/{order_status_id}")
	public ResponseEntity<GetOrderResponse> updateOrderStatus(@PathVariable("order_id") Long orderId,
		@PathVariable("order_status_id") Long orderStatusId) {
		return ResponseEntity.ok(orderServiceImpl.updateOrderStatus(orderId, orderStatusId));
	}

	//TODO 주문상태

	//주문상태 만들기
	@PostMapping("/orderStatus")
	public ResponseEntity<GetOrderStatusResponse> createOrderStatus(
		@RequestBody CreateOrderStatusRequest createOrderStatusRequest) {
		return ResponseEntity.ok(orderStatusServiceImpl.create(createOrderStatusRequest));
	}

	//주문상태 업데이트
	@PutMapping("/orderStatus/{order_status_id}")
	public ResponseEntity<GetOrderStatusResponse> updateOrderStatus(@PathVariable("order_status_id") Long orderStatusId,
		@RequestBody CreateOrderStatusRequest createOrderStatusRequest) {
		return ResponseEntity.ok(orderStatusServiceImpl.update(createOrderStatusRequest, orderStatusId));
	}

	//주문상태 삭제
	@DeleteMapping("/orderStatus/{order_status_id}")
	public void deleteOrderStatus(@PathVariable("order_status_id") Long orderStatusId) {
		orderStatusServiceImpl.delete(orderStatusId);
	}

	//주문상태 아이디로 주문상태 확인
	@GetMapping("/orderStatus/{order_status_id}")
	public ResponseEntity<GetOrderStatusResponse> getOrderStatus(@PathVariable("order_status_id") Long orderStatusId) {
		return ResponseEntity.ok(orderStatusServiceImpl.findById(orderStatusId));
	}

	//TODO 포장지

	//특정 포장지 가져오기 -> 특정 포장지 종류로 변경 예정 -> 수정 O 테스트 X
	@GetMapping("/wrapping/{paper_id}")
	public ResponseEntity<GetPaperResponse> getWrappingPaper(
		@PathVariable("paper_id") Long paperId) {
		return ResponseEntity.ok(paperTypeServiceImpl.getPaperTypeById(paperId));
	}

	//포장지 삭제
	@DeleteMapping("/wrapping/{wrapping_paper_id}")
	public void deleteWrappingPaper(@PathVariable("wrapping_paper_id") Long wrappingPaperId) {
		wrappingPaperServiceImpl.deleteWrappingPapers(wrappingPaperId);
	}

	//포장지 종류 생성
	@PostMapping("/papers")
	public ResponseEntity<CreatePaperResponse> createPaper(
		@RequestBody CreateWrappingTypeRequest createWrappingTypeRequest) {
		return ResponseEntity.ok(paperTypeServiceImpl.createPaper(createWrappingTypeRequest));
	}

	@PutMapping("/papers/{paper_type_id}")
	public ResponseEntity<GetPaperResponse> updatePaper(
		@ModelAttribute UpdateWrappingTypeRequest updateWrappingTypeRequest,
		@PathVariable("paper_type_id") Long paperTypeId) {
		return ResponseEntity.ok(paperTypeServiceImpl.updatePaperTypeById(paperTypeId, updateWrappingTypeRequest));
	}

	@DeleteMapping("/papers/{paper_type_id}")
	public void deletePaper(@PathVariable("paper_type_id") Long paperTypeId) {
		paperTypeServiceImpl.deletePaperTypeById(paperTypeId);
	}

	//TODO 도서 주문

	// feign 클라이언트에 사용된 매핑

	@GetMapping("/books-orders/{order_list_id}")
	public ResponseEntity<GetBookOrderResponse> getBookOrder(@PathVariable("order_list_id") Long orderListId) {
		return ResponseEntity.status(HttpStatus.OK).body(bookOrderServiceImpl.getBookOrder(orderListId));
	}

	//도서 주문 생성
	@PostMapping("/books-orders")
	public ResponseEntity<CreateBookOrderResponse> createBookOrder(
		@RequestBody CreateBookOrderRequest createBookOrderRequest) {
		return ResponseEntity.status(HttpStatus.OK).body(bookOrderServiceImpl.createBookOrder(createBookOrderRequest));
	}

	@GetMapping("/wrappings")
	public ResponseEntity<GetAllPaperResponse> getAllWrappingPapers() {
		return ResponseEntity.status(HttpStatus.OK).body(paperTypeServiceImpl.getAllPaperTypes());
	}

	@PostMapping("/wrappings/{paper_id}/{book_order_id}/{quantity}")
	public ResponseEntity<GetWrappingResponse> createWrappingPapers(@PathVariable("paper_id") Long paperId,
		@PathVariable("book_order_id") long bookOrderId, @PathVariable("quantity") int quantity) {
		return ResponseEntity.status(HttpStatus.OK)
			.body(wrappingPaperServiceImpl.createWrappingPapers(paperId, bookOrderId, quantity));
	}

	@GetMapping("/books-orders/{order_list_id}/wrapping-papers")
	public ResponseEntity<GetListWrappingResponse> getWrappingPaperByOrderListId(
		@PathVariable("order_list_id") Long orderListId) {
		return ResponseEntity.status(HttpStatus.OK)
			.body(wrappingPaperServiceImpl.getWrappingPaperByOrderListId(orderListId));
	}

	//주문 만들기 (도서 주문)
	@PostMapping("/orders")
	public ResponseEntity<CreateOrderResponse> createOrder(@RequestBody CreateOrderRequest createOrderRequest
	) {
		return ResponseEntity.status(HttpStatus.OK).body(orderServiceImpl.createOrder(createOrderRequest));
	}

	@PutMapping("/books-orders/{book_list_id}/{order_id}")
	public ResponseEntity<UpdateBookOrderResponse> updateBookOrder(@PathVariable("book_list_id") Long bookListId,
		@PathVariable("order_id") Long orderId) {
		return ResponseEntity.status(HttpStatus.OK).body(bookOrderServiceImpl.updateOrder(bookListId, orderId));
	}

	@GetMapping("/carts/{cart_id}/orders/all")
	public ResponseEntity<GetAllListOrderResponse> findAllByCartId(@PathVariable("cart_id") Long cartId) {
		return ResponseEntity.status(HttpStatus.OK).body(orderServiceImpl.findAllByCartId(cartId));
	}

	@GetMapping("/order-info/{order_info_id}")
	public ResponseEntity<GetOrderByInfoResponse> findByOrderInfoId(@PathVariable("order_info_id") String orderInfoId) {
		return ResponseEntity.status(HttpStatus.OK).body(orderServiceImpl.findByOrderInfoId(orderInfoId));
	}

	@GetMapping("/books/{book_id}")
	public ResponseEntity<GetBookResponse> getBook(@PathVariable("book_id") Long bookId) {
		return ResponseEntity.status(HttpStatus.OK).body(bookService.findBookById(bookId));
	}

}
