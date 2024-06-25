package com.nhnacademy.bookstoreback.order.controller;

import org.springframework.data.domain.Pageable;
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

import com.nhnacademy.bookstoreback.order.domain.dto.request.CreateBookOrderRequest;
import com.nhnacademy.bookstoreback.order.domain.dto.request.CreateOrderRequest;
import com.nhnacademy.bookstoreback.order.domain.dto.request.CreateOrderStatusRequest;
import com.nhnacademy.bookstoreback.order.domain.dto.request.CreateWrappingRequest;
import com.nhnacademy.bookstoreback.order.domain.dto.request.CreateWrappingTypeRequest;
import com.nhnacademy.bookstoreback.order.domain.dto.request.UpdateWrappingTypeRequest;
import com.nhnacademy.bookstoreback.order.domain.dto.response.CreateBookOrderResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.CreateOrderResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.CreatePaperResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetAllPaperResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetBookOrderByInfoIdResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetOrderByStatusIdResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetOrderResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetOrderStatusResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetPaperResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetWrappingResponse;
import com.nhnacademy.bookstoreback.order.service.BookOrderService;
import com.nhnacademy.bookstoreback.order.service.OrderService;
import com.nhnacademy.bookstoreback.order.service.OrderStatusService;
import com.nhnacademy.bookstoreback.order.service.PaperTypeService;
import com.nhnacademy.bookstoreback.order.service.WrappingPaperService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping
public class OrderController {

	private final OrderService orderService;

	private final OrderStatusService orderStatusService;

	private final WrappingPaperService wrappingPaperService;

	private final BookOrderService bookOrderService;

	private final PaperTypeService paperTypeService;

	//TODO 주문

	//주문 만들기 (도서 주문)
	@PostMapping(value = "/createOrder")
	public ResponseEntity<CreateOrderResponse> createOrder(@ModelAttribute CreateOrderRequest createOrderRequest
	) {
		return ResponseEntity.ok(orderService.createOrder(createOrderRequest));
	}

	//특정 주문 가져오기
	@GetMapping("/{order_id}")
	public ResponseEntity<GetOrderResponse> getOrder(@PathVariable("order_id") Long orderId) {
		return ResponseEntity.ok().body(orderService.getOrder(orderId));
	}

	//특정 상태의 주문 가져오기
	@GetMapping("/getOrderByStatus/{order_status_id}")
	public ResponseEntity<GetOrderByStatusIdResponse> getOrderStatus(
		@PathVariable("order_status_id") Long orderStatusId,
		Pageable pageable) {
		return ResponseEntity.ok(orderService.findByOrderStatus_OrderStatusId(orderStatusId, pageable));
	}

	//주문 상태 업데이트
	@PutMapping("/{order_id}/orderStatus/{order_status_id}")
	public ResponseEntity<GetOrderResponse> updateOrderStatus(@PathVariable("order_id") Long orderId,
		@PathVariable("order_status_id") Long orderStatusId) {
		return ResponseEntity.ok(orderService.updateOrderStatus(orderId, orderStatusId));
	}

	//포장지 확인
	@GetMapping("/{order_id}/wrapping")
	public ResponseEntity<GetWrappingResponse> getOrderWrappingCheck(@PathVariable("order_id") Long orderId) {
		return ResponseEntity.ok(wrappingPaperService.findOrder_OrderId(orderId));
	}

	//TODO 주문상태

	//주문상태 만들기
	@PostMapping("/orderStatus")
	public ResponseEntity<GetOrderStatusResponse> createOrderStatus(
		@RequestBody CreateOrderStatusRequest createOrderStatusRequest) {
		return ResponseEntity.ok(orderStatusService.create(createOrderStatusRequest));
	}

