package com.nhnacademy.bookstoreback.order;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.nhnacademy.bookstoreback.auth.jwt.dto.CurrentUserDetails;
import com.nhnacademy.bookstoreback.delivery.domain.entity.Delivery;
import com.nhnacademy.bookstoreback.delivery.repository.DeliveryRepository;
import com.nhnacademy.bookstoreback.deliverystatus.domain.entity.DeliveryStatus;
import com.nhnacademy.bookstoreback.deliverystatus.exception.DeliveryStatusNotFoundException;
import com.nhnacademy.bookstoreback.deliverystatus.repository.DeliveryStatusRepository;
import com.nhnacademy.bookstoreback.global.exception.NotFoundException;
import com.nhnacademy.bookstoreback.global.exception.OrderFailException;
import com.nhnacademy.bookstoreback.global.exception.OrderStatusFailException;
import com.nhnacademy.bookstoreback.order.domain.dto.request.CreateOrderRequest;
import com.nhnacademy.bookstoreback.order.domain.dto.response.CreateCartOrderResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.CreateOrderResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetAllListOrderResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetOrderResponse;
import com.nhnacademy.bookstoreback.order.domain.entity.Order;
import com.nhnacademy.bookstoreback.order.domain.entity.OrderStatus;
import com.nhnacademy.bookstoreback.order.repository.OrderRepository;
import com.nhnacademy.bookstoreback.order.repository.OrderStatusRepository;
import com.nhnacademy.bookstoreback.order.service.impl.OrderServiceImpl;
import com.nhnacademy.bookstoreback.user.domain.entity.User;
import com.nhnacademy.bookstoreback.user.repository.UserRepository;

public class OrderServiceImplTest {

	@InjectMocks
	private OrderServiceImpl orderService;

	@Mock
	private OrderRepository orderRepository;

	@Mock
	private OrderStatusRepository orderStatusRepository;

	@Mock
	private UserRepository userRepository;

	@Mock
	private DeliveryRepository deliveryRepository;

	@Mock
	private DeliveryStatusRepository deliveryStatusRepository;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testCreateOrder() {
		CreateOrderRequest request = mock(CreateOrderRequest.class);
		OrderStatus orderStatus = mock(OrderStatus.class);
		Order order = mock(Order.class);

		when(orderStatusRepository.findAll()).thenReturn(Collections.singletonList(orderStatus));
		when(orderStatus.getOrderStatusName()).thenReturn("결제 대기");
		when(orderRepository.save(any(Order.class))).thenReturn(order);
		when(order.getOrderId()).thenReturn(1L);

		CreateOrderResponse response = orderService.createOrder(request, null);

		assertThat(response).isNotNull();
		verify(orderStatusRepository).findAll();
		verify(orderRepository).save(any(Order.class));
	}

	@Test
	public void testUpdateCartOrder() {
		CreateOrderRequest request = mock(CreateOrderRequest.class);
		OrderStatus orderStatus = mock(OrderStatus.class);
		Order order = mock(Order.class);

		when(orderStatusRepository.findAll()).thenReturn(Collections.singletonList(orderStatus));
		when(orderStatus.getOrderStatusName()).thenReturn("결제 대기");
		when(orderRepository.findByOrderId(1L)).thenReturn(order);
		when(orderRepository.save(any(Order.class))).thenReturn(order);

		CreateOrderResponse response = orderService.updateCartOrder(request, 1L);

		assertThat(response).isNotNull();
		verify(orderStatusRepository).findAll();
		verify(orderRepository).findByOrderId(1L);
		verify(orderRepository).save(any(Order.class));
	}

	@Test
	public void testGetOrder() {
		Order order = mock(Order.class);

		when(orderRepository.findById(1L)).thenReturn(java.util.Optional.of(order));
		when(order.getOrderId()).thenReturn(1L);

		GetOrderResponse response = orderService.getOrder(1L);

		assertThat(response).isNotNull();
		verify(orderRepository).findById(1L);
	}

	@Test
	public void testGetOrder_NotFound() {
		when(orderRepository.findById(1L)).thenReturn(java.util.Optional.empty());

		assertThrows(OrderFailException.class, () -> orderService.getOrder(1L));

		verify(orderRepository).findById(1L);
	}

