package com.nhnacademy.bookstoreback.order.service;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nhnacademy.bookstoreback.global.exception.OrderStatusFailException;
import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;
import com.nhnacademy.bookstoreback.order.domain.dto.request.CreateOrderStatusRequest;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetOrderStatusResponse;
import com.nhnacademy.bookstoreback.order.domain.entity.OrderStatus;
import com.nhnacademy.bookstoreback.order.repository.OrderStatusRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional
public class OrderStatusService {

	private final OrderStatusRepository orderStatusRepository;

	public static final String ERROR_STATUS_EXITS = "주문 상태를 가져올 수 없습니다";

	public GetOrderStatusResponse create(CreateOrderStatusRequest createOrderStatusRequest) {
		OrderStatus orderStatus = OrderStatus.toEntity(createOrderStatusRequest);
		return GetOrderStatusResponse.from(orderStatusRepository.save(orderStatus));
	}

	public GetOrderStatusResponse update(CreateOrderStatusRequest createOrderStatusRequest, Long id) {
		OrderStatus newOrderStatus = orderStatusRepository.findById(id).orElse(null);
		if (newOrderStatus == null) {

			ErrorStatus errorStatus = ErrorStatus.from(ERROR_STATUS_EXITS, HttpStatus.NOT_FOUND, LocalDateTime.now());
			throw new OrderStatusFailException(errorStatus);
		}
		newOrderStatus.updateName(createOrderStatusRequest.orderStatusName());
		return GetOrderStatusResponse.from(orderStatusRepository.save(newOrderStatus));
	}

	public void delete(Long id) {
		orderStatusRepository.deleteById(id);
	}

	@Transactional(readOnly = true)
	public GetOrderStatusResponse findById(Long id) {
		OrderStatus orderStatus = orderStatusRepository.findById(id).orElse(null);
		if (orderStatus == null) {
			ErrorStatus errorStatus = ErrorStatus.from(ERROR_STATUS_EXITS, HttpStatus.NOT_FOUND, LocalDateTime.now());
			throw new OrderStatusFailException(errorStatus);
		}
		return GetOrderStatusResponse.from(orderStatus);
	}
}
