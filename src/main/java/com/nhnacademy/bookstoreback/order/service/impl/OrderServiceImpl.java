package com.nhnacademy.bookstoreback.order.service.impl;

import com.nhnacademy.bookstoreback.order.domain.entity.Order;

public interface OrderServiceImpl {
	public Order createOrder(Order order, Long wrappingPaperId);

	public Order getOrder(Long orderId);
}