	@Test
	public void testUpdateOrderStatus() {
		Order order = mock(Order.class);
		OrderStatus orderStatus = mock(OrderStatus.class);

		when(orderRepository.findById(1L)).thenReturn(java.util.Optional.of(order));
		when(orderStatusRepository.findById(1L)).thenReturn(java.util.Optional.of(orderStatus));
		when(orderRepository.save(any(Order.class))).thenReturn(order);

		GetOrderResponse response = orderService.updateOrderStatus(1L, 1L);

		assertThat(response).isNotNull();
		verify(orderRepository).findById(1L);
		verify(orderStatusRepository).findById(1L);
		verify(orderRepository).save(any(Order.class));
	}

	@Test
	public void testFindAllByUserId() {
		List<Order> orders = Collections.singletonList(mock(Order.class));

		when(orderRepository.findAllByUserId(1L)).thenReturn(orders);

		GetAllListOrderResponse response = orderService.findAllByUserId(1L);

		assertThat(response).isNotNull();
		verify(orderRepository).findAllByUserId(1L);
	}

	@Test
	public void testFindAllUserId() {
		List<Order> orders = Collections.singletonList(mock(Order.class));

		when(orderRepository.findAllByUserId(1L)).thenReturn(orders);

		GetAllListOrderResponse response = orderService.findAllUserId(mock(CurrentUserDetails.class));

		assertThat(response).isNotNull();
		verify(orderRepository).findAllByUserId(anyLong());
	}

	@Test
	public void testCreateOrder_NoOrderStatusFound() {
		CreateOrderRequest request = mock(CreateOrderRequest.class);

		when(orderStatusRepository.findAll()).thenReturn(Collections.emptyList()); // No order statuses found

		assertThrows(OrderFailException.class, () -> orderService.createOrder(request, null));
	}

	@Test
	public void testGetOrder_OrderNotFound() {
		when(orderRepository.findById(1L)).thenReturn(java.util.Optional.empty());

		assertThrows(OrderFailException.class, () -> orderService.getOrder(1L));
	}

	@Test
	public void testUpdateOrderStatus_OrderNotFound() {
		when(orderRepository.findById(1L)).thenReturn(java.util.Optional.empty());

		assertThrows(OrderFailException.class, () -> orderService.updateOrderStatus(1L, 1L));
	}

	@Test
	public void testUpdateOrderStatus_OrderStatusNotFound() {
		when(orderRepository.findById(1L)).thenReturn(java.util.Optional.of(mock(Order.class)));
		when(orderStatusRepository.findById(1L)).thenReturn(java.util.Optional.empty()); // OrderStatus not found

		assertThrows(OrderFailException.class, () -> orderService.updateOrderStatus(1L, 1L));
	}

	@Test
	public void testFindAllByUserId_NoOrdersFound() {
		when(orderRepository.findAllByUserId(1L)).thenReturn(null); // No orders found

		assertThrows(OrderFailException.class, () -> orderService.findAllByUserId(1L));
	}

	@Test
	public void testFindAllUserId_CurrentUserNull() {
		assertThrows(OrderFailException.class, () -> orderService.findAllUserId(null));
	}

	@Test
	public void testFindAllUserId_NoOrdersFound() {
		when(orderRepository.findAllByUserId(anyLong())).thenReturn(null); // No orders found

		assertThrows(OrderFailException.class, () -> orderService.findAllUserId(mock(CurrentUserDetails.class)));
	}

	@Test
	public void testFindByOrderInfoId_OrderNotFound() {
		when(orderRepository.findByOrderInfoId("order123")).thenReturn(null); // Order not found

		assertThrows(OrderFailException.class, () -> orderService.findByOrderInfoId("order123"));
	}

	@Test
	public void testRefundedOrder_DeliveryNotFound() {
		Order order = mock(Order.class);

		when(orderRepository.findByOrderInfoId("order123")).thenReturn(order);
		when(deliveryRepository.findByOrder_OrderId(anyLong())).thenReturn(null); // Delivery not found

		assertThrows(NotFoundException.class, () -> orderService.refundedOrder("order123"));
	}

