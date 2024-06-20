package com.nhnacademy.bookstoreback.order.controller;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.nhnacademy.bookstoreback.order.domain.dto.request.CreateOrderRequest;
import com.nhnacademy.bookstoreback.order.domain.dto.request.CreateOrderStatusRequest;
import com.nhnacademy.bookstoreback.order.domain.dto.request.CreateWrappingRequest;
import com.nhnacademy.bookstoreback.order.domain.dto.request.UpdateWrappingRequest;
import com.nhnacademy.bookstoreback.order.domain.dto.response.CreateOrderResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetOrderByStatusIdResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetOrderResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetOrderStatusResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetWrappingResponse;
import com.nhnacademy.bookstoreback.order.service.OrderService;
import com.nhnacademy.bookstoreback.order.service.OrderStatusService;
import com.nhnacademy.bookstoreback.order.service.WrappingPaperService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class OrderController {

	private final OrderService orderService;

	private final OrderStatusService orderStatusService;

	private final WrappingPaperService wrappingPaperService;

	//TODO 주문

	//주문 만들기 (주문페이지에서 포장선택까지해서)
	@PostMapping("/createOrder/{wrapping_paper_id}")
	public ResponseEntity<CreateOrderResponse> createOrder(@RequestBody CreateOrderRequest createOrderRequest,
		@PathVariable(name = "wrapping_paper_id") Long wrappingPaperId) {
		return ResponseEntity.ok(orderService.createOrder(createOrderRequest, wrappingPaperId));
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
		return ResponseEntity.ok(orderService.getWrappingPapers(orderId));
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

	//포장지 전부 가져오기
	@GetMapping("/wrapping")
	public ResponseEntity<List<GetWrappingResponse>> getAllWrappingPapers() {
		return ResponseEntity.ok(wrappingPaperService.getAllWrappingPapers());
	}

	//포장지 생성
	@PostMapping("/wrapping")
	public ResponseEntity<GetWrappingResponse> createWrappingPaper(
		@RequestBody CreateWrappingRequest createWrappingRequest) {
		return ResponseEntity.ok(wrappingPaperService.createWrappingPapers(createWrappingRequest));
	}

	//포장지 수정
	@PutMapping("/wrapping/{wrapping_paper_id}")
	public ResponseEntity<GetWrappingResponse> updateWrappingPaper(
		@PathVariable("wrapping_paper_id") Long wrappingPaperId,
		@RequestBody UpdateWrappingRequest updateWrappingRequest) {
		return ResponseEntity.ok(wrappingPaperService.updateWrappingPapers(wrappingPaperId, updateWrappingRequest));
	}

	//특정 포장지 가져오기
	@GetMapping("/wrapping/{wrapping_paper_id}")
	public ResponseEntity<GetWrappingResponse> getWrappingPaper(
		@PathVariable("wrapping_paper_id") Long wrappingPaperId) {
		return ResponseEntity.ok(wrappingPaperService.getWrappingPapers(wrappingPaperId));
	}

	//포장지 삭제
	@DeleteMapping("/wrapping/{wrapping_paper_id}")
	public void deleteWrappingPaper(@PathVariable("wrapping_paper_id") Long wrappingPaperId) {
		wrappingPaperService.deleteWrappingPapers(wrappingPaperId);
	}
}
