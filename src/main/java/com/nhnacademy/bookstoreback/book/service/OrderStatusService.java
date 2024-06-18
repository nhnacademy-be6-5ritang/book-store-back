package com.nhnacademy.bookstoreback.book.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nhnacademy.bookstoreback.book.domain.entity.OrderStatus;
import com.nhnacademy.bookstoreback.book.repository.OrderStatusRepository;

@Service
public class OrderStatusService {
	@Autowired
	private OrderStatusRepository orderStatusRepository;

	public OrderStatus create(OrderStatus orderStatus) {
		return orderStatusRepository.save(orderStatus);
	}

	public OrderStatus update(OrderStatus orderStatus, Long id) {
		OrderStatus newOrderStatus = orderStatusRepository.findById(id).orElse(null);
		if (newOrderStatus != null) {
			newOrderStatus.setOrderStatusName(orderStatus.getOrderStatusName());
		}
		return orderStatusRepository.save(orderStatus);
	}

	public void delete(Long id) {
		orderStatusRepository.deleteById(id);
	}

	public OrderStatus findById(Long id) {
		return orderStatusRepository.findById(id).orElse(null);
	}
}
