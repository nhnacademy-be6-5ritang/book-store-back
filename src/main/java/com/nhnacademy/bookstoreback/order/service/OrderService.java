package com.nhnacademy.bookstoreback.order.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.nhnacademy.bookstoreback.order.domain.entity.Order;
import com.nhnacademy.bookstoreback.order.domain.entity.OrderStatus;
import com.nhnacademy.bookstoreback.order.domain.entity.WrappingPaper;
import com.nhnacademy.bookstoreback.order.repository.OrderRepository;
import com.nhnacademy.bookstoreback.order.repository.OrderStatusRepository;
import com.nhnacademy.bookstoreback.order.repository.WrappingPaperRepository;
import com.nhnacademy.bookstoreback.order.service.impl.OrderServiceImpl;

@Service
public class OrderService implements OrderServiceImpl {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private OrderStatusRepository orderStatusRepository;

	@Autowired
	private WrappingPaperRepository wrappingPaperRepository;

	//카트 아이디를 가지고 있다면 그걸 사용해서 정보 추가로 가져오는 코드 추가 예정
	@Override
	public Order createOrder(Order order, Long wrappingPaperId) {
		order.setOrderInfoId(UUID.randomUUID().toString());
		List<OrderStatus> orderStatuses = orderStatusRepository.findAll();
		WrappingPaper wrappingPaper = wrappingPaperRepository.findById(wrappingPaperId).orElse(null);
		order.setWrappingPaper(wrappingPaper);
		for (OrderStatus orderStatus : orderStatuses) {
			if (orderStatus.getOrderStatusName().equals("대기")) {
				order.setOrderStatus(orderStatus);
			}
		}
		return orderRepository.save(order);
	}

	// 특정 주문 가져오기
	@Override
	public Order getOrder(Long orderId) {
		return orderRepository.findById(orderId).orElse(null);
	}

	//관리자가 배송중이라는 주문 상태를 찾는 jpa
	public Page<Order> findByOrderStatus_OrderStatusId(Long orderStatusId, Pageable pageable) {
		return orderRepository.findByOrderStatus_OrderStatusId(orderStatusId, pageable);
	}

	// 주문의 상태 변경
	public Order updateOrderStatus(Long orderId, Long orderStatusId) {
		Order order = orderRepository.findById(orderId).orElse(null);
		OrderStatus orderStatus = orderStatusRepository.findById(orderStatusId).orElse(null);
		if (order != null && orderStatus != null) {
			order.setOrderStatus(orderStatus);
			return orderRepository.save(order);
		}
		return null;
	}

	//주문에 적용된 포장지 확인
	public WrappingPaper getWrappingPapers(Long orderId) {
		Order order = orderRepository.findById(orderId).orElse(null);
		if (order != null) {
			return order.getWrappingPaper();
		}
		return null;
	}

	//주문에 포장지 적용
	public Order updateByOrderId(Long orderId, Long wrappingPaperId) {
		Order order = orderRepository.findById(orderId).orElse(null);
		WrappingPaper wrappingPaper = wrappingPaperRepository.findById(wrappingPaperId).orElse(null);
		if (order != null) {
			order.setWrappingPaper(wrappingPaper);
			return orderRepository.save(order);
		}
		return null;
	}
}
