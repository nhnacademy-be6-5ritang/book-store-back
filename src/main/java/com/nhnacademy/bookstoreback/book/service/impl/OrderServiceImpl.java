package com.nhnacademy.bookstoreback.book.service.impl;

import com.nhnacademy.bookstoreback.book.domain.entity.Order;

public interface OrderServiceImpl {
	public Order createOrder(Order order, Long wrappingPaperId);

	public Order getOrder(Long orderId);
}
