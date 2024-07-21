package com.nhnacademy.bookstoreback.payment;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nhnacademy.bookstoreback.auth.jwt.dto.CurrentUserDetails;
import com.nhnacademy.bookstoreback.book.domain.entity.Book;
import com.nhnacademy.bookstoreback.global.exception.BookOrderFailException;
import com.nhnacademy.bookstoreback.global.exception.OrderFailException;
import com.nhnacademy.bookstoreback.global.exception.ParserFailException;
import com.nhnacademy.bookstoreback.global.exception.PaymentFailException;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetBookOrderByInfoIdResponse;
import com.nhnacademy.bookstoreback.order.domain.entity.BookOrder;
import com.nhnacademy.bookstoreback.order.domain.entity.Order;
import com.nhnacademy.bookstoreback.order.repository.BookOrderRepository;
import com.nhnacademy.bookstoreback.order.repository.OrderRepository;
import com.nhnacademy.bookstoreback.order.service.impl.OrderServiceImpl;
import com.nhnacademy.bookstoreback.payment.dto.entitiy.Payment;
import com.nhnacademy.bookstoreback.payment.dto.response.CancelResponse;
import com.nhnacademy.bookstoreback.payment.dto.response.PaymentResponse;
import com.nhnacademy.bookstoreback.payment.dto.response.TransactionsResponse;
import com.nhnacademy.bookstoreback.payment.dto.response.UpdatePaymentResponse;
import com.nhnacademy.bookstoreback.payment.repository.PaymentRepository;
import com.nhnacademy.bookstoreback.payment.service.Impl.PaymentServiceImpl;
import com.nhnacademy.bookstoreback.point.earningpolicy.exception.PointEarningPolicyNotFoundException;
import com.nhnacademy.bookstoreback.point.earningpolicy.repository.PointEarningPolicyRepository;
import com.nhnacademy.bookstoreback.user.domain.entity.User;
import com.nhnacademy.bookstoreback.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {
	@Mock
	private PaymentRepository paymentRepository;

	@Mock
	private OrderRepository orderRepository;

	@Mock
	private BookOrderRepository bookOrderRepository;

	@Mock
	private PointEarningPolicyRepository pointEarningPolicyRepository;

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private PaymentServiceImpl paymentServiceImpl;

	@Mock
	private OrderServiceImpl orderServiceImpl;

	@BeforeEach
	void setUp() {
	}

	@Test
	void testParsePaymentResponse() {
		String paymentResponseJson = "{ \"paymentKey\": \"key\", \"orderId\": \"orderId\", \"easyPay\": { \"amount\": 1000 }, \"status\": \"COMPLETED\", \"requestedAt\": \"2023-07-15T15:30:00Z\" }";
		PaymentResponse paymentResponse = paymentServiceImpl.parsePaymentResponse(paymentResponseJson);
		assertNotNull(paymentResponse);
		assertEquals("key", paymentResponse.paymentKey());
		assertEquals("orderId", paymentResponse.orderId());
		assertEquals(new BigDecimal(1000), paymentResponse.amount());
		assertEquals("COMPLETED", paymentResponse.status());
	}

	@Test
	void testParsePaymentResponse_ThrowsParserFailException() {
		String paymentResponseJson = "{ invalid json }";
		assertThrows(ParserFailException.class, () -> paymentServiceImpl.parsePaymentResponse(paymentResponseJson));
	}

	@Test
	void testTransactions() {
		String paymentResponseJson = "{ \"orderId\": \"orderId\", \"easyPay\": { \"amount\": 1000, \"provider\": \"KAKAO\" }, \"status\": \"COMPLETED\", \"orderName\": \"Book Order\", \"requestedAt\": \"2023-07-15T15:30:00Z\" }";
		TransactionsResponse transactionsResponse = paymentServiceImpl.transactions(paymentResponseJson);
		assertNotNull(transactionsResponse);
		assertEquals("orderId", transactionsResponse.orderId());
		assertEquals(new BigDecimal(1000), transactionsResponse.amount());
		assertEquals("COMPLETED", transactionsResponse.status());
	}

	@Test
	void testFindByOrderInfoId() {
		Order order = mock(Order.class);
		BookOrder bookOrder = mock(BookOrder.class);
		when(orderRepository.findByOrderInfoId("orderInfoId")).thenReturn(order);
		when(bookOrderRepository.findByOrder_OrderId(anyLong())).thenReturn(bookOrder);
		when(bookOrder.getOrderListId()).thenReturn(1L);
		when(bookOrder.getBook()).thenReturn(mock(Book.class));
		when(bookOrder.getOrder()).thenReturn(order);
		when(bookOrder.getBookQuantity()).thenReturn(1);

		GetBookOrderByInfoIdResponse response = paymentServiceImpl.findByOrderInfoId("orderInfoId");
		assertNotNull(response);
		assertEquals(1L, response.orderListId());
	}

	@Test
	void testPaymentFindByOrderInfoId() {
		Payment payment = mock(Payment.class);
		when(paymentRepository.findByOrder_OrderInfoId("orderInfoId")).thenReturn(payment);
		when(payment.getPaymentKey()).thenReturn("key");
		when(payment.getPaymentId()).thenReturn(1L);

		CancelResponse response = paymentServiceImpl.paymentFindByOrderInfoId("orderInfoId");
		assertNotNull(response);
		assertEquals("key", response.paymentKey());
	}

	@Test
	void testPaymentFindByOrderInfoId_ThrowsPaymentFailException() {
		when(paymentRepository.findByOrder_OrderInfoId("invalidOrderInfoId")).thenReturn(null);
		assertThrows(PaymentFailException.class,
			() -> paymentServiceImpl.paymentFindByOrderInfoId("invalidOrderInfoId"));
	}

	@Test
	void testSavePaymentResponse_OrderNotFound() {
		String paymentResponseJson = "{ \"paymentKey\": \"key\", \"orderId\": \"orderId\", \"easyPay\": { \"amount\": 1000 }, \"status\": \"COMPLETED\", \"requestedAt\": \"2023-07-15T15:30:00Z\" }";
		when(orderRepository.findByOrderInfoId("orderId")).thenReturn(null);
		assertThrows(OrderFailException.class, () -> paymentServiceImpl.savePaymentResponse(paymentResponseJson, null));
	}

	@Test
	void testSavePaymentResponse_PointPolicyNotFound() {
		String paymentResponseJson = "{ \"paymentKey\": \"key\", \"orderId\": \"orderId\", \"easyPay\": { \"amount\": 1000 }, \"status\": \"COMPLETED\", \"requestedAt\": \"2023-07-15T15:30:00Z\" }";
		Order order = mock(Order.class);
		User user = mock(User.class);
		when(orderRepository.findByOrderInfoId("orderId")).thenReturn(order);
		when(order.getOrderPointSale()).thenReturn(BigDecimal.TEN);
		when(userRepository.getReferenceById(anyLong())).thenReturn(user);
		when(pointEarningPolicyRepository.findByPointEarningPolicyType("포인트 사용")).thenReturn(Optional.empty());
		assertThrows(PointEarningPolicyNotFoundException.class,
			() -> paymentServiceImpl.savePaymentResponse(paymentResponseJson, mock(
				CurrentUserDetails.class)));
	}

	@Test
	void testUpdatePayment_ThrowsParserFailException() throws JsonProcessingException {
		String paymentResponseJson = "{ invalid json }";
		assertThrows(ParserFailException.class, () -> paymentServiceImpl.updatePayment(paymentResponseJson, 1L));
	}

	@Test
	void testUpdatePayment_Success() throws JsonProcessingException {
		// JSON 응답과 Mock 객체 생성
		String paymentResponseJson = "{ \"status\": \"COMPLETED\" }";
		Payment payment = mock(Payment.class);
		Order order = mock(Order.class);

		// Mock 설정
		when(paymentRepository.getReferenceById(1L)).thenReturn(payment);
		when(payment.getOrder()).thenReturn(order);
		when(order.getOrderId()).thenReturn(1L);
		when(orderRepository.getReferenceById(1L)).thenReturn(order);

		UpdatePaymentResponse response = paymentServiceImpl.updatePayment(paymentResponseJson, 1L);

		// 결과 검증
		assertNotNull(response);
		assertEquals("COMPLETED", response.status());
	}

	@Test
	void testFindByCartOrderInfoId() {
		Order order = mock(Order.class);
		List<BookOrder> bookOrders = List.of(mock(BookOrder.class), mock(BookOrder.class));
		when(orderRepository.findByOrderInfoId("orderInfoId")).thenReturn(order);
		when(bookOrderRepository.findByOrder_OrderInfoId("orderInfoId")).thenReturn(bookOrders);
		when(bookOrders.get(0).getBook()).thenReturn(mock(Book.class));
		when(bookOrders.get(0).getBook().getBookTitle()).thenReturn("Book Title");

		GetBookOrderByInfoIdResponse response = paymentServiceImpl.findByCartOrderInfoId("orderInfoId");

		assertNotNull(response);
		assertEquals("Book Title외1", response.title());
	}

	@Test
	void testFindByCartOrderInfoId_NoBookOrder() {
		when(orderRepository.findByOrderInfoId("orderInfoId")).thenReturn(mock(Order.class));
		when(bookOrderRepository.findByOrder_OrderInfoId("orderInfoId")).thenReturn(null);

		assertThrows(BookOrderFailException.class, () -> paymentServiceImpl.findByCartOrderInfoId("orderInfoId"));
	}

	@Test
	void testUpdatePayment_ThrowsOrderFailException() throws JsonProcessingException {
		String paymentResponseJson = "{ \"status\": \"COMPLETED\" }";
		Payment payment = mock(Payment.class);

		when(paymentRepository.getReferenceById(1L)).thenReturn(payment);
		when(payment.getOrder()).thenReturn(null);

		assertThrows(NullPointerException.class, () -> paymentServiceImpl.updatePayment(paymentResponseJson, 1L));
	}

	@Test
	void testFindByOrderInfoId_BookOrderIsNull() {
		Order order = mock(Order.class);
		when(orderRepository.findByOrderInfoId("orderInfoId")).thenReturn(order);
		when(bookOrderRepository.findByOrder_OrderId(anyLong())).thenReturn(null);

		assertThrows(BookOrderFailException.class, () -> paymentServiceImpl.findByOrderInfoId("orderInfoId"));
	}

	@Test
	void testFindByCartOrderInfoId_OrderIsNull() {
		when(orderRepository.findByOrderInfoId("orderInfoId")).thenReturn(null);

		assertThrows(OrderFailException.class, () -> paymentServiceImpl.findByCartOrderInfoId("orderInfoId"));
	}

}
