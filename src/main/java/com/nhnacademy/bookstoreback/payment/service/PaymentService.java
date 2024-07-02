package com.nhnacademy.bookstoreback.payment.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookstoreback.order.domain.dto.response.FindByInfoIdBookOrderGetBookResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.FindByInfoIdBookOrderGetOrderResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetBookOrderByInfoIdResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetOrderByInfoResponse;
import com.nhnacademy.bookstoreback.order.domain.entity.BookOrder;
import com.nhnacademy.bookstoreback.order.domain.entity.Order;
import com.nhnacademy.bookstoreback.order.repository.BookOrderRepository;
import com.nhnacademy.bookstoreback.order.repository.OrderRepository;
import com.nhnacademy.bookstoreback.payment.dto.entitiy.Payment;
import com.nhnacademy.bookstoreback.payment.dto.response.PaymentResponse;
import com.nhnacademy.bookstoreback.payment.dto.response.PaymentSaveResponse;
import com.nhnacademy.bookstoreback.payment.dto.response.TransactionsResponse;
import com.nhnacademy.bookstoreback.payment.repository.PaymentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentService {
	private final PaymentRepository paymentRepository;
	private final OrderRepository orderRepository;
	private final BookOrderRepository bookOrderRepository;

	public PaymentSaveResponse savePaymentResponse(String paymentResponseJson) {
		PaymentResponse paymentResponse = parsePaymentResponse(paymentResponseJson);
		Order order = orderRepository.findByOrderInfoId(paymentResponse.orderId());
		return PaymentSaveResponse.from(paymentRepository.save(
			Payment.toEntity(paymentResponse.paymentKey(), order, paymentResponse.amount(), paymentResponse.status(),
				paymentResponse.date())));
	}

	public PaymentResponse parsePaymentResponse(String paymentResponseJson) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			JsonNode rootNode = objectMapper.readTree(paymentResponseJson);

			String paymentKey = rootNode.path("paymentKey").asText();
			String orderId = rootNode.path("orderId").asText();
			BigDecimal amount = new BigDecimal(rootNode.path("easyPay").path("amount").asInt());
			String status = rootNode.path("status").asText();
			String requestedAtStr = rootNode.path("requestedAt").asText();
			OffsetDateTime offsetDateTime = OffsetDateTime.parse(requestedAtStr);
			LocalDateTime date = offsetDateTime.toLocalDateTime();
			return PaymentResponse.from(paymentKey, orderId, amount, status, date);
		} catch (JsonProcessingException e) {
			//에러 메세지 변경 예정
			throw new IllegalArgumentException("Invalid payment response JSON", e);
		}
	}

	@Transactional(readOnly = true)
	public TransactionsResponse transactions(String paymentResponseJson) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			JsonNode rootNode = objectMapper.readTree(paymentResponseJson);

			String orderId = rootNode.path("orderId").asText();
			BigDecimal amount = new BigDecimal(rootNode.path("easyPay").path("amount").asInt());
			String status = rootNode.path("status").asText();
			String orderName = rootNode.path("orderName").asText();
			String provider = rootNode.path("easyPay").path("provider").asText();
			String requestedAtStr = rootNode.path("requestedAt").asText();
			OffsetDateTime offsetDateTime = OffsetDateTime.parse(requestedAtStr);
			LocalDateTime date = offsetDateTime.toLocalDateTime();
			return TransactionsResponse.from(orderId, amount, status, provider, orderName, date);
		} catch (JsonProcessingException e) {
			//에러 메세지 변경 예정
			throw new IllegalArgumentException("Invalid payment response JSON", e);
		}
	}

	@Transactional(readOnly = true)
	public GetBookOrderByInfoIdResponse findByOrderInfoId(String orderInfoId) {
		Order order = orderRepository.findByOrderInfoId(orderInfoId);
		BookOrder bookOrder = bookOrderRepository.findByOrder_OrderId(order.getOrderId());
		return GetBookOrderByInfoIdResponse.from(bookOrder.getOrderListId(),
			FindByInfoIdBookOrderGetBookResponse.from(bookOrder.getBook()),
			FindByInfoIdBookOrderGetOrderResponse.from(bookOrder.getOrder()), bookOrder.getBookQuantity());
	}

	@Transactional(readOnly = true)
	public GetOrderByInfoResponse findByOrder(String orderInfoId) {
		return GetOrderByInfoResponse.from(orderRepository.findByOrderInfoId(orderInfoId));
	}
}
