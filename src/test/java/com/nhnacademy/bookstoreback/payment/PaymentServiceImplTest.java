package com.nhnacademy.bookstoreback.payment;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetOrderByInfoResponse;
import com.nhnacademy.bookstoreback.order.domain.entity.BookOrder;
import com.nhnacademy.bookstoreback.order.domain.entity.Order;
import com.nhnacademy.bookstoreback.order.domain.entity.OrderStatus;
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
import com.nhnacademy.bookstoreback.payment.service.Impl.PaymentServiceImpl;
import com.nhnacademy.bookstoreback.point.earningpolicy.domain.entity.PointEarningPolicy;
import com.nhnacademy.bookstoreback.point.earningpolicy.exception.PointEarningPolicyNotFoundException;
import com.nhnacademy.bookstoreback.point.earningpolicy.repository.PointEarningPolicyRepository;
import com.nhnacademy.bookstoreback.point.transaction.domain.entity.PointTransaction;
import com.nhnacademy.bookstoreback.point.transaction.repository.PointTransactionRepository;
import com.nhnacademy.bookstoreback.user.domain.entity.User;
import com.nhnacademy.bookstoreback.user.repository.UserRepository;
import com.nhnacademy.bookstoreback.usergrade.domain.entity.UserGrade;
import com.nhnacademy.bookstoreback.usergrade.repository.UserGradeRepository;

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

	@Mock
	private PointTransactionRepository pointTransactionRepository;

	@Mock
	private UserGradeRepository userGradeRepository;

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
	void testTransactions_InvalidJson() {
		String paymentResponseJson = "{ invalid json }";
		assertThrows(ParserFailException.class, () -> paymentServiceImpl.transactions(paymentResponseJson));
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
	void testFindByOrderInfoId_BookOrderIsNull() {
		Order order = mock(Order.class);
		when(orderRepository.findByOrderInfoId("orderInfoId")).thenReturn(order);
		when(bookOrderRepository.findByOrder_OrderId(anyLong())).thenReturn(null);

		assertThrows(BookOrderFailException.class, () -> paymentServiceImpl.findByOrderInfoId("orderInfoId"));
	}

	@Test
	void testFindByOrderInfoId_OrderIsNull() {
		when(orderRepository.findByOrderInfoId("orderInfoId")).thenReturn(null);

		assertThrows(OrderFailException.class, () -> paymentServiceImpl.findByOrderInfoId("orderInfoId"));
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
			() -> paymentServiceImpl.savePaymentResponse(paymentResponseJson, mock(CurrentUserDetails.class)));
	}

	@Test
	void testUpdatePayment_ThrowsParserFailException() throws JsonProcessingException {
		String paymentResponseJson = "{ invalid json }";
		assertThrows(ParserFailException.class, () -> paymentServiceImpl.updatePayment(paymentResponseJson, 1L));
	}

	@Test
	void testUpdatePayment_Success() throws JsonProcessingException {
		String paymentResponseJson = "{ \"status\": \"COMPLETED\" }";
		Payment payment = mock(Payment.class);
		Order order = mock(Order.class);

		when(paymentRepository.getReferenceById(1L)).thenReturn(payment);
		when(payment.getOrder()).thenReturn(order);
		when(order.getOrderId()).thenReturn(1L);
		when(orderRepository.getReferenceById(1L)).thenReturn(order);

		UpdatePaymentResponse response = paymentServiceImpl.updatePayment(paymentResponseJson, 1L);

		assertNotNull(response);
		assertEquals("COMPLETED", response.status());
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
	void testFindByCartOrderInfoId_OrderIsNull() {
		when(orderRepository.findByOrderInfoId("orderInfoId")).thenReturn(null);

		assertThrows(OrderFailException.class, () -> paymentServiceImpl.findByCartOrderInfoId("orderInfoId"));
	}

	@Test
	void testSavePaymentResponse_NoPointEarningPolicy() {
		String paymentResponseJson = "{ \"paymentKey\": \"key\", \"orderId\": \"orderId\", \"easyPay\": { \"amount\": 1000 }, \"status\": \"COMPLETED\", \"requestedAt\": \"2023-07-15T15:30:00Z\" }";
		Order order = mock(Order.class);
		User user = mock(User.class);
		when(orderRepository.findByOrderInfoId("orderId")).thenReturn(order);
		when(order.getOrderPointSale()).thenReturn(BigDecimal.TEN);
		when(userRepository.getReferenceById(anyLong())).thenReturn(user);
		when(pointEarningPolicyRepository.findByPointEarningPolicyType("포인트 사용")).thenReturn(Optional.empty());

		assertThrows(PointEarningPolicyNotFoundException.class,
			() -> paymentServiceImpl.savePaymentResponse(paymentResponseJson, mock(CurrentUserDetails.class)));
	}

	@Test
	void testUpdatePayment_NoPaymentFound() throws JsonProcessingException {
		String paymentResponseJson = "{ \"status\": \"COMPLETED\" }";
		when(paymentRepository.getReferenceById(1L)).thenThrow(new RuntimeException("Payment not found"));

		assertThrows(RuntimeException.class, () -> paymentServiceImpl.updatePayment(paymentResponseJson, 1L));
	}

	@Test
	void testUpdatePayment_NoOrderFound() throws JsonProcessingException {
		String paymentResponseJson = "{ \"status\": \"COMPLETED\" }";
		Payment payment = mock(Payment.class);
		when(paymentRepository.getReferenceById(1L)).thenReturn(payment);
		when(payment.getOrder()).thenReturn(mock(Order.class));
		when(orderRepository.getReferenceById(anyLong())).thenThrow(new RuntimeException("Order not found"));

		assertThrows(RuntimeException.class, () -> paymentServiceImpl.updatePayment(paymentResponseJson, 1L));
	}

	@Test
	void testSavePaymentResponse_Success() {
		String paymentResponseJson = "{ \"paymentKey\": \"key\", \"orderId\": \"orderId\", \"easyPay\": { \"amount\": 1000 }, \"status\": \"COMPLETED\", \"requestedAt\": \"2023-07-15T15:30:00Z\" }";
		Order order = mock(Order.class);
		User user = mock(User.class);
		UserGrade userGrade = mock(UserGrade.class);
		PointEarningPolicy pointEarningPolicy = mock(PointEarningPolicy.class);
		CurrentUserDetails currentUser = mock(CurrentUserDetails.class);
		Payment savedPayment = mock(Payment.class);

		when(orderRepository.findByOrderInfoId("orderId")).thenReturn(order);
		when(order.getOrderPointSale()).thenReturn(BigDecimal.ZERO);
		when(userRepository.getReferenceById(anyLong())).thenReturn(user);
		when(pointEarningPolicyRepository.findByPointEarningPolicyType(anyString())).thenReturn(
			Optional.of(pointEarningPolicy));
		when(orderServiceImpl.getTotalOrderPrice(currentUser)).thenReturn(BigDecimal.TEN);

		when(user.getUserGrade()).thenReturn(userGrade);
		when(userGrade.getUserGradeName()).thenReturn("USER_GRADE_NAME");
		when(pointEarningPolicy.getPointEarningAmount()).thenReturn(BigDecimal.TEN);
		when(order.getOrderPrice()).thenReturn(BigDecimal.TEN);
		when(paymentRepository.save(any(Payment.class))).thenReturn(savedPayment);
		PaymentSaveResponse response = paymentServiceImpl.savePaymentResponse(paymentResponseJson, currentUser);

		assertNotNull(response);
		verify(paymentRepository, times(1)).save(any(Payment.class));
	}

	@Test
	void testSavePaymentResponse_WithPoints() {
		String paymentResponseJson = "{ \"paymentKey\": \"key\", \"orderId\": \"orderId\", \"easyPay\": { \"amount\": 1000 }, \"status\": \"COMPLETED\", \"requestedAt\": \"2023-07-15T15:30:00Z\" }";
		Order order = mock(Order.class);
		User user = mock(User.class);
		UserGrade userGrade = mock(UserGrade.class);
		PointEarningPolicy pointUsagePolicy = mock(PointEarningPolicy.class);
		PointEarningPolicy userGradePolicy = mock(PointEarningPolicy.class);
		CurrentUserDetails currentUser = mock(CurrentUserDetails.class);
		Payment savedPayment = mock(Payment.class);  // Mock the saved Payment object

		when(orderRepository.findByOrderInfoId("orderId")).thenReturn(order);
		when(order.getOrderPointSale()).thenReturn(BigDecimal.TEN);
		when(order.getOrderPrice()).thenReturn(BigDecimal.TEN);
		when(userRepository.getReferenceById(anyLong())).thenReturn(user);
		when(pointEarningPolicyRepository.findByPointEarningPolicyType("포인트 사용")).thenReturn(
			Optional.of(pointUsagePolicy));
		when(pointUsagePolicy.getPointEarningAmount()).thenReturn(BigDecimal.TEN);
		when(orderServiceImpl.getTotalOrderPrice(currentUser)).thenReturn(BigDecimal.TEN);

		when(user.getUserGrade()).thenReturn(userGrade);
		when(userGrade.getUserGradeName()).thenReturn("USER_GRADE_NAME");
		when(pointEarningPolicyRepository.findByPointEarningPolicyType("USER_GRADE_NAME")).thenReturn(
			Optional.of(userGradePolicy));
		when(userGradePolicy.getPointEarningAmount()).thenReturn(BigDecimal.valueOf(10));
		when(userGrade.getUserGradeMinAmount()).thenReturn(BigDecimal.ZERO);
		when(userGrade.getUserGradeMaxAmount()).thenReturn(BigDecimal.valueOf(1000));

		when(paymentRepository.save(any(Payment.class))).thenReturn(
			savedPayment);  // Set the return value of save method
		when(userGradeRepository.findAll()).thenReturn(List.of(userGrade));

		PaymentSaveResponse response = paymentServiceImpl.savePaymentResponse(paymentResponseJson, currentUser);

		assertNotNull(response);
		verify(paymentRepository, times(1)).save(any(Payment.class));
		verify(pointTransactionRepository, times(2)).save(any(PointTransaction.class));
		verify(user, times(1)).updateOutPoints(BigDecimal.TEN);
		verify(user, times(1)).updatePoints(any(BigDecimal.class));
	}

	@Test
	void testUpdatePayment_InvalidPaymentId() throws JsonProcessingException {
		String paymentResponseJson = "{ \"status\": \"COMPLETED\" }";
		when(paymentRepository.getReferenceById(anyLong())).thenThrow(new RuntimeException("Payment not found"));

		assertThrows(RuntimeException.class, () -> paymentServiceImpl.updatePayment(paymentResponseJson, 1L));
	}

	@Test
	void testUpdatePayment_InvalidOrderId() throws JsonProcessingException {
		String paymentResponseJson = "{ \"status\": \"COMPLETED\" }";
		Payment payment = mock(Payment.class);
		when(paymentRepository.getReferenceById(anyLong())).thenReturn(payment);
		when(payment.getOrder()).thenReturn(null);

		assertThrows(NullPointerException.class, () -> paymentServiceImpl.updatePayment(paymentResponseJson, 1L));
	}

	@Test
	void testFindByOrder_Success() {
		OrderStatus orderStatus = mock(OrderStatus.class);
		Order order = Order.builder().orderStatus(orderStatus).build();
		when(orderRepository.findByOrderInfoId("orderInfoId")).thenReturn(order);

		GetOrderByInfoResponse response = paymentServiceImpl.findByOrder("orderInfoId");

		assertNotNull(response);
		assertEquals(order.getOrderId(), response.orderId());
	}

	@Test
	void testFindByOrder_OrderNotFound() {
		when(orderRepository.findByOrderInfoId("orderInfoId")).thenReturn(null);

		assertThrows(OrderFailException.class, () -> paymentServiceImpl.findByOrder("orderInfoId"));
	}

	@Test
	void testTransactions_InvalidProvider() {
		String paymentResponseJson = "{ \"orderId\": \"orderId\", \"easyPay\": { \"amount\": 1000, \"provider\": \"UNKNOWN\" }, \"status\": \"COMPLETED\", \"orderName\": \"Book Order\", \"requestedAt\": \"2023-07-15T15:30:00Z\" }";

		TransactionsResponse response = paymentServiceImpl.transactions(paymentResponseJson);

		assertNotNull(response);
		assertEquals("UNKNOWN", response.provider());
	}

	@Test
	void testSavePointPayment_Success() {
		Order order = mock(Order.class);
		User user = mock(User.class);
		CurrentUserDetails currentUser = mock(CurrentUserDetails.class);
		PointEarningPolicy pointEarningPolicy = mock(PointEarningPolicy.class);

		when(orderRepository.findByOrderInfoId("orderInfoId")).thenReturn(order);
		when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
		when(currentUser.getUserId()).thenReturn(1L);
		when(pointEarningPolicyRepository.findByPointEarningPolicyType("포인트 사용")).thenReturn(
			Optional.of(pointEarningPolicy));
		when(order.getOrderPointSale()).thenReturn(BigDecimal.TEN);
		when(pointEarningPolicy.getPointEarningAmount()).thenReturn(BigDecimal.ONE);

		paymentServiceImpl.savePointPayment("orderInfoId", currentUser);

		verify(orderServiceImpl, times(1)).updateOrderStatus(order.getOrderId(), 1L);
		verify(orderRepository, times(1)).save(order);
		verify(paymentRepository, times(1)).save(any(Payment.class));
		verify(pointTransactionRepository, times(1)).save(any(PointTransaction.class));
		verify(user, times(1)).updateOutPoints(order.getOrderPointSale());
	}

	@Test
	void testSavePointPayment_UserNotFound() {
		Order order = mock(Order.class);
		when(orderRepository.findByOrderInfoId("orderInfoId")).thenReturn(order);
		when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

		CurrentUserDetails currentUser = mock(CurrentUserDetails.class);

		assertThrows(PaymentFailException.class, () -> paymentServiceImpl.savePointPayment("orderInfoId", currentUser));
	}

	@Test
	void testSavePointPayment_OrderNotFound() {
		CurrentUserDetails currentUser = mock(CurrentUserDetails.class);
		when(orderRepository.findByOrderInfoId("orderInfoId")).thenReturn(null);

		assertThrows(PaymentFailException.class, () -> paymentServiceImpl.savePointPayment("orderInfoId", currentUser));
	}

	@Test
	void testSavePointPayment_PointEarningPolicyNotFound() {
		Order order = mock(Order.class);
		User user = mock(User.class);
		CurrentUserDetails currentUser = mock(CurrentUserDetails.class);

		when(orderRepository.findByOrderInfoId("orderInfoId")).thenReturn(order);
		when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
		when(currentUser.getUserId()).thenReturn(1L);
		when(pointEarningPolicyRepository.findByPointEarningPolicyType("포인트 사용")).thenReturn(Optional.empty());

		assertThrows(PointEarningPolicyNotFoundException.class,
			() -> paymentServiceImpl.savePointPayment("orderInfoId", currentUser));
	}

	@Test
	void testUpdatePayment_PaymentNotFound() {
		when(paymentRepository.findById(1L)).thenReturn(Optional.empty());

		assertThrows(PaymentFailException.class,
			() -> paymentServiceImpl.updatePayment(1L, mock(CurrentUserDetails.class)));
	}

	@Test
	void testUpdatePayment_UserNotFound() {
		Payment payment = mock(Payment.class);
		when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));
		when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

		assertThrows(PaymentFailException.class,
			() -> paymentServiceImpl.updatePayment(1L, mock(CurrentUserDetails.class)));
	}

	@Test
	void testGetPayment_Success() {
		Payment payment = mock(Payment.class);
		Order order = mock(Order.class);

		when(paymentRepository.findByOrder_OrderInfoId("orderInfoId")).thenReturn(payment);
		when(payment.getOrder()).thenReturn(order);
		when(payment.getPaymentKey()).thenReturn("paymentKey");
		when(payment.getAmount()).thenReturn(BigDecimal.TEN);
		when(payment.getStatus()).thenReturn("status");
		when(payment.getPaymentDate()).thenReturn(LocalDateTime.now());
		when(order.getOrderInfoId()).thenReturn("orderInfoId");

		PaymentResponse response = paymentServiceImpl.getPayment("orderInfoId");

		assertNotNull(response);
		assertEquals("paymentKey", response.paymentKey());
		assertEquals("orderInfoId", response.orderId());
		assertEquals(BigDecimal.TEN, response.amount());
		assertEquals("status", response.status());
	}

	@Test
	void testGetPayment_PaymentNotFound() {
		when(paymentRepository.findByOrder_OrderInfoId("invalidOrderInfoId")).thenReturn(null);

		assertThrows(PaymentFailException.class, () -> paymentServiceImpl.getPayment("invalidOrderInfoId"));
	}

	@Test
	void testUpdatePointPayment_Success() {
		Payment payment = mock(Payment.class);
		Order order = mock(Order.class);
		User user = mock(User.class);
		CurrentUserDetails currentUser = mock(CurrentUserDetails.class);
		PointEarningPolicy pointEarningPolicy = mock(PointEarningPolicy.class);

		when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));
		when(payment.getOrder()).thenReturn(order);
		when(order.getOrderInfoId()).thenReturn("orderInfoId");
		when(orderRepository.findByOrderInfoId("orderInfoId")).thenReturn(order);
		when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
		when(currentUser.getUserId()).thenReturn(1L);
		when(pointEarningPolicyRepository.findByPointEarningPolicyType("포인트 결제 취소")).thenReturn(
			Optional.of(pointEarningPolicy));
		when(order.getOrderPointSale()).thenReturn(BigDecimal.TEN);

		paymentServiceImpl.updatePayment(1L, currentUser);

		verify(payment, times(1)).updateStatus("결제 취소");
		verify(paymentRepository, times(1)).save(payment);
		verify(orderServiceImpl, times(1)).updateOrderStatus(order.getOrderId(), 3L);
		verify(user, times(1)).updatePoints(order.getOrderPointSale());
		verify(userRepository, times(1)).save(user);
		verify(pointTransactionRepository, times(1)).save(any(PointTransaction.class));
	}
}