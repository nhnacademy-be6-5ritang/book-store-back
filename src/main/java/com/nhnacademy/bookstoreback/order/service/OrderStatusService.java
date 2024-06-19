package com.nhnacademy.bookstoreback.order.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.nhnacademy.bookstoreback.global.exception.OrderStatusFailException;
import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;
import com.nhnacademy.bookstoreback.order.domain.dto.request.CreateOrderStatusRequest;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetOrderStatusResponse;
import com.nhnacademy.bookstoreback.order.domain.entity.OrderStatus;
import com.nhnacademy.bookstoreback.order.repository.OrderStatusRepository;

@Service
public class OrderStatusService {
	@Autowired
	private OrderStatusRepository orderStatusRepository;

	public GetOrderStatusResponse create(CreateOrderStatusRequest createOrderStatusRequest) {
		OrderStatus orderStatus = OrderStatus.toEntity(createOrderStatusRequest);
		return GetOrderStatusResponse.from(orderStatusRepository.save(orderStatus));
	}

	public GetOrderStatusResponse update(CreateOrderStatusRequest createOrderStatusRequest, Long id) {
		OrderStatus newOrderStatus = orderStatusRepository.findById(id).orElse(null);
		if (newOrderStatus == null) {
			String errorMessage = "주문 상태를 가져올 수 없습니다";
			ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.NOT_FOUND, LocalDateTime.now());
			throw new OrderStatusFailException(errorStatus);
		}
		newOrderStatus.setOrderStatusName(createOrderStatusRequest.orderStatusName());
		return GetOrderStatusResponse.from(orderStatusRepository.save(newOrderStatus));
	}

	public void delete(Long id) {
		orderStatusRepository.deleteById(id);
	}

	public GetOrderStatusResponse findById(Long id) {
		OrderStatus orderStatus = orderStatusRepository.findById(id).orElse(null);
		if (orderStatus == null) {
			String errorMessage = "주문 상태를 가져올 수 없습니다";
			ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.NOT_FOUND, LocalDateTime.now());
			throw new OrderStatusFailException(errorStatus);
		}
		return GetOrderStatusResponse.from(orderStatus);
	}
}
