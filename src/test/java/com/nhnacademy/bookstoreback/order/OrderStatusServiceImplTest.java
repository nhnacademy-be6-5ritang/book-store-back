package com.nhnacademy.bookstoreback.order;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import com.nhnacademy.bookstoreback.global.exception.OrderStatusFailException;
import com.nhnacademy.bookstoreback.order.domain.dto.request.CreateOrderStatusRequest;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetOrderStatusResponse;
import com.nhnacademy.bookstoreback.order.domain.entity.OrderStatus;
import com.nhnacademy.bookstoreback.order.repository.OrderStatusRepository;
import com.nhnacademy.bookstoreback.order.service.impl.OrderStatusServiceImpl;

public class OrderStatusServiceImplTest {

	private final OrderStatusRepository orderStatusRepository = mock(OrderStatusRepository.class);
	private final OrderStatusServiceImpl orderStatusService = new OrderStatusServiceImpl(orderStatusRepository);

	@Test
	void testCreateOrderStatus() {
		CreateOrderStatusRequest request = mock(CreateOrderStatusRequest.class);
		OrderStatus orderStatus = mock(OrderStatus.class);

		// when(request.orderStatusName())가 "Test Status"를 반환하도록 설정
		when(request.orderStatusName()).thenReturn("Test Status");

		// OrderStatus.toEntity(request) 메서드를 모킹
		try (MockedStatic<OrderStatus> mockedStatic = Mockito.mockStatic(OrderStatus.class)) {
			mockedStatic.when(() -> OrderStatus.toEntity(request)).thenReturn(orderStatus);

			// orderStatusRepository.save(orderStatus) 메서드를 모킹
			when(orderStatusRepository.save(any(OrderStatus.class))).thenReturn(orderStatus);

			// GetOrderStatusResponse.from(orderStatus) 메서드를 모킹
			GetOrderStatusResponse response = orderStatusService.create(request);

			// 검증
			assertThat(response).isNotNull();
			verify(orderStatusRepository).save(any(OrderStatus.class));
		}
	}

	@Test
	void testUpdateOrderStatus_Success() {
		Long id = 1L;
		String newName = "Updated Status";
		CreateOrderStatusRequest request = mock(CreateOrderStatusRequest.class);
		OrderStatus existingOrderStatus = mock(OrderStatus.class);

		// Mockito 설정
		when(orderStatusRepository.findById(id)).thenReturn(Optional.of(existingOrderStatus));
		when(request.orderStatusName()).thenReturn(newName);

		// void 메서드에 대한 스텁 설정
		doNothing().when(existingOrderStatus).updateName(newName);
		when(orderStatusRepository.save(existingOrderStatus)).thenReturn(existingOrderStatus);

		// OrderStatusServiceImpl 호출
		GetOrderStatusResponse response = orderStatusService.update(request, id);

		// 검증
		assertThat(response).isNotNull();
		verify(orderStatusRepository).findById(id);
		verify(existingOrderStatus).updateName(newName);
		verify(orderStatusRepository).save(existingOrderStatus);
	}

	@Test
	void testUpdateOrderStatus_NotFound() {
		CreateOrderStatusRequest request = mock(CreateOrderStatusRequest.class);

		when(orderStatusRepository.findById(anyLong())).thenReturn(Optional.empty());

		assertThrows(OrderStatusFailException.class, () -> orderStatusService.update(request, 1L));

		verify(orderStatusRepository).findById(anyLong());
	}

	@Test
	void testFindById_Success() {
		OrderStatus orderStatus = mock(OrderStatus.class);

		when(orderStatusRepository.findById(anyLong())).thenReturn(Optional.of(orderStatus));

		GetOrderStatusResponse response = orderStatusService.findById(1L);

		assertThat(response).isNotNull();
		verify(orderStatusRepository).findById(anyLong());
	}

	@Test
	void testFindById_NotFound() {
		when(orderStatusRepository.findById(anyLong())).thenReturn(Optional.empty());

		assertThrows(OrderStatusFailException.class, () -> orderStatusService.findById(1L));

		verify(orderStatusRepository).findById(anyLong());
	}

	@Test
	void testFindAll() {
		OrderStatus orderStatus = mock(OrderStatus.class);
		when(orderStatusRepository.findAll()).thenReturn(Collections.singletonList(orderStatus));

		List<GetOrderStatusResponse> responses = orderStatusService.findAll();

		assertThat(responses).isNotEmpty();
		verify(orderStatusRepository).findAll();
	}
}