	//주문상태 업데이트
	@PutMapping("/orderStatus/{order_status_id}")
	public ResponseEntity<GetOrderStatusResponse> updateOrderStatus(@PathVariable("order_status_id") Long orderStatusId,
		@RequestBody CreateOrderStatusRequest createOrderStatusRequest) {
		return ResponseEntity.ok(orderStatusService.update(createOrderStatusRequest, orderStatusId));
	}

	//주문상태 삭제
	@DeleteMapping("/orderStatus/{order_status_id}")
	public void deleteOrderStatus(@PathVariable("order_status_id") Long orderStatusId) {
		orderStatusService.delete(orderStatusId);
	}

	//주문상태 아이디로 주문상태 확인
	@GetMapping("/orderStatus/{order_status_id}")
	public ResponseEntity<GetOrderStatusResponse> getOrderStatus(@PathVariable("order_status_id") Long orderStatusId) {
		return ResponseEntity.ok(orderStatusService.findById(orderStatusId));
	}

	//TODO 포장지

	// 포장지 전부 -> 가져오기 포장지 종류 가져오기로 변경 예정 -> 수정 O 테스트 X
	@GetMapping("/wrapping")
	public ResponseEntity<GetAllPaperResponse> getAllWrappingPapers() {
		return ResponseEntity.ok(paperTypeService.getAllPaperTypes());
	}

	//포장지 생성
	@PostMapping("/wrapping/{order_id}")
	public ResponseEntity<GetWrappingResponse> createWrappingPaper(
		@RequestBody CreateWrappingRequest createWrappingRequest, @PathVariable("order_id") Long orderId) {
		return ResponseEntity.ok(wrappingPaperService.createWrappingPapers(createWrappingRequest, orderId));
	}

	//특정 포장지 가져오기 -> 특정 포장지 종류로 변경 예정 -> 수정 O 테스트 X
	@GetMapping("/wrapping/{paper_id}")
	public ResponseEntity<GetPaperResponse> getWrappingPaper(
		@PathVariable("paper_id") Long paperId) {
		return ResponseEntity.ok(paperTypeService.getPaperTypeById(paperId));
	}

	//포장지 삭제
	@DeleteMapping("/wrapping/{wrapping_paper_id}")
	public void deleteWrappingPaper(@PathVariable("wrapping_paper_id") Long wrappingPaperId) {
		wrappingPaperService.deleteWrappingPapers(wrappingPaperId);
	}

	//포장지 종류 생성
	@PostMapping("/createPaper")
	public ResponseEntity<CreatePaperResponse> createPaper(
		@ModelAttribute CreateWrappingTypeRequest createWrappingTypeRequest) {
		return ResponseEntity.ok(paperTypeService.createPaper(createWrappingTypeRequest));
	}

	@PutMapping("/updatePaper/{paper_type_id}")
	public ResponseEntity<GetPaperResponse> updatePaper(
		@ModelAttribute UpdateWrappingTypeRequest updateWrappingTypeRequest,
		@PathVariable("paper_type_id") Long paperTypeId) {
		return ResponseEntity.ok(paperTypeService.updatePaperTypeById(paperTypeId, updateWrappingTypeRequest));
	}

	@DeleteMapping("/deletePaper/{paper_type_id}")
	public void deletePaper(@PathVariable("paper_type_id") Long paperTypeId) {
		paperTypeService.deletePaperTypeById(paperTypeId);
	}

	//TODO 도서 주문

	//주문 보안 아이디로 주문리스트 가져오기
	@GetMapping("/bookOrder/{order_info_id}")
	public ResponseEntity<GetBookOrderByInfoIdResponse> bookOrder(@PathVariable("order_info_id") String orderInfoId) {
		return ResponseEntity.ok(bookOrderService.findByOrderInfoId(orderInfoId));
	}

	//도서 주문 생성
	@PostMapping("/bookOrder")
	public ResponseEntity<CreateBookOrderResponse> createBookOrder(
		@RequestBody CreateBookOrderRequest createBookOrderRequest) {
		return ResponseEntity.ok(bookOrderService.createBookOrder(createBookOrderRequest));
	}
}
