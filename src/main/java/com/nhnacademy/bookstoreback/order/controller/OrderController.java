package com.nhnacademy.bookstoreback.order.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.nhnacademy.bookstoreback.order.domain.entity.Order;
import com.nhnacademy.bookstoreback.order.domain.entity.OrderStatus;
import com.nhnacademy.bookstoreback.order.domain.entity.WrappingPaper;
import com.nhnacademy.bookstoreback.order.service.OrderService;
import com.nhnacademy.bookstoreback.order.service.OrderStatusService;
import com.nhnacademy.bookstoreback.order.service.WrappingPaperService;

@RestController
public class OrderController {

	@Autowired
	private OrderService orderService;

	@Autowired
	private OrderStatusService orderStatusService;

	@Autowired
	private WrappingPaperService wrappingPaperService;

	//TODO 주문

	//주문 만들기 (주문페이지에서 포장선택까지해서)
	@PostMapping("/createOrder/{wrapping_paper_id}")
	public ResponseEntity<Order> createOrder(@RequestBody Order order,
		@PathVariable(name = "wrapping_paper_id") Long wrappingPaperId) {
		return ResponseEntity.ok(orderService.createOrder(order, wrappingPaperId));
	}

	//특정 주문 가져오기
	@GetMapping("/{order_id}")
	public ResponseEntity<Order> getOrder(@PathVariable("order_id") Long orderId) {
		Order order = orderService.getOrder(orderId);
		return ResponseEntity.ok().body(order);
	}

	//특정 상태의 주문 가져오기
	@GetMapping("/{order_status_id}")
	public ResponseEntity<Page<Order>> getOrderStatus(@PathVariable("order_status_id") Long orderStatusId,
		Pageable pageable) {
		return ResponseEntity.ok(orderService.findByOrderStatus_OrderStatusId(orderStatusId, pageable));
	}

	//주문 상태 업데이트
	@PutMapping("/{order_id}/orderStatus/{order_status_id}")
	public ResponseEntity<Order> updateOrderStatus(@PathVariable("order_id") Long orderId,
		@PathVariable("order_status_id") Long orderStatusId) {
		Order order = orderService.updateOrderStatus(orderId, orderStatusId);
		return ResponseEntity.ok(order);
	}

	//포장지 확인
	@GetMapping("/{order_id}/wrapping")
	public ResponseEntity<WrappingPaper> getOrderWrappingCheck(@PathVariable("order_id") Long orderId) {
		return ResponseEntity.ok(orderService.getWrappingPapers(orderId));
	}

	//포장지 선택
	@PutMapping("/{order_id}/wrapping/{wrapping_paper_id}")
	public ResponseEntity<Order> updateOrderWrappingCheck(@PathVariable("order_id") Long orderId,
		@PathVariable("wrapping_paper_id") Long wrappingPaperId) {
		return ResponseEntity.ok(orderService.updateByOrderId(orderId, wrappingPaperId));
	}

	//TODO 주문상태

	//주문상태 만들기
	@PostMapping("/orderStatus")
	public ResponseEntity<OrderStatus> createOrderStatus(@RequestBody OrderStatus orderStatus) {
		return ResponseEntity.ok(orderStatusService.create(orderStatus));
	}

	//주문상태 업데이트
	@PutMapping("/orderStatus/{order_status_id}")
	public ResponseEntity<OrderStatus> updateOrderStatus(@PathVariable("order_status_id") Long orderStatusId,
		@RequestBody OrderStatus orderStatus) {
		return ResponseEntity.ok(orderStatusService.update(orderStatus, orderStatusId));
	}

	//주문상태 삭제
	@DeleteMapping("/orderStatus/{order_status_id}")
	public void deleteOrderStatus(@PathVariable("order_status_id") Long orderStatusId) {
		orderStatusService.delete(orderStatusId);
	}

	//주문상태 아이디로 주문상태 확인
	@GetMapping("/orderStatus/{order_status_id}")
	public ResponseEntity<OrderStatus> getOrderStatus(@PathVariable("order_status_id") Long orderStatusId) {
		return ResponseEntity.ok(orderStatusService.findById(orderStatusId));
	}

	//TODO 포장지

	//포장지 전부 가져오기
	@GetMapping("/wrapping")
	public ResponseEntity<List<WrappingPaper>> getAllWrappingPapers() {
		return ResponseEntity.ok(wrappingPaperService.getAllWrappingPapers());
	}

	//포장지 생성
	@PostMapping("/wrapping")
	public ResponseEntity<WrappingPaper> createWrappingPaper(@RequestBody WrappingPaper wrappingPaper) {
		return ResponseEntity.ok(wrappingPaperService.createWrappingPapers(wrappingPaper));
	}

	//포장지 수정
	@PutMapping("/wrapping/{wrapping_paper_id}")
	public ResponseEntity<WrappingPaper> updateWrappingPaper(@PathVariable("wrapping_paper_id") Long wrappingPaperId,
		@RequestBody WrappingPaper wrappingPaper) {
		return ResponseEntity.ok(wrappingPaperService.updateWrappingPapers(wrappingPaper));
	}

	//특정 포장지 가져오기
	@GetMapping("/wrapping/{wrapping_paper_id}")
	public ResponseEntity<WrappingPaper> getWrappingPaper(@PathVariable("wrapping_paper_id") Long wrappingPaperId) {
		return ResponseEntity.ok(wrappingPaperService.getWrappingPapers(wrappingPaperId));
	}

	//포장지 삭제
	@DeleteMapping("/wrapping/{wrapping_paper_id}")
	public void deleteWrappingPaper(@PathVariable("wrapping_paper_id") Long wrappingPaperId) {
		wrappingPaperService.deleteWrappingPapers(wrappingPaperId);
	}
}
