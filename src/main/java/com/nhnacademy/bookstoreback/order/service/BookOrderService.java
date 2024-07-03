package com.nhnacademy.bookstoreback.order.service;

import com.nhnacademy.bookstoreback.order.domain.dto.request.CreateBookOrderRequest;
import com.nhnacademy.bookstoreback.order.domain.dto.response.CreateBookOrderResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetBookOrderResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.UpdateBookOrderResponse;

public interface BookOrderService {
	CreateBookOrderResponse createBookOrder(CreateBookOrderRequest createBookOrderRequest);

	UpdateBookOrderResponse updateOrder(Long bookOrderId, Long orderId);

	GetBookOrderResponse getBookOrder(Long bookOrderId);
}
