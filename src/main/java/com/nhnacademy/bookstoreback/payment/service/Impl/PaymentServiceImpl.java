package com.nhnacademy.bookstoreback.payment.service.Impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookstoreback.global.exception.BookOrderFailException;
import com.nhnacademy.bookstoreback.global.exception.OrderFailException;
import com.nhnacademy.bookstoreback.global.exception.ParserFailException;
import com.nhnacademy.bookstoreback.global.exception.PaymentFailException;
import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;
import com.nhnacademy.bookstoreback.order.domain.dto.response.FindByInfoIdBookOrderGetBookResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.FindByInfoIdBookOrderGetOrderResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetBookOrderByInfoIdResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetOrderByInfoResponse;
import com.nhnacademy.bookstoreback.order.domain.entity.BookOrder;
import com.nhnacademy.bookstoreback.order.domain.entity.Order;
import com.nhnacademy.bookstoreback.order.repository.BookOrderRepository;
import com.nhnacademy.bookstoreback.order.repository.OrderRepository;
import com.nhnacademy.bookstoreback.order.service.impl.OrderServiceImpl;
import com.nhnacademy.bookstoreback.payment.dto.entitiy.Payment;
import com.nhnacademy.bookstoreback.payment.dto.response.CancelResponse;
import com.nhnacademy.bookstoreback.payment.dto.response.PaymentResponse;
import com.nhnacademy.bookstoreback.payment.dto.response.PaymentSaveResponse;
import com.nhnacademy.bookstoreback.payment.dto.response.TransactionsResponse;
import com.nhnacademy.bookstoreback.payment.dto.response.UpdatePaymentResponse;
import com.nhnacademy.bookstoreback.payment.repository.PaymentRepository;
import com.nhnacademy.bookstoreback.payment.service.PaymentService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {
	private final PaymentRepository paymentRepository;
	private final OrderRepository orderRepository;
	private final BookOrderRepository bookOrderRepository;
	private final OrderServiceImpl orderServiceImpl;

	public static final String ERROR_PARSER_FAIL = "파싱 실패";
	public static final String ERROR_PAYMENT_EXITS = "결제를 찾을 수 없습니다";
	public static final String ERROR_ORDER_EXITS = "주문 정보를 찾을 수 없습니다";
	public static final String ERROR_BOOKORDER_EXITS = "주문리스트를 찾을 수 없습니다";

	@Override
	public PaymentSaveResponse savePaymentResponse(String paymentResponseJson) {
		PaymentResponse paymentResponse = parsePaymentResponse(paymentResponseJson);
		Order order = orderRepository.findByOrderInfoId(paymentResponse.orderId());
		if (order == null) {
			ErrorStatus errorStatus = ErrorStatus.from(ERROR_ORDER_EXITS, HttpStatus.UNPROCESSABLE_ENTITY,
				LocalDateTime.now());
			throw new OrderFailException(errorStatus);
		}
		orderServiceImpl.updateOrderStatus(order.getOrderId(), 1L);
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
			ErrorStatus errorStatus = ErrorStatus.from(ERROR_PARSER_FAIL, HttpStatus.INTERNAL_SERVER_ERROR,
				LocalDateTime.now());
			throw new ParserFailException(errorStatus);
		}
	}

	@Override
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
			ErrorStatus errorStatus = ErrorStatus.from(ERROR_PARSER_FAIL, HttpStatus.INTERNAL_SERVER_ERROR,
				LocalDateTime.now());
			throw new ParserFailException(errorStatus);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public GetBookOrderByInfoIdResponse findByOrderInfoId(String orderInfoId) {
		Order order = orderRepository.findByOrderInfoId(orderInfoId);
		if (order == null) {
			ErrorStatus errorStatus = ErrorStatus.from(ERROR_ORDER_EXITS, HttpStatus.UNPROCESSABLE_ENTITY,
				LocalDateTime.now());
			throw new OrderFailException(errorStatus);
		}
		BookOrder bookOrder = bookOrderRepository.findByOrder_OrderId(order.getOrderId());
		if (bookOrder == null) {
			ErrorStatus errorStatus = ErrorStatus.from(ERROR_BOOKORDER_EXITS, HttpStatus.UNPROCESSABLE_ENTITY,
				LocalDateTime.now());
			throw new BookOrderFailException(errorStatus);
		}
		return GetBookOrderByInfoIdResponse.from(bookOrder.getOrderListId(),
			FindByInfoIdBookOrderGetBookResponse.from(bookOrder.getBook()),
			FindByInfoIdBookOrderGetOrderResponse.from(bookOrder.getOrder()), bookOrder.getBookQuantity());
	}

	@Override
	@Transactional(readOnly = true)
	public GetOrderByInfoResponse findByOrder(String orderInfoId) {
		return GetOrderByInfoResponse.from(orderRepository.findByOrderInfoId(orderInfoId));
	}

	@Override
	@Transactional(readOnly = true)
	public CancelResponse paymentFindByOrderInfoId(String orderInfoId) {
		Payment payment = paymentRepository.findByOrder_OrderInfoId(orderInfoId);
		if (payment == null) {
			ErrorStatus errorStatus = ErrorStatus.from(ERROR_PAYMENT_EXITS, HttpStatus.UNPROCESSABLE_ENTITY,
				LocalDateTime.now());
			throw new PaymentFailException(errorStatus);
		}
		return CancelResponse.from(payment.getPaymentKey(), payment.getPaymentId());
	}

	@Override
	public UpdatePaymentResponse updatePayment(String paymentResponseJson, Long paymentId) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			JsonNode rootNode = objectMapper.readTree(paymentResponseJson);
			String status = rootNode.path("status").asText();
			Payment payment = paymentRepository.getReferenceById(paymentId);
			payment.updateStatus(status);
			Order order = orderRepository.getReferenceById(payment.getOrder().getOrderId());
			orderServiceImpl.updateOrderStatus(order.getOrderId(), 3L);
			return UpdatePaymentResponse.from(status);
		} catch (JsonProcessingException e) {
			ErrorStatus errorStatus = ErrorStatus.from(ERROR_PARSER_FAIL, HttpStatus.INTERNAL_SERVER_ERROR,
				LocalDateTime.now());
			throw new ParserFailException(errorStatus);
		}
	}

}
