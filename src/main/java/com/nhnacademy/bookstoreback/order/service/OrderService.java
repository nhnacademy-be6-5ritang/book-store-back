package com.nhnacademy.bookstoreback.order.service;

import org.springframework.data.domain.Pageable;

import com.nhnacademy.bookstoreback.order.domain.dto.request.CreateOrderRequest;
import com.nhnacademy.bookstoreback.order.domain.dto.response.CreateOrderResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetAllListOrderResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetOrderByInfoResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetOrderByStatusIdResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetOrderResponse;

public interface OrderService {
	CreateOrderResponse createOrder(CreateOrderRequest createOrderRequest);

	GetOrderResponse getOrder(Long orderId);

	GetOrderByStatusIdResponse findByOrderStatus_OrderStatusId(Long orderStatusId, Pageable pageable);

	GetOrderResponse updateOrderStatus(Long orderId, Long orderStatusId);

	GetAllListOrderResponse findAllByCartId(Long cartId);

	GetOrderByInfoResponse findByOrderInfoId(String orderInfoId);
}
