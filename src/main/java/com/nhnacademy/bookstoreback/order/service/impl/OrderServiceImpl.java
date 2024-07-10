package com.nhnacademy.bookstoreback.order.service.impl;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nhnacademy.bookstoreback.auth.annotation.CurrentUser;
import com.nhnacademy.bookstoreback.auth.jwt.dto.CurrentUserDetails;
import com.nhnacademy.bookstoreback.cart.domain.entity.Cart;
import com.nhnacademy.bookstoreback.cart.repository.CartRepository;
import com.nhnacademy.bookstoreback.global.exception.OrderFailException;
import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;
import com.nhnacademy.bookstoreback.order.domain.dto.request.CreateOrderRequest;
import com.nhnacademy.bookstoreback.order.domain.dto.response.CreateOrderResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetAllListOrderByStatusResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetAllListOrderResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetNonOrderByInfoResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetOrderByInfoResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetOrderByStatusIdResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetOrderResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetUserPointOrderResponse;
import com.nhnacademy.bookstoreback.order.domain.entity.Order;
import com.nhnacademy.bookstoreback.order.domain.entity.OrderStatus;
import com.nhnacademy.bookstoreback.order.repository.OrderRepository;
import com.nhnacademy.bookstoreback.order.repository.OrderStatusRepository;
import com.nhnacademy.bookstoreback.order.service.OrderService;
import com.nhnacademy.bookstoreback.point.earningpolicy.domain.entity.PointEarningPolicy;
import com.nhnacademy.bookstoreback.point.earningpolicy.exception.PointEarningPolicyNotFoundException;
import com.nhnacademy.bookstoreback.point.earningpolicy.repository.PointEarningPolicyRepository;
import com.nhnacademy.bookstoreback.point.transaction.domain.entity.PointTransaction;
import com.nhnacademy.bookstoreback.point.transaction.repository.PointTransactionRepository;
import com.nhnacademy.bookstoreback.user.domain.entity.User;
import com.nhnacademy.bookstoreback.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class OrderServiceImpl implements OrderService {

	private final OrderRepository orderRepository;

	private final OrderStatusRepository orderStatusRepository;

	private final CartRepository cartRepository;

	private final UserRepository userRepository;

	private final PointTransactionRepository pointTransactionRepository;

	private final PointEarningPolicyRepository pointEarningPolicyRepository;

	public static final String ERROR_STATUS_WAIT = "주문 상태를 대기로 지정할 수 없습니다";
	public static final String ERROR_ORDER_EXITS = "주문을 가져올 수 없습니다";
	public static final String ERROR_ORDERS_EXITS = "주문 내역을 가져올 수 없습니다";
	public static final String ERROR_STATUS_EXITS = "주문 상태를 가져올 수 없습니다";
	public static final String ERROR_USER_EXITS = "사용자 정보를 가져올 수 없습니다";

	//카트 아이디를 가지고 있다면 그걸 사용해서 정보 추가로 가져오는 코드 추가 예정
	@Override
	public CreateOrderResponse createOrder(CreateOrderRequest createOrderRequest,
		@CurrentUser CurrentUserDetails currentUser) {
		List<OrderStatus> orderStatuses = orderStatusRepository.findAll();

		for (OrderStatus orderStatus : orderStatuses) {
			if (orderStatus.getOrderStatusName().equals("결제 대기")) {
				Order order = Order.toEntity(createOrderRequest, orderStatus);

				if (currentUser != null) {
					Cart cart = cartRepository.findByUser_Id(currentUser.getUserId());
					order.updateCart(cart);
					User user = userRepository.getReferenceById(currentUser.getUserId());

					if (!createOrderRequest.pointSale().equals(BigDecimal.ZERO)) {
						PointEarningPolicy pointEarningPolicy = pointEarningPolicyRepository.findByPointEarningPolicyType(
								"포인트 사용")
							.orElseThrow(
								() -> new PointEarningPolicyNotFoundException("포인트 사용"));
						pointTransactionRepository.save(PointTransaction.builder()
							.user(user)
							.pointEarningPolicy(pointEarningPolicy)
							.pointTransactionAmount(
								createOrderRequest.pointSale()
									.multiply(pointEarningPolicy.getPointEarningAmount(), MathContext.UNLIMITED))
							.build());
						user.updateOutPoints(createOrderRequest.pointSale());
					}

					PointEarningPolicy pointEarningPolicy = pointEarningPolicyRepository.findByPointEarningPolicyType(
							user.getUserGrade().getUserGradeName())
						.orElseThrow(
							() -> new PointEarningPolicyNotFoundException(user.getUserGrade().getUserGradeName()));
					pointTransactionRepository.save(PointTransaction.builder()
						.user(user)
						.pointEarningPolicy(pointEarningPolicy)
						.pointTransactionAmount(
							createOrderRequest.orderPrice()
								.multiply(pointEarningPolicy.getPointEarningAmount()
										.divide(new BigDecimal(100), new MathContext(1, RoundingMode.HALF_UP)),
									MathContext.UNLIMITED))
						.build());
					log.info("{}", createOrderRequest.orderPrice()
						.multiply(pointEarningPolicy.getPointEarningAmount()
								.divide(new BigDecimal(100), new MathContext(1, RoundingMode.HALF_UP)),
							MathContext.UNLIMITED));
					user.updatePoints(
						createOrderRequest.orderPrice()
							.multiply(pointEarningPolicy.getPointEarningAmount()
									.divide(new BigDecimal(100), new MathContext(1, RoundingMode.HALF_UP)),
								MathContext.UNLIMITED));

					userRepository.save(user);
					orderRepository.save(order);
					return CreateOrderResponse.from(order);
				} else {
					orderRepository.save(order);
					return CreateOrderResponse.from(order);
				}
			}
		}
		ErrorStatus errorStatus = ErrorStatus.from(ERROR_STATUS_WAIT, HttpStatus.UNPROCESSABLE_ENTITY,
			LocalDateTime.now());
		throw new OrderFailException(errorStatus);
	}

	// 특정 주문 가져오기
	@Override
	@Transactional(readOnly = true)
	public GetOrderResponse getOrder(Long orderId) {
		Order order = orderRepository.findById(orderId).orElse(null);
		if (order == null) {
			ErrorStatus errorStatus = ErrorStatus.from(ERROR_ORDER_EXITS, HttpStatus.NOT_FOUND, LocalDateTime.now());
			throw new OrderFailException(errorStatus);
		}
		return GetOrderResponse.from(order);
	}

	//관리자가 배송중이라는 주문 상태를 찾는 jpa
	@Override
	@Transactional(readOnly = true)
	public GetOrderByStatusIdResponse findByOrderStatus_OrderStatusId(Long orderStatusId, Pageable pageable) {
		Page<Order> order = orderRepository.findByOrderStatus_OrderStatusId(orderStatusId, pageable);
		if (order == null) {
			ErrorStatus errorStatus = ErrorStatus.from(ERROR_STATUS_WAIT, HttpStatus.NOT_FOUND, LocalDateTime.now());
			throw new OrderFailException(errorStatus);
		}
		return GetOrderByStatusIdResponse.from(order);
	}

	@Override
	@Transactional(readOnly = true)
	public GetAllListOrderByStatusResponse findByOrderStatus(Long orderStatusId) {
		OrderStatus orderStatus = orderStatusRepository.findById(orderStatusId).orElse(null);
		if (orderStatus == null) {
			ErrorStatus errorStatus = ErrorStatus.from(ERROR_STATUS_EXITS, HttpStatus.NOT_FOUND, LocalDateTime.now());
			throw new OrderFailException(errorStatus);
		}
		List<Order> orders = orderRepository.findAllByOrderStatus_OrderStatusId(orderStatusId);
		return GetAllListOrderByStatusResponse.from(orders);
	}

	// 주문의 상태 변경
	@Override
	@Transactional(readOnly = true)
	public GetOrderResponse updateOrderStatus(Long orderId, Long orderStatusId) {
		Order order = orderRepository.findById(orderId).orElse(null);
		OrderStatus orderStatus = orderStatusRepository.findById(orderStatusId).orElse(null);
		if (order == null) {
			ErrorStatus errorStatus = ErrorStatus.from(ERROR_ORDER_EXITS, HttpStatus.NOT_FOUND, LocalDateTime.now());
			throw new OrderFailException(errorStatus);
		} else if (orderStatus == null) {
			ErrorStatus errorStatus = ErrorStatus.from(ERROR_STATUS_WAIT, HttpStatus.NOT_FOUND, LocalDateTime.now());
			throw new OrderFailException(errorStatus);
		}
		order.updateOrderStatus(orderStatus);
		return GetOrderResponse.from(orderRepository.save(order));
	}

	@Override
	@Transactional(readOnly = true)
	public GetAllListOrderResponse findAllByCartId(Long cartId) {
		List<Order> orders = orderRepository.findAllByCart_CartId(cartId);
		if (orders == null) {
			ErrorStatus errorStatus = ErrorStatus.from(ERROR_ORDERS_EXITS, HttpStatus.NOT_FOUND, LocalDateTime.now());
			throw new OrderFailException(errorStatus);
		}
		return GetAllListOrderResponse.from(orders);
	}

	@Override
	@Transactional(readOnly = true)
	public GetAllListOrderResponse findAllUserId(@CurrentUser CurrentUserDetails currentUserDetails) {
		if (currentUserDetails == null) {
			ErrorStatus errorStatus = ErrorStatus.from(ERROR_USER_EXITS, HttpStatus.NOT_FOUND, LocalDateTime.now());
			throw new OrderFailException(errorStatus);
		}
		List<Order> orders = orderRepository.findAllByCart_UserId(currentUserDetails.getUserId());
		if (orders == null) {
			ErrorStatus errorStatus = ErrorStatus.from(ERROR_ORDERS_EXITS, HttpStatus.NOT_FOUND, LocalDateTime.now());
			throw new OrderFailException(errorStatus);
		}
		return GetAllListOrderResponse.from(orders);
	}

	@Override
	@Transactional(readOnly = true)
	public GetOrderByInfoResponse findByOrderInfoId(String orderInfoId) {
		Order order = orderRepository.findByOrderInfoId(orderInfoId);
		if (order == null) {
			ErrorStatus errorStatus = ErrorStatus.from(ERROR_ORDER_EXITS, HttpStatus.NOT_FOUND, LocalDateTime.now());
			throw new OrderFailException(errorStatus);
		}
		return GetOrderByInfoResponse.from(order);
	}

	@Override
	@Transactional(readOnly = true)
	public GetNonOrderByInfoResponse findByOrderInfoIdByEmail(String orderInfoId, String email) {
		Order order = orderRepository.findByOrderInfoId(orderInfoId);
		if (order == null) {
			ErrorStatus errorStatus = ErrorStatus.from(ERROR_ORDER_EXITS, HttpStatus.NOT_FOUND, LocalDateTime.now());
			throw new OrderFailException(errorStatus);
		} else if (!order.getOrderPayerEmail().equals(email)) {
			ErrorStatus errorStatus = ErrorStatus.from(ERROR_ORDER_EXITS, HttpStatus.NOT_FOUND, LocalDateTime.now());
			throw new OrderFailException(errorStatus);
		}
		return GetNonOrderByInfoResponse.from(order);
	}

	@Override
	@Transactional(readOnly = true)
	public GetUserPointOrderResponse getUserPoint(@CurrentUser CurrentUserDetails currentUserDetails) {
		if (currentUserDetails == null) {
			ErrorStatus errorStatus = ErrorStatus.from(ERROR_USER_EXITS, HttpStatus.NOT_FOUND, LocalDateTime.now());
			throw new OrderFailException(errorStatus);
		}
		User user = userRepository.getReferenceById(currentUserDetails.getUserId());
		return GetUserPointOrderResponse.from(user.getPoints());
	}
}
