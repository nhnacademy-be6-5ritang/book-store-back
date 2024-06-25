package com.nhnacademy.bookstoreback.order.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nhnacademy.bookstoreback.global.exception.OrderFailException;
import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;
import com.nhnacademy.bookstoreback.order.domain.dto.request.CreateOrderRequest;
import com.nhnacademy.bookstoreback.order.domain.dto.response.CreateOrderResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetOrderByStatusIdResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetOrderResponse;
import com.nhnacademy.bookstoreback.order.domain.entity.Order;
import com.nhnacademy.bookstoreback.order.domain.entity.OrderStatus;
import com.nhnacademy.bookstoreback.order.repository.OrderRepository;
import com.nhnacademy.bookstoreback.order.repository.OrderStatusRepository;
import com.nhnacademy.bookstoreback.order.repository.PaperTypeRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional
public class OrderService {

	private final OrderRepository orderRepository;

	private final OrderStatusRepository orderStatusRepository;

	private final PaperTypeRepository paperTypeRepository;

	public static final String ERROR_STATUS_WAIT = "주문 상태를 대기로 지정할 수 없습니다";
	public static final String ERROR_ORDER_EXITS = "주문을 가져올 수 없습니다";

	//카트 아이디를 가지고 있다면 그걸 사용해서 정보 추가로 가져오는 코드 추가 예정
	public CreateOrderResponse createOrder(CreateOrderRequest createOrderRequest) {
		List<OrderStatus> orderStatuses = orderStatusRepository.findAll();

		for (OrderStatus orderStatus : orderStatuses) {
			if (orderStatus.getOrderStatusName().equals("대기")) {
				Order order = Order.toEntity(createOrderRequest, orderStatus);
				orderRepository.save(order);
				return CreateOrderResponse.from(order);
			}
		}
		ErrorStatus errorStatus = ErrorStatus.from(ERROR_STATUS_WAIT, HttpStatus.UNPROCESSABLE_ENTITY,
			LocalDateTime.now());
		throw new OrderFailException(errorStatus);
	}

	// 특정 주문 가져오기
	@Transactional(readOnly = true)
	public GetOrderResponse getOrder(Long orderId) {
		Order order = orderRepository.findById(orderId).orElse(null);
		if (order == null) {
			ErrorStatus errorStatus = ErrorStatus.from(ERROR_ORDER_EXITS, HttpStatus.NOT_FOUND, LocalDateTime.now());
			throw new OrderFailException(errorStatus);
		}
		return GetOrderResponse.from(order);
	}

	//관리자가 배송중이라는 주문 상태를 찾는 jpa
	@Transactional(readOnly = true)
	public GetOrderByStatusIdResponse findByOrderStatus_OrderStatusId(Long orderStatusId, Pageable pageable) {
		Page<Order> order = orderRepository.findByOrderStatus_OrderStatusId(orderStatusId, pageable);
		if (order == null) {
			ErrorStatus errorStatus = ErrorStatus.from(ERROR_STATUS_WAIT, HttpStatus.NOT_FOUND, LocalDateTime.now());
			throw new OrderFailException(errorStatus);
		}
		return GetOrderByStatusIdResponse.from(order);
	}

	// 주문의 상태 변경
	public GetOrderResponse updateOrderStatus(Long orderId, Long orderStatusId) {
		Order order = orderRepository.findById(orderId).orElse(null);
		OrderStatus orderStatus = orderStatusRepository.findById(orderStatusId).orElse(null);
		if (order == null) {
			ErrorStatus errorStatus = ErrorStatus.from(ERROR_ORDER_EXITS, HttpStatus.NOT_FOUND, LocalDateTime.now());
			throw new OrderFailException(errorStatus);
		} else if (orderStatus == null) {
			ErrorStatus errorStatus = ErrorStatus.from(ERROR_STATUS_WAIT, HttpStatus.NOT_FOUND, LocalDateTime.now());
			throw new OrderFailException(errorStatus);
		}
		order.updateOrderStatus(orderStatus);
		return GetOrderResponse.from(orderRepository.save(order));
	}
}