	@Test
	public void testCreateOrder_NoCurrentUser() {
		CreateOrderRequest request = mock(CreateOrderRequest.class);
		OrderStatus orderStatus = mock(OrderStatus.class);
		Order order = mock(Order.class);

		when(orderStatusRepository.findAll()).thenReturn(Collections.singletonList(orderStatus));
		when(orderStatus.getOrderStatusName()).thenReturn("결제 대기");
		when(orderRepository.save(any(Order.class))).thenReturn(order);
		when(order.getOrderId()).thenReturn(1L);

		CreateOrderResponse response = orderService.createOrder(request, null);

		assertThat(response).isNotNull();
		verify(orderStatusRepository).findAll();
		verify(orderRepository).save(any(Order.class));
	}

	@Test
	public void testCreateCartOrder_WithCurrentUser() {
		CurrentUserDetails currentUser = mock(CurrentUserDetails.class);
		User user = mock(User.class);
		Order order = mock(Order.class);

		when(currentUser.getUserId()).thenReturn(1L);
		when(userRepository.getReferenceById(1L)).thenReturn(user);
		when(orderRepository.save(any(Order.class))).thenReturn(order);

		CreateCartOrderResponse response = orderService.createCartOrder(currentUser);

		assertThat(response).isNotNull();
		verify(userRepository).getReferenceById(1L);
		verify(orderRepository).save(any(Order.class));
	}

	@Test
	public void testUpdateCartOrder_OrderNotFound() {
		CreateOrderRequest request = mock(CreateOrderRequest.class);
		OrderStatus orderStatus = mock(OrderStatus.class);

		when(orderStatusRepository.findAll()).thenReturn(Collections.singletonList(orderStatus));
		when(orderStatus.getOrderStatusName()).thenReturn("결제 대기");
		when(orderRepository.findByOrderId(1L)).thenReturn(null);

		assertThrows(OrderFailException.class, () -> orderService.updateCartOrder(request, 1L));
		verify(orderStatusRepository).findAll();
		verify(orderRepository).findByOrderId(1L);
	}

	@Test
	public void testUpdateOrderStatus_OrderStatusNull() {
		Order order = mock(Order.class);

		when(orderRepository.findById(1L)).thenReturn(java.util.Optional.of(order));
		when(orderStatusRepository.findById(1L)).thenReturn(java.util.Optional.empty());

		assertThrows(OrderFailException.class, () -> orderService.updateOrderStatus(1L, 1L));
		verify(orderRepository).findById(1L);
		verify(orderStatusRepository).findById(1L);
	}

	@Test
	public void testFindByOrderStatus_OrderStatusNull() {
		when(orderStatusRepository.findById(1L)).thenReturn(java.util.Optional.empty());

		assertThrows(OrderFailException.class, () -> orderService.findByOrderStatus(1L));
		verify(orderStatusRepository).findById(1L);
	}

	@Test
	public void testFindByOrderInfoIdByEmail_EmailMismatch() {
		Order order = mock(Order.class);

		when(orderRepository.findByOrderInfoId("order123")).thenReturn(order);
		when(order.getOrderPayerEmail()).thenReturn("different@example.com");

		assertThrows(OrderFailException.class,
			() -> orderService.findByOrderInfoIdByEmail("order123", "email@example.com"));
	}

	@Test
	public void testGetUserPoint_NullCurrentUser() {
		assertThat(orderService.getUserPoint(null)).isNull();
	}

	@Test
	public void testRefundingOrder_DeliveryStatusNotFound() {
		Order order = mock(Order.class);

		when(orderRepository.findByOrderInfoId("order123")).thenReturn(order);
		when(deliveryRepository.findByOrder_OrderId(anyLong())).thenReturn(mock(Delivery.class));
		when(deliveryStatusRepository.findDeliveryStatusByDeliveryStatusName("반품 요청중")).thenReturn(null);

		assertThrows(DeliveryStatusNotFoundException.class, () -> orderService.refundingOrder("order123"));
	}

	@Test
	public void testGetTotalOrderPrice_WithNullOrderPrice() {
		CurrentUserDetails currentUser = mock(CurrentUserDetails.class);
		Order order = mock(Order.class);

		when(currentUser.getUserId()).thenReturn(1L);
		when(orderRepository.findAllByUserId(1L)).thenReturn(Collections.singletonList(order));
		when(order.getOrderPrice()).thenReturn(null);

		assertThat(orderService.getTotalOrderPrice(currentUser)).isEqualByComparingTo(BigDecimal.ZERO);
	}

