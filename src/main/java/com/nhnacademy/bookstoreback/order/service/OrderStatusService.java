package com.nhnacademy.bookstoreback.order.service;

import com.nhnacademy.bookstoreback.order.domain.dto.request.CreateOrderStatusRequest;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetOrderStatusResponse;

public interface OrderStatusService {
	GetOrderStatusResponse create(CreateOrderStatusRequest createOrderStatusRequest);

	GetOrderStatusResponse update(CreateOrderStatusRequest createOrderStatusRequest, Long id);

	void delete(Long id);

	GetOrderStatusResponse findById(Long id);
}
