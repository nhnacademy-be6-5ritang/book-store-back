package com.nhnacademy.bookstoreback.order.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nhnacademy.bookstoreback.auth.annotation.CurrentUser;
import com.nhnacademy.bookstoreback.auth.jwt.dto.CurrentUserDetails;
import com.nhnacademy.bookstoreback.delivery.domain.entity.Delivery;
import com.nhnacademy.bookstoreback.delivery.repository.DeliveryRepository;
import com.nhnacademy.bookstoreback.deliverystatus.domain.entity.DeliveryStatus;
import com.nhnacademy.bookstoreback.deliverystatus.exception.DeliveryStatusNotFoundException;
import com.nhnacademy.bookstoreback.deliverystatus.repository.DeliveryStatusRepository;
import com.nhnacademy.bookstoreback.global.exception.NotFoundException;
import com.nhnacademy.bookstoreback.global.exception.OrderFailException;
import com.nhnacademy.bookstoreback.global.exception.OrderStatusFailException;
import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;
import com.nhnacademy.bookstoreback.order.domain.dto.request.CreateOrderRequest;
import com.nhnacademy.bookstoreback.order.domain.dto.response.CreateCartOrderResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.CreateOrderResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetAllListOrderByStatusResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetAllListOrderResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetAllOrderResponse;
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
import com.nhnacademy.bookstoreback.point.transaction.service.impl.PointTransactionServiceImpl;
import com.nhnacademy.bookstoreback.user.domain.entity.User;
import com.nhnacademy.bookstoreback.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class OrderServiceImpl implements OrderService {

	public static final String ERROR_STATUS_WAIT = "주문 상태를 대기로 지정할 수 없습니다";
	public static final String ERROR_ORDER_EXITS = "주문을 가져올 수 없습니다";
	public static final String ERROR_ORDERS_EXITS = "주문 내역을 가져올 수 없습니다";
	public static final String ERROR_STATUS_EXITS = "주문 상태를 가져올 수 없습니다";
	public static final String ERROR_USER_EXITS = "사용자 정보를 가져올 수 없습니다";
	public static final String ERROR_DELIVERY_STATUS_NOTFUND = "배송 상태를 가져올 수 없습니다";
	public static final String ERROR_ORDER_STATUS_NOTFUND = "주문 상태를 가져올 수 없습니다";
	private final OrderRepository orderRepository;
	private final OrderStatusRepository orderStatusRepository;
	private final PointTransactionServiceImpl pointTransactionService;
	private final UserRepository userRepository;
	private final DeliveryRepository deliveryRepository;
	private final DeliveryStatusRepository deliveryStatusRepository;

	//카트 아이디를 가지고 있다면 그걸 사용해서 정보 추가로 가져오는 코드 추가 예정
	@Override
	public CreateOrderResponse createOrder(CreateOrderRequest createOrderRequest,
		@CurrentUser CurrentUserDetails currentUser) {
		List<OrderStatus> orderStatuses = orderStatusRepository.findAll();
		for (OrderStatus orderStatus : orderStatuses) {
			if (orderStatus.getOrderStatusName().equals("결제 대기")) {
				Order order = Order.toEntity(createOrderRequest, orderStatus);

				if (currentUser != null) {
					User user = userRepository.getReferenceById(currentUser.getUserId());
					order.updateUser(user);

					orderRepository.save(order);
					return CreateOrderResponse.from(order);
				} else {
					orderRepository.save(order);
					return CreateOrderResponse.from(order);
				}
			}
		}
		ErrorStatus errorStatus = ErrorStatus.from(ERROR_STATUS_WAIT, HttpStatus.NOT_FOUND,
			LocalDateTime.now());
		throw new OrderFailException(errorStatus);
	}

	@Override
	public CreateCartOrderResponse createCartOrder(@CurrentUser CurrentUserDetails currentUser) {
		Order order = Order.builder()
			.orderInfoId(UUID.randomUUID().toString())
			.build();
		if (currentUser != null) {
			User user = userRepository.getReferenceById(currentUser.getUserId());
			order.updateUser(user);
		}
		orderRepository.save(order);
		return CreateCartOrderResponse.from(order);
	}

	@Override
	public CreateOrderResponse updateCartOrder(CreateOrderRequest createOrderRequest, Long orderId) {
		List<OrderStatus> orderStatuses = orderStatusRepository.findAll();
		for (OrderStatus orderStatus : orderStatuses) {
			if (orderStatus.getOrderStatusName().equals("결제 대기")) {
				Order order = orderRepository.findByOrderId(orderId);

				if (order != null) {
					order.updateCartOrder(createOrderRequest, orderStatus);
					orderRepository.save(order);
					return CreateOrderResponse.from(order);
				} else {
					ErrorStatus errorStatus = ErrorStatus.from(ERROR_ORDER_EXITS, HttpStatus.NOT_FOUND,
						LocalDateTime.now());
					throw new OrderFailException(errorStatus);
				}
			}
		}
		ErrorStatus errorStatus = ErrorStatus.from(ERROR_STATUS_WAIT, HttpStatus.NOT_FOUND,
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
	public GetAllListOrderResponse findAllByUserId(Long userId) {
		List<Order> orders = orderRepository.findAllByUserId(userId);
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
		List<Order> orders = orderRepository.findAllByUserId(currentUserDetails.getUserId());
		if (orders == null) {
			ErrorStatus errorStatus = ErrorStatus.from(ERROR_ORDERS_EXITS, HttpStatus.NOT_FOUND, LocalDateTime.now());
			throw new OrderFailException(errorStatus);
		}
		List<GetAllOrderResponse> orderResponses = new ArrayList<>();
		for (Order order : orders) {
			if (order.getOrderPrice() != null && deliveryRepository.findByOrder_OrderId(order.getOrderId()) != null) {
				orderResponses.add(GetAllOrderResponse.from(order));
			}
		}
		return new GetAllListOrderResponse(orderResponses);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<GetAllOrderResponse> findAllPageByUserId(@CurrentUser CurrentUserDetails currentUserDetails,
		Pageable pageable) {
		if (currentUserDetails == null) {
			ErrorStatus errorStatus = ErrorStatus.from(ERROR_USER_EXITS, HttpStatus.NOT_FOUND, LocalDateTime.now());
			throw new OrderFailException(errorStatus);
		}
		int page = pageable.getPageNumber() > 0 ? pageable.getPageNumber() - 1 : 0;
		int size = pageable.isPaged() && pageable.getPageSize() > 0 ? pageable.getPageSize() : 10;
		Page<Order> orders = orderRepository.findAllByUser_Id(currentUserDetails.getUserId(),
			PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "orderDate")));
		if (orders == null) {
			ErrorStatus errorStatus = ErrorStatus.from(ERROR_ORDERS_EXITS, HttpStatus.NOT_FOUND, LocalDateTime.now());
			throw new OrderFailException(errorStatus);
		}
		return orders.map(GetAllOrderResponse::from);
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

	@Override
	public void refundedOrder(String orderInfoId) {
		Order order = orderRepository.findByOrderInfoId(orderInfoId);

		if (order == null) {
			ErrorStatus errorStatus = ErrorStatus.from(ERROR_ORDER_EXITS, HttpStatus.NOT_FOUND, LocalDateTime.now());
			throw new OrderFailException(errorStatus);
		}

		Delivery delivery = deliveryRepository.findByOrder_OrderId(order.getOrderId());

		if (delivery == null) {
			String errorMessage = "해당 주문은 아직 배송을 준비중입니다.";
			ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.NOT_FOUND, LocalDateTime.now());
			throw new NotFoundException(errorStatus);
		}

		DeliveryStatus deliveryStatus = deliveryStatusRepository.findDeliveryStatusByDeliveryStatusName("반품");
		delivery.updateDeliveryStatus(deliveryStatus);
		OrderStatus status = orderStatusRepository.findByOrderStatusName("반품");
		order.updateOrderStatus(status);

		pointTransactionService.refundPointTransaction(order.getUser(), order.getOrderPrice());
	}

	@Override
	public void refundingOrder(String orderInfoId) {
		Order order = orderRepository.findByOrderInfoId(orderInfoId);

		if (order == null) {
			ErrorStatus errorStatus = ErrorStatus.from(ERROR_ORDER_EXITS, HttpStatus.NOT_FOUND, LocalDateTime.now());
			throw new OrderFailException(errorStatus);
		}

		Delivery delivery = deliveryRepository.findByOrder_OrderId(order.getOrderId());

		if (delivery == null) {
			String errorMessage = "해당 주문은 아직 배송을 준비중입니다.";
			ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.NOT_FOUND, LocalDateTime.now());
			throw new NotFoundException(errorStatus);
		}

		DeliveryStatus deliveryStatus = deliveryStatusRepository.findDeliveryStatusByDeliveryStatusName("반품 요청중");
		if (deliveryStatus == null) {
			ErrorStatus errorStatus = ErrorStatus.from(ERROR_DELIVERY_STATUS_NOTFUND, HttpStatus.NOT_FOUND,
				LocalDateTime.now());
			throw new DeliveryStatusNotFoundException(errorStatus);
		}
		delivery.updateDeliveryStatus(deliveryStatus);
		OrderStatus status = orderStatusRepository.findByOrderStatusName("반품 요청중");
		if (status == null) {
			ErrorStatus errorStatus = ErrorStatus.from(ERROR_ORDER_STATUS_NOTFUND, HttpStatus.NOT_FOUND,
				LocalDateTime.now());
			throw new OrderStatusFailException(errorStatus);
		}
		order.updateOrderStatus(status);
	}

	@Override
	@Transactional(readOnly = true)
	public BigDecimal getTotalOrderPrice(CurrentUserDetails currentUser) {
		BigDecimal totalPaymentAmount = BigDecimal.ZERO;
		GetAllListOrderResponse allOrders = findAllUserId(currentUser);

		for (GetAllOrderResponse order : allOrders.orders()) {
			if (Objects.nonNull(order.orderPrice())) {
				totalPaymentAmount = totalPaymentAmount.add(order.orderPrice());
			}
		}

		return totalPaymentAmount;
	}
}