	@Test
	public void testCreateOrder_InvalidOrderStatus() {
		CreateOrderRequest request = mock(CreateOrderRequest.class);
		OrderStatus invalidOrderStatus = mock(OrderStatus.class);

		when(orderStatusRepository.findAll()).thenReturn(Collections.singletonList(invalidOrderStatus));
		when(invalidOrderStatus.getOrderStatusName()).thenReturn("Invalid Status");

		assertThrows(OrderFailException.class, () -> orderService.createOrder(request, null));
	}

	@Test
	public void testGetOrder_EmptyOptional() {
		when(orderRepository.findById(1L)).thenReturn(java.util.Optional.empty());

		assertThrows(OrderFailException.class, () -> orderService.getOrder(1L));
	}

	@Test
	public void testCreateOrder_ValidRequestWithCurrentUser() {
		CreateOrderRequest request = mock(CreateOrderRequest.class);
		OrderStatus orderStatus = mock(OrderStatus.class);
		Order order = mock(Order.class);
		CurrentUserDetails currentUser = mock(CurrentUserDetails.class);
		User user = mock(User.class);

		when(orderStatusRepository.findAll()).thenReturn(Collections.singletonList(orderStatus));
		when(orderStatus.getOrderStatusName()).thenReturn("결제 대기");
		when(orderRepository.save(any(Order.class))).thenReturn(order);
		when(order.getOrderId()).thenReturn(1L);
		when(currentUser.getUserId()).thenReturn(1L);
		when(userRepository.getReferenceById(1L)).thenReturn(user);

		CreateOrderResponse response = orderService.createOrder(request, currentUser);

		assertThat(response).isNotNull();
		verify(orderStatusRepository).findAll();
		verify(orderRepository).save(any(Order.class));
		verify(userRepository).getReferenceById(1L);
	}

	@Test
	public void testCreateCartOrder_WithNullCurrentUser() {
		Order order = mock(Order.class);

		when(orderRepository.save(any(Order.class))).thenReturn(order);

		CreateCartOrderResponse response = orderService.createCartOrder(null);

		assertThat(response).isNotNull();
		verify(orderRepository).save(any(Order.class));
	}

	@Test
	public void testUpdateOrderStatus_WithValidStatus() {
		Order order = mock(Order.class);
		OrderStatus orderStatus = mock(OrderStatus.class);

		when(orderRepository.findById(1L)).thenReturn(java.util.Optional.of(order));
		when(orderStatusRepository.findById(1L)).thenReturn(java.util.Optional.of(orderStatus));
		when(orderRepository.save(any(Order.class))).thenReturn(order);

		GetOrderResponse response = orderService.updateOrderStatus(1L, 1L);

		assertThat(response).isNotNull();
		verify(orderRepository).findById(1L);
		verify(orderStatusRepository).findById(1L);
		verify(orderRepository).save(any(Order.class));
	}

	@Test
	public void testRefundingOrder_DeliveryStatusAlreadyRefunded() {
		Order order = mock(Order.class);
		Delivery delivery = mock(Delivery.class);
		DeliveryStatus refundRequestedStatus = mock(DeliveryStatus.class);

		when(orderRepository.findByOrderInfoId("order123")).thenReturn(order);
		when(deliveryRepository.findByOrder_OrderId(anyLong())).thenReturn(delivery);
		when(deliveryStatusRepository.findDeliveryStatusByDeliveryStatusName("반품 요청중")).thenReturn(
			refundRequestedStatus);

		assertThrows(OrderStatusFailException.class, () -> orderService.refundingOrder("order123"));
	}

	@Test
	public void testGetTotalOrderPrice_NoOrdersFound() {
		CurrentUserDetails currentUser = mock(CurrentUserDetails.class);

		when(currentUser.getUserId()).thenReturn(1L);
		when(orderRepository.findAllByUserId(1L)).thenReturn(Collections.emptyList());

		assertThat(orderService.getTotalOrderPrice(currentUser)).isEqualByComparingTo(BigDecimal.ZERO);
	}

	@Test
	public void testGetUserPoint_UserNotFound() {
		assertThat(orderService.getUserPoint(null)).isNull();
	}
}