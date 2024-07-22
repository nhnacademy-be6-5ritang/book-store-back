package com.nhnacademy.bookstoreback.delivery;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.TaskScheduler;

import com.nhnacademy.bookstoreback.delivery.domain.dto.request.CreateDeliveryRequest;
import com.nhnacademy.bookstoreback.delivery.domain.dto.request.GetDeliveriesRequest;
import com.nhnacademy.bookstoreback.delivery.domain.dto.request.UpdateDeliveryByOrderIdRequest;
import com.nhnacademy.bookstoreback.delivery.domain.dto.request.UpdateDeliveryRequest;
import com.nhnacademy.bookstoreback.delivery.domain.dto.response.CreateDeliveryResponse;
import com.nhnacademy.bookstoreback.delivery.domain.dto.response.GetDeliveryResponse;
import com.nhnacademy.bookstoreback.delivery.domain.dto.response.UpdateDeliveryAddOrderPolicyResponse;
import com.nhnacademy.bookstoreback.delivery.domain.dto.response.UpdateDeliveryResponse;
import com.nhnacademy.bookstoreback.delivery.domain.entity.Delivery;
import com.nhnacademy.bookstoreback.delivery.repository.DeliveryRepository;
import com.nhnacademy.bookstoreback.delivery.service.impl.DeliveryServiceImpl;
import com.nhnacademy.bookstoreback.deliverypolicy.domain.entity.DeliveryPolicy;
import com.nhnacademy.bookstoreback.deliverystatus.domain.entity.DeliveryStatus;
import com.nhnacademy.bookstoreback.deliverystatus.repository.DeliveryStatusRepository;
import com.nhnacademy.bookstoreback.global.exception.NotFoundException;
import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;
import com.nhnacademy.bookstoreback.order.domain.entity.Order;
import com.nhnacademy.bookstoreback.order.domain.entity.OrderStatus;
import com.nhnacademy.bookstoreback.order.repository.OrderRepository;
import com.nhnacademy.bookstoreback.order.repository.OrderStatusRepository;
import com.nhnacademy.bookstoreback.order.service.impl.OrderServiceImpl;

class DeliveryServiceImplTest {

	@Mock
	private DeliveryRepository deliveryRepository;

	@Mock
	private OrderRepository orderRepository;

	@Mock
	private DeliveryStatusRepository deliveryStatusRepository;

	private DeliveryPolicy deliveryPolicy;

	@Mock
	private TaskScheduler taskScheduler;

	@Mock
	private OrderServiceImpl orderServiceImpl;

	@Mock
	private OrderStatusRepository orderStatusRepository;

	@InjectMocks
	private DeliveryServiceImpl deliveryService;

