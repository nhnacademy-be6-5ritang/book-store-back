package com.nhnacademy.bookstoreback.payment.service.Impl;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookstoreback.auth.annotation.CurrentUser;
import com.nhnacademy.bookstoreback.auth.jwt.dto.CurrentUserDetails;
import com.nhnacademy.bookstoreback.global.exception.BookOrderFailException;
import com.nhnacademy.bookstoreback.global.exception.OrderFailException;
import com.nhnacademy.bookstoreback.global.exception.ParserFailException;
import com.nhnacademy.bookstoreback.global.exception.PaymentFailException;
import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;
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
import com.nhnacademy.bookstoreback.point.earningpolicy.domain.entity.PointEarningPolicy;
import com.nhnacademy.bookstoreback.point.earningpolicy.exception.PointEarningPolicyNotFoundException;
import com.nhnacademy.bookstoreback.point.earningpolicy.repository.PointEarningPolicyRepository;
import com.nhnacademy.bookstoreback.point.transaction.domain.entity.PointTransaction;
import com.nhnacademy.bookstoreback.point.transaction.repository.PointTransactionRepository;
import com.nhnacademy.bookstoreback.user.domain.entity.User;
import com.nhnacademy.bookstoreback.user.repository.UserRepository;
import com.nhnacademy.bookstoreback.usergrade.domain.entity.UserGrade;
import com.nhnacademy.bookstoreback.usergrade.repository.UserGradeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {
	public static final String ERROR_PARSER_FAIL = "파싱 실패";
	public static final String ERROR_PAYMENT_EXITS = "결제를 찾을 수 없습니다";
	public static final String ERROR_ORDER_EXITS = "주문 정보를 찾을 수 없습니다";
	public static final String ERROR_BOOKORDER_EXITS = "주문리스트를 찾을 수 없습니다";
	private final PaymentRepository paymentRepository;
	private final OrderRepository orderRepository;
	private final BookOrderRepository bookOrderRepository;
	private final OrderServiceImpl orderServiceImpl;
	private final PointTransactionRepository pointTransactionRepository;
	private final PointEarningPolicyRepository pointEarningPolicyRepository;
	private final UserRepository userRepository;
	private final UserGradeRepository userGradeRepository;

	@Override
	public PaymentSaveResponse savePaymentResponse(String paymentResponseJson,
		@CurrentUser CurrentUserDetails currentUser) {
		PaymentResponse paymentResponse = parsePaymentResponse(paymentResponseJson);
		Order order = orderRepository.findByOrderInfoId(paymentResponse.orderId());
		if (order == null) {
			ErrorStatus errorStatus = ErrorStatus.from(ERROR_ORDER_EXITS, HttpStatus.UNPROCESSABLE_ENTITY,
				LocalDateTime.now());
			throw new OrderFailException(errorStatus);
		}
		if (currentUser != null) {
			User user = userRepository.getReferenceById(currentUser.getUserId());

			if (!order.getOrderPointSale().equals(BigDecimal.ZERO)) {

				PointEarningPolicy pointEarningPolicy = pointEarningPolicyRepository.findByPointEarningPolicyType(
						"포인트 사용")
					.orElseThrow(
						() -> new PointEarningPolicyNotFoundException("포인트 사용"));
				pointTransactionRepository.save(PointTransaction.builder()
					.user(user)
					.pointEarningPolicy(pointEarningPolicy)
					.pointTransactionAmount(
						order.getOrderPointSale()
							.multiply(pointEarningPolicy.getPointEarningAmount(), MathContext.UNLIMITED))
					.build());
				user.updateOutPoints(order.getOrderPointSale());
			}

			PointEarningPolicy pointEarningPolicy = pointEarningPolicyRepository.findByPointEarningPolicyType(
					user.getUserGrade().getUserGradeName())
				.orElseThrow(
					() -> new PointEarningPolicyNotFoundException(user.getUserGrade().getUserGradeName()));
			pointTransactionRepository.save(PointTransaction.builder()
				.user(user)
				.pointEarningPolicy(pointEarningPolicy)
				.pointTransactionAmount(
					order.getOrderPrice()
						.multiply(pointEarningPolicy.getPointEarningAmount()
								.divide(new BigDecimal(100), new MathContext(1, RoundingMode.HALF_UP)),
							MathContext.UNLIMITED))
				.build());
			user.updatePoints(
				order.getOrderPrice()
					.multiply(pointEarningPolicy.getPointEarningAmount()
							.divide(new BigDecimal(100), new MathContext(1, RoundingMode.HALF_UP)),
						MathContext.UNLIMITED));

			BigDecimal updatedOrderPrice = orderServiceImpl.getTotalOrderPrice(currentUser);

			updateUserGrade(updatedOrderPrice, user);

			userRepository.save(user);
		}
		orderServiceImpl.updateOrderStatus(order.getOrderId(), 1L);
		return PaymentSaveResponse.from(paymentRepository.save(
			Payment.toEntity(paymentResponse.paymentKey(), order, paymentResponse.amount(), paymentResponse.status(),
				paymentResponse.date())));
	}

	private void updateUserGrade(BigDecimal updatedOrderPrice, User user) {
		List<UserGrade> userGrades = userGradeRepository.findAll();
		for (UserGrade userGrade : userGrades) {
			if (userGrade.getUserGradeMinAmount().compareTo(updatedOrderPrice) <= 0
				&& updatedOrderPrice.compareTo(userGrade.getUserGradeMaxAmount()) < 0) {
				user.updateUserGrade(userGrade);
				break;
			}
		}
	}

	// private String getNextGrade(User user) {
	// 	String gradeName = user.getUserGrade().getUserGradeName();
	// 	String nextGrade = null;
	// 	if ("REGULAR".equals(gradeName)) {
	// 		nextGrade = "ROYAL";
	// 	} else if ("ROYAL".equals(gradeName)) {
	// 		nextGrade = "GRAND";
	// 	} else if ("GRAND".equals(gradeName)) {
	// 		nextGrade = "PRESTIGE";
	// 	}
	//
	// 	return nextGrade;
	// }

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
		List<BookOrder> bookOrder = bookOrderRepository.findByOrder_OrderInfoId(orderInfoId);
		if (bookOrder == null) {
			ErrorStatus errorStatus = ErrorStatus.from(ERROR_BOOKORDER_EXITS, HttpStatus.UNPROCESSABLE_ENTITY,
				LocalDateTime.now());
			throw new BookOrderFailException(errorStatus);
		}
		int quantity = bookOrder.size() - 1;
		String title = bookOrder.getFirst().getBook().getBookTitle() + "외" + quantity;
		return GetBookOrderByInfoIdResponse.from(title);
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
