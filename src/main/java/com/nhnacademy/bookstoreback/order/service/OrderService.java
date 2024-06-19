package com.nhnacademy.bookstoreback.order.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.nhnacademy.bookstoreback.global.exception.OrderFailException;
import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;
import com.nhnacademy.bookstoreback.order.domain.dto.request.CreateOrderRequest;
import com.nhnacademy.bookstoreback.order.domain.dto.response.CreateOrderResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetOrderByStatusIdResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetOrderResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetWrappingResponse;
import com.nhnacademy.bookstoreback.order.domain.entity.Order;
import com.nhnacademy.bookstoreback.order.domain.entity.OrderStatus;
import com.nhnacademy.bookstoreback.order.domain.entity.WrappingPaper;
import com.nhnacademy.bookstoreback.order.repository.OrderRepository;
import com.nhnacademy.bookstoreback.order.repository.OrderStatusRepository;
import com.nhnacademy.bookstoreback.order.repository.WrappingPaperRepository;

@Service
public class OrderService {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private OrderStatusRepository orderStatusRepository;

	@Autowired
	private WrappingPaperRepository wrappingPaperRepository;

	//카트 아이디를 가지고 있다면 그걸 사용해서 정보 추가로 가져오는 코드 추가 예정

	public CreateOrderResponse createOrder(CreateOrderRequest createOrderRequest, Long wrappingPaperId) {
		List<OrderStatus> orderStatuses = orderStatusRepository.findAll();
		WrappingPaper wrappingPaper = wrappingPaperRepository.findById(wrappingPaperId).orElse(null);

		for (OrderStatus orderStatus : orderStatuses) {
			if (orderStatus.getOrderStatusName().equals("대기")) {
				Order order = Order.toEntity(createOrderRequest, wrappingPaper, orderStatus);
				orderRepository.save(order);
				return CreateOrderResponse.from(order);
			}
		}
		String errorMessage = "주문 상태를 대기로 지정할 수 없습니다";
		ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.UNPROCESSABLE_ENTITY, LocalDateTime.now());
		throw new OrderFailException(errorStatus);
	}

	// 특정 주문 가져오기
	public GetOrderResponse getOrder(Long orderId) {
		Order order = orderRepository.findById(orderId).orElse(null);
		if (order == null) {
			String errorMessage = "주문을 가져올 수 없습니다";
			ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.NOT_FOUND, LocalDateTime.now());
			throw new OrderFailException(errorStatus);
		}
		return GetOrderResponse.from(order);
	}

	//관리자가 배송중이라는 주문 상태를 찾는 jpa
	public GetOrderByStatusIdResponse findByOrderStatus_OrderStatusId(Long orderStatusId, Pageable pageable) {
		Page<Order> order = orderRepository.findByOrderStatus_OrderStatusId(orderStatusId, pageable);
		if (order == null) {
			String errorMessage = "주문을 가져올 수 없습니다";
			ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.NOT_FOUND, LocalDateTime.now());
			throw new OrderFailException(errorStatus);
		}
		return GetOrderByStatusIdResponse.from(order);
	}

	// 주문의 상태 변경
	public GetOrderResponse updateOrderStatus(Long orderId, Long orderStatusId) {
		Order order = orderRepository.findById(orderId).orElse(null);
		OrderStatus orderStatus = orderStatusRepository.findById(orderStatusId).orElse(null);
		if (order != null && orderStatus != null) {
			order.setOrderStatus(orderStatus);
			return GetOrderResponse.from(orderRepository.save(order));
		}
		String errorMessage = "주문 또는 주문 상태를 가져올 수 없습니다";
		ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.NOT_FOUND, LocalDateTime.now());
		throw new OrderFailException(errorStatus);
	}

	//주문에 적용된 포장지 확인
	public GetWrappingResponse getWrappingPapers(Long orderId) {
		Order order = orderRepository.findById(orderId).orElse(null);
		if (order == null) {
			String errorMessage = "주문을 가져올 수 없습니다";
			ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.NOT_FOUND, LocalDateTime.now());
			throw new OrderFailException(errorStatus);
		}
		return GetWrappingResponse.from(order.getWrappingPaper());
	}
}