	private Delivery delivery;
	private DeliveryStatus deliveryStatus;
	private Order order;
	private static final String NOT_FOUND_MESSAGE_DELIVERY_STATUS = "존재하지 않는 배달 상태입니다.";

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);

		deliveryStatus = mock(DeliveryStatus.class);

		order = mock(Order.class);

		deliveryPolicy = mock(DeliveryPolicy.class);

		delivery = Delivery.builder()
			.deliverySenderName("Sender Name")
			.deliverySenderPhone("1234567890")
			.deliverySenderDate(LocalDateTime.now())
			.deliverySenderAddress("123 Sender St")
			.deliveryReceiver("Receiver Name")
			.deliveryReceiverPhone("0987654321")
			.deliveryReceiverDate(LocalDateTime.now())
			.deliveryReceiverAddress("456 Receiver Ave")
			.order(order)
			.deliveryStatus(deliveryStatus)
			.deliveryPolicy(deliveryPolicy)
			.build();
	}

	@Test
	void testCreateDelivery_Success() {
		CreateDeliveryRequest request = new CreateDeliveryRequest(
			"Receiver Name", "0987654321", LocalDateTime.now(), "456 Receiver Ave", "456 Receiver Ave");

		when(deliveryStatusRepository.findDeliveryStatusByDeliveryStatusName(any()))
			.thenReturn(deliveryStatus);
		when(deliveryRepository.save(any(Delivery.class)))
			.thenReturn(delivery);

		CreateDeliveryResponse response = deliveryService.createDelivery(request);

		verify(deliveryRepository, times(1)).save(any(Delivery.class));
		assertEquals("Receiver Name", response.deliveryReceiver());
	}

	@Test
	void testGetDelivery_Success() {
		Long deliveryId = 1L;
		when(deliveryRepository.findById(anyLong())).thenReturn(Optional.of(delivery));

		GetDeliveryResponse response = deliveryService.getDelivery(deliveryId);

		verify(deliveryRepository, times(1)).findById(anyLong());
		assertEquals("Sender Name", response.deliverySenderName());
	}

	@Test
	void testUpdateDelivery_Success() {
		Long deliveryId = 1L;
		Long deliveryStatusId = 2L;
		String deliveryStatusName = "DELIVERED";

		// DeliveryStatus 객체 설정
		DeliveryStatus deliveryStatus = new DeliveryStatus(deliveryStatusName);

		// Delivery 객체 설정
		Delivery delivery = Delivery.builder()
			.deliveryStatus(deliveryStatus)
			.build();

		// UpdateDeliveryRequest 설정
		UpdateDeliveryRequest request = new UpdateDeliveryRequest(deliveryStatusId);

		// Mock 설정
		when(deliveryRepository.findById(anyLong())).thenReturn(Optional.of(delivery));
		when(deliveryStatusRepository.findById(anyLong())).thenReturn(Optional.of(deliveryStatus));
		when(deliveryRepository.save(any(Delivery.class))).thenReturn(delivery);

		// 서비스 호출
		UpdateDeliveryResponse response = deliveryService.updateDelivery(deliveryId, request);

		// Mock 검증
		verify(deliveryRepository, times(1)).save(any(Delivery.class));

		assertEquals(deliveryStatusName, response.deliveryStatusName());
	}

	@Test
	void testDeleteDelivery_Success() {
		Long deliveryId = 1L;
		doNothing().when(deliveryRepository).deleteById(anyLong());

		deliveryService.deleteDelivery(deliveryId);

		verify(deliveryRepository, times(1)).deleteById(anyLong());
	}

	@Test
	void testScheduleDeliveries_Success() {

		when(delivery.getDeliveryStatus().getDeliveryStatusId()).thenReturn(2L);

		deliveryService.scheduleDeliveries(delivery);

		verify(taskScheduler, times(1)).schedule(any(Runnable.class), any(Instant.class));
	}

	@Test
	void testScheduleDeliveries_DeliverySenderDateNull() {
		Delivery deliveryWithNullSenderDate = Delivery.builder()
			.deliverySenderName("Sender Name")
			.deliverySenderPhone("1234567890")
			.deliverySenderDate(null) // Setting DeliverySenderDate as null
			.deliverySenderAddress("123 Sender St")
			.deliveryReceiver("Receiver Name")
			.deliveryReceiverPhone("0987654321")
			.deliveryReceiverDate(LocalDateTime.now())
			.deliveryReceiverAddress("456 Receiver Ave")
			.order(order)
			.deliveryStatus(deliveryStatus)
			.deliveryPolicy(deliveryPolicy)
			.build();

		when(deliveryStatus.getDeliveryStatusId()).thenReturn(2L);

		deliveryService.scheduleDeliveries(deliveryWithNullSenderDate);

		verify(taskScheduler, never()).schedule(any(Runnable.class), any(Instant.class));
	}

	@Test
	void testUpdateDelivery_DeliveryNotFound() {
		Long deliveryId = 1L;
		Long deliveryStatusId = 2L;
		UpdateDeliveryRequest request = new UpdateDeliveryRequest(deliveryStatusId);

		when(deliveryRepository.findById(anyLong())).thenReturn(Optional.empty());

		assertThrows(NotFoundException.class, () -> deliveryService.updateDelivery(deliveryId, request));
	}

	@Test
	void testUpdateDelivery_DeliveryStatusNotFound() {
		Long deliveryId = 1L;
		Long deliveryStatusId = 2L;
		String deliveryStatusName = "DELIVERED";

		Delivery delivery = new Delivery();
		when(deliveryRepository.findById(anyLong())).thenReturn(Optional.of(delivery));
		when(deliveryStatusRepository.findById(anyLong())).thenReturn(Optional.empty());

		UpdateDeliveryRequest request = new UpdateDeliveryRequest(deliveryStatusId);

		assertThrows(NotFoundException.class, () -> deliveryService.updateDelivery(deliveryId, request));
	}

	@Test
	void testCreateDelivery_DeliveryStatusNotFound() {
		CreateDeliveryRequest request = new CreateDeliveryRequest(
			"Receiver Name", "0987654321", LocalDateTime.now(), "456 Receiver Ave", "456 Receiver Ave");

		when(deliveryStatusRepository.findDeliveryStatusByDeliveryStatusName(any()))
			.thenThrow(new NotFoundException(
				ErrorStatus.from(NOT_FOUND_MESSAGE_DELIVERY_STATUS, HttpStatus.NOT_FOUND, LocalDateTime.now())));

		assertThrows(NotFoundException.class, () -> deliveryService.createDelivery(request));
	}

	@Test
	void testScheduleDeliveries_DeliveryStatusIdNot2() {
		when(deliveryStatus.getDeliveryStatusId()).thenReturn(1L); // Non-2L status ID

		deliveryService.scheduleDeliveries(delivery);

		verify(taskScheduler, never()).schedule(any(Runnable.class), any(Instant.class));
	}

	@Test
	void testUpdateDeliveryAddOrder_Success() {
		Long deliveryId = 1L;
		Long orderId = 2L;

		when(deliveryRepository.findById(anyLong())).thenReturn(Optional.of(delivery));
		when(orderRepository.findByOrderId(anyLong())).thenReturn(order);

		UpdateDeliveryAddOrderPolicyResponse response = deliveryService.updateDeliveryAddOrder(deliveryId, orderId);

		verify(deliveryRepository, times(1)).save(any(Delivery.class));
		assertNotNull(response);
	}

	@Test
	void testUpdateDeliveryByOrderId_Success() {
		Long orderId = 1L;
		UpdateDeliveryByOrderIdRequest request = new UpdateDeliveryByOrderIdRequest(
			"Sender Name", "1234567890", "123 Sender St", "123 Sender St");

		Delivery delivery = mock(Delivery.class);
		DeliveryStatus deliveryStatus = mock(DeliveryStatus.class);

		when(deliveryRepository.findByOrder_OrderId(anyLong())).thenReturn(delivery);
		when(deliveryStatusRepository.getReferenceById(anyLong())).thenReturn(deliveryStatus);

		deliveryService.updateDeliveryByOrderId(orderId, request);

		verify(delivery, times(1)).updateDeliverySender(
			anyString(), anyString(), anyString(), any(DeliveryStatus.class));
		verify(orderServiceImpl, times(1)).updateOrderStatus(anyLong(), anyLong());
	}

	@Test
	void testCompleteDelivery_Success() {
		Long orderId = 1L;
		Delivery delivery = mock(Delivery.class);
		Order order = mock(Order.class);
		DeliveryStatus completedDeliveryStatus = mock(DeliveryStatus.class);
		OrderStatus completedOrderStatus = mock(OrderStatus.class);

		when(deliveryRepository.findByOrder_OrderId(anyLong())).thenReturn(delivery);
		when(orderRepository.findByOrderId(anyLong())).thenReturn(order);
		when(deliveryStatusRepository.getReferenceById(anyLong())).thenReturn(completedDeliveryStatus);
		when(orderStatusRepository.getReferenceById(anyLong())).thenReturn(completedOrderStatus);

		deliveryService.completeDelivery(orderId);

		verify(delivery, times(1)).updateDeliveryStatus(any(DeliveryStatus.class));
		verify(order, times(1)).updateOrderStatus(any(OrderStatus.class));
		verify(deliveryRepository, times(1)).save(delivery);
		verify(orderRepository, times(1)).save(order);
	}

	@Test
	void testUpdateDelivery_DeliveryStatusUpdate() {
		Long deliveryId = 1L;
		Long deliveryStatusId = 3L;
		DeliveryStatus updatedDeliveryStatus = new DeliveryStatus("UPDATED_STATUS");

		Delivery delivery = Delivery.builder().build();
		UpdateDeliveryRequest request = new UpdateDeliveryRequest(deliveryStatusId);

		when(deliveryRepository.findById(anyLong())).thenReturn(Optional.of(delivery));
		when(deliveryStatusRepository.findById(anyLong())).thenReturn(Optional.of(updatedDeliveryStatus));
		when(deliveryRepository.save(any(Delivery.class))).thenReturn(delivery);

		UpdateDeliveryResponse response = deliveryService.updateDelivery(deliveryId, request);

		assertEquals("UPDATED_STATUS", response.deliveryStatusName());
		verify(deliveryRepository, times(1)).save(any(Delivery.class));
	}

	@Test
	void testCompleteDelivery_DeliveryOrOrderNotFound() {
		Long orderId = 1L;

		// Delivery not found scenario
		when(deliveryRepository.findByOrder_OrderId(anyLong()))
			.thenThrow(new NotFoundException(
				ErrorStatus.from("배송이 존재하지 않습니다.", HttpStatus.NOT_FOUND, LocalDateTime.now())));

		assertThrows(NotFoundException.class, () -> deliveryService.completeDelivery(orderId));

		// Reset mocks to clear previous interactions
		reset(deliveryRepository, orderRepository);

		// Order not found scenario
		when(deliveryRepository.findByOrder_OrderId(anyLong()))
			.thenReturn(delivery);
		when(orderRepository.findByOrderId(anyLong()))
			.thenThrow(new NotFoundException(
				ErrorStatus.from("주문이 존재하지 않습니다.", HttpStatus.NOT_FOUND, LocalDateTime.now())));

		assertThrows(NotFoundException.class, () -> deliveryService.completeDelivery(orderId));
	}

	@Test
	void testUpdateDeliveryByOrderId_OrderStatusNotFound() {
		Long orderId = 1L;
		UpdateDeliveryByOrderIdRequest request = new UpdateDeliveryByOrderIdRequest(
			"Sender Name", "1234567890", "123 Sender St", "123 Sender St");

		Delivery delivery = mock(Delivery.class);

		when(deliveryRepository.findByOrder_OrderId(anyLong())).thenReturn(delivery);
		when(orderServiceImpl.updateOrderStatus(anyLong(), anyLong())).thenThrow(new NotFoundException(
			ErrorStatus.from("주문 상태가 존재하지 않습니다.", HttpStatus.NOT_FOUND, LocalDateTime.now())));

		assertThrows(NotFoundException.class, () -> deliveryService.updateDeliveryByOrderId(orderId, request));
	}

	@Test
	void testGetDeliveriesByUserId_Success() {
		Long userId = 1L;
		Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "delivery_sender_date"));

		OrderStatus orderStatus = mock(OrderStatus.class);
		Order order = Order.builder().orderStatus(orderStatus).build();
		DeliveryStatus deliveryStatus = mock(DeliveryStatus.class);
		DeliveryPolicy deliveryPolicy = mock(DeliveryPolicy.class);

		Delivery delivery1 = Delivery.builder()
			.deliverySenderName("Sender 1")
			.deliverySenderPhone("1234567890")
			.deliverySenderDate(LocalDateTime.now())
			.deliveryReceiver("Receiver 1")
			.deliveryReceiverPhone("0987654321")
			.order(order)
			.deliveryStatus(deliveryStatus)
			.deliveryPolicy(deliveryPolicy)
			.build();

		Delivery delivery2 = Delivery.builder()
			.deliverySenderName("Sender 2")
			.deliverySenderPhone("1234567890")
			.deliverySenderDate(LocalDateTime.now())
			.deliveryReceiver("Receiver 2")
			.deliveryReceiverPhone("0987654321")
			.order(order)
			.deliveryStatus(deliveryStatus)
			.deliveryPolicy(deliveryPolicy)
			.build();

		List<Delivery> deliveries = List.of(delivery1, delivery2);
		Page<Delivery> deliveryPage = new PageImpl<>(deliveries, pageable, deliveries.size());

		GetDeliveriesRequest request = new GetDeliveriesRequest(userId);

		when(deliveryRepository.findAllByOrder_User_Id(anyLong(), any(Pageable.class)))
			.thenReturn(deliveryPage);

		Page<GetDeliveryResponse> responsePage = deliveryService.getDeliveriesByUserId(request, pageable);

		verify(deliveryRepository, times(1)).findAllByOrder_User_Id(eq(userId), any(Pageable.class));
		assertEquals(2, responsePage.getTotalElements());
		assertEquals("Sender 1", responsePage.getContent().get(0).deliverySenderName());
		assertEquals("Sender 2", responsePage.getContent().get(1).deliverySenderName());
	}

	@Test
	void testGetDeliveriesByUserId_InvalidPageRequest() {
		Long userId = 1L;
		Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "delivery_sender_date"));

		GetDeliveriesRequest request = new GetDeliveriesRequest(userId);

		// Simulate no deliveries found for invalid page request
		when(deliveryRepository.findAllByOrder_User_Id(anyLong(), any(Pageable.class)))
			.thenReturn(Page.empty());

		Page<GetDeliveryResponse> responsePage = deliveryService.getDeliveriesByUserId(request, pageable);

		verify(deliveryRepository, times(1)).findAllByOrder_User_Id(anyLong(), any(Pageable.class));
		assertTrue(responsePage.getContent().isEmpty());
	}

	@Test
	void testGetDeliveryById_Success() {
		Long deliveryId = 1L;

		OrderStatus orderStatus = mock(OrderStatus.class);
		Order order = Order.builder().orderStatus(orderStatus).build();
		DeliveryStatus deliveryStatus = mock(DeliveryStatus.class);
		DeliveryPolicy deliveryPolicy = mock(DeliveryPolicy.class);

		Delivery delivery = Delivery.builder()
			.deliverySenderName("Sender")
			.deliverySenderPhone("1234567890")
			.deliverySenderDate(LocalDateTime.now())
			.deliveryReceiver("Receiver")
			.deliveryReceiverPhone("0987654321")
			.order(order)
			.deliveryStatus(deliveryStatus)
			.deliveryPolicy(deliveryPolicy)
			.build();

		when(deliveryRepository.findById(deliveryId))
			.thenReturn(java.util.Optional.of(delivery));

		GetDeliveryResponse response = deliveryService.getDelivery(deliveryId);

		verify(deliveryRepository, times(1)).findById(deliveryId);
		assertNotNull(response);
		assertEquals("Sender", response.deliverySenderName());
		assertEquals("Receiver", response.deliveryReceiver());
	}

	@Test
	void testGetDeliveryById_NotFound() {
		Long deliveryId = 1L;

		when(deliveryRepository.findById(deliveryId))
			.thenReturn(Optional.empty());

		NotFoundException thrownException = assertThrows(NotFoundException.class, () -> {
			deliveryService.getDelivery(deliveryId);
		});

		String expectedMessage = String.format("해당 배송 '%s'은 존재하지 않는 배송입니다.", deliveryId);
		assertEquals(expectedMessage, thrownException.getErrorStatus().getMessage());

		assertEquals(HttpStatus.NOT_FOUND, thrownException.getErrorStatus().getStatus());
	}

	@Test
	public void testScheduleDeliveries() {
		// Arrange
		Delivery delivery = mock(Delivery.class);
		DeliveryStatus status = mock(DeliveryStatus.class);
		Order order = mock(Order.class);

		LocalDateTime senderDate = LocalDateTime.now();
		Instant instant = senderDate.plusMinutes(2).atZone(ZoneId.systemDefault()).toInstant();

		when(delivery.getDeliverySenderDate()).thenReturn(senderDate);
		when(delivery.getDeliveryStatus()).thenReturn(status);
		when(status.getDeliveryStatusId()).thenReturn(2L);
		when(delivery.getOrder()).thenReturn(order);
		when(order.getOrderId()).thenReturn(1L);

		deliveryService.scheduleDeliveries(delivery);

		verify(taskScheduler).schedule(any(Runnable.class), eq(instant));
	}

	@Test
	public void testUpdateDeliveryAddOrder() {
		Long deliveryId = 1L;
		Long orderId = 2L;

		Delivery delivery = mock(Delivery.class);
		Order order = mock(Order.class);

		UpdateDeliveryAddOrderPolicyResponse response = new UpdateDeliveryAddOrderPolicyResponse(deliveryId);

		when(deliveryRepository.findById(deliveryId)).thenReturn(Optional.of(delivery));
		when(orderRepository.findByOrderId(orderId)).thenReturn(order);

		when(delivery.getDeliveryId()).thenReturn(deliveryId);
		doNothing().when(delivery).updateDeliveryAddOrder(order);
		when(deliveryRepository.save(delivery)).thenReturn(delivery);

		UpdateDeliveryAddOrderPolicyResponse result = deliveryService.updateDeliveryAddOrder(deliveryId, orderId);

		assertNotNull(result);
		assertEquals(response, result);

		verify(deliveryRepository).findById(deliveryId);
		verify(orderRepository).findByOrderId(orderId);
		verify(delivery).updateDeliveryAddOrder(order);
		verify(deliveryRepository).save(delivery);
	}

	@Test
	public void testUpdateDeliveryAddOrder_DeliveryNotFound() {
		Long deliveryId = 1L;
		Long orderId = 2L;

		when(deliveryRepository.findById(deliveryId))
			.thenReturn(Optional.empty());

		// 예외가 발생하는지 확인
		NotFoundException thrownException = assertThrows(NotFoundException.class, () -> {
			deliveryService.updateDeliveryAddOrder(deliveryId, orderId);
		});

		String expectedMessage = String.format("해당 배송 '%s'은 존재하지 않는 배송입니다.", deliveryId);
		assertEquals(expectedMessage, thrownException.getErrorStatus().getMessage());

		assertEquals(HttpStatus.NOT_FOUND, thrownException.getErrorStatus().getStatus());
	}

	@Test
	void testGetDeliveryByOrderId_Success() {
		Long orderId = 1L;

		// 배송 엔티티 설정
		Delivery delivery = Delivery.builder()
			.deliverySenderName("John Doe")
			.deliverySenderPhone("1234567890")
			.deliverySenderDate(LocalDateTime.now().minusDays(1))
			.deliverySenderAddress("123 Sender St")
			.deliveryReceiver("Jane Doe")
			.deliveryReceiverPhone("0987654321")
			.deliveryReceiverDate(LocalDateTime.now())
			.deliveryReceiverAddress("456 Receiver Ave")
			.order(order)
			.deliveryStatus(deliveryStatus)
			.deliveryPolicy(deliveryPolicy)
			.build();

		// Repository에서 배송 엔티티를 반환하도록 설정
		when(deliveryRepository.findByOrder_OrderId(orderId)).thenReturn(delivery);

		// 서비스 호출 및 결과 검증
		GetDeliveryResponse response = deliveryService.getDeliveryByOrderId(orderId);

		assertNotNull(delivery);
		assertEquals("John Doe", delivery.getDeliverySenderName());
		assertEquals("1234567890", delivery.getDeliverySenderPhone());
		assertNotNull(delivery.getDeliverySenderDate());
		assertEquals("123 Sender St", delivery.getDeliverySenderAddress());
		assertEquals("Jane Doe", delivery.getDeliveryReceiver());
		assertEquals("0987654321", delivery.getDeliveryReceiverPhone());
		assertNotNull(delivery.getDeliveryReceiverDate());
		assertEquals("456 Receiver Ave", delivery.getDeliveryReceiverAddress());
		assertEquals(order, delivery.getOrder());
		assertEquals(deliveryStatus, delivery.getDeliveryStatus());
		assertEquals(deliveryPolicy, delivery.getDeliveryPolicy());
	}

}
