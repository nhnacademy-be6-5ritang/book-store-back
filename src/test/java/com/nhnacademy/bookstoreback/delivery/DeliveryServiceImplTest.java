package com.nhnacademy.bookstoreback.delivery;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.TaskScheduler;

import com.nhnacademy.bookstoreback.delivery.domain.dto.request.CreateDeliveryRequest;
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
import com.nhnacademy.bookstoreback.order.repository.OrderRepository;
import com.nhnacademy.bookstoreback.order.service.impl.OrderServiceImpl;

public class DeliveryServiceImplTest {

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

}
