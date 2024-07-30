package com.nhnacademy.bookstoreback.deliverystatus.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.nhnacademy.bookstoreback.deliverystatus.domain.dto.request.CreateDeliveryStatusRequest;
import com.nhnacademy.bookstoreback.deliverystatus.domain.dto.request.UpdateDeliveryStatusRequest;
import com.nhnacademy.bookstoreback.deliverystatus.domain.dto.response.GetDeliveryStatusResponse;
import com.nhnacademy.bookstoreback.deliverystatus.domain.entity.DeliveryStatus;
import com.nhnacademy.bookstoreback.deliverystatus.exception.DeliveryStatusAlreadyExistsException;
import com.nhnacademy.bookstoreback.deliverystatus.exception.DeliveryStatusNotFoundException;
import com.nhnacademy.bookstoreback.deliverystatus.repository.DeliveryStatusRepository;
import com.nhnacademy.bookstoreback.deliverystatus.service.impl.DeliveryStatusServiceImpl;

class DeliveryStatusServiceImplTest {

	@Mock
	private DeliveryStatusRepository deliveryStatusRepository;

	@InjectMocks
	private DeliveryStatusServiceImpl deliveryStatusService;

	private DeliveryStatus deliveryStatus;
	private CreateDeliveryStatusRequest createDeliveryStatusRequest;
	private UpdateDeliveryStatusRequest updateDeliveryStatusRequest;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		deliveryStatus = new DeliveryStatus("Shipped");
		createDeliveryStatusRequest = new CreateDeliveryStatusRequest("Shipped");
		updateDeliveryStatusRequest = new UpdateDeliveryStatusRequest("Delivered");
	}

	@Test
	void testGetDeliveryStatus() {
		when(deliveryStatusRepository.findById(anyLong())).thenReturn(Optional.of(deliveryStatus));

		GetDeliveryStatusResponse response = deliveryStatusService.getDeliveryStatus(1L);

		assertNotNull(response);
		assertEquals("Shipped", response.deliveryStatusName());
		verify(deliveryStatusRepository, times(1)).findById(1L);
	}

	@Test
	void testGetDeliveryStatus_NotFound() {
		when(deliveryStatusRepository.findById(anyLong())).thenReturn(Optional.empty());

		assertThrows(DeliveryStatusNotFoundException.class, () -> deliveryStatusService.getDeliveryStatus(1L));
		verify(deliveryStatusRepository, times(1)).findById(1L);
	}

	@Test
	void testGetDeliveryStatuses() {
		List<DeliveryStatus> deliveryStatuses = Collections.singletonList(deliveryStatus);
		when(deliveryStatusRepository.findAll()).thenReturn(deliveryStatuses);

		List<GetDeliveryStatusResponse> responses = deliveryStatusService.getDeliveryStatuses();

		assertNotNull(responses);
		assertEquals(1, responses.size());
		assertEquals("Shipped", responses.get(0).deliveryStatusName());
		verify(deliveryStatusRepository, times(1)).findAll();
	}

	@Test
	void testCreateDeliveryStatus() {
		when(deliveryStatusRepository.existsByDeliveryStatusName(anyString())).thenReturn(false);

		deliveryStatusService.createDeliveryStatus(createDeliveryStatusRequest);

		verify(deliveryStatusRepository, times(1)).save(any(DeliveryStatus.class));
	}

	@Test
	void testCreateDeliveryStatus_AlreadyExists() {
		when(deliveryStatusRepository.existsByDeliveryStatusName(anyString())).thenReturn(true);

		assertThrows(DeliveryStatusAlreadyExistsException.class,
			() -> deliveryStatusService.createDeliveryStatus(createDeliveryStatusRequest));
		verify(deliveryStatusRepository, times(1)).existsByDeliveryStatusName("Shipped");
	}

	@Test
	void testUpdateDeliveryStatus() {
		when(deliveryStatusRepository.findById(anyLong())).thenReturn(Optional.of(deliveryStatus));
		when(deliveryStatusRepository.existsByDeliveryStatusName(anyString())).thenReturn(false);

		deliveryStatusService.updateDeliveryStatus(1L, updateDeliveryStatusRequest);

		assertEquals("Delivered", deliveryStatus.getDeliveryStatusName());
		verify(deliveryStatusRepository, times(1)).findById(1L);
	}

	@Test
	void testUpdateDeliveryStatus_NotFound() {
		when(deliveryStatusRepository.findById(anyLong())).thenReturn(Optional.empty());

		assertThrows(DeliveryStatusNotFoundException.class,
			() -> deliveryStatusService.updateDeliveryStatus(1L, updateDeliveryStatusRequest));
		verify(deliveryStatusRepository, times(1)).findById(1L);
	}

	@Test
	void testUpdateDeliveryStatus_AlreadyExists() {
		when(deliveryStatusRepository.findById(anyLong())).thenReturn(Optional.of(deliveryStatus));
		when(deliveryStatusRepository.existsByDeliveryStatusName(anyString())).thenReturn(true);

		assertThrows(DeliveryStatusAlreadyExistsException.class,
			() -> deliveryStatusService.updateDeliveryStatus(1L, updateDeliveryStatusRequest));
		verify(deliveryStatusRepository, times(1)).findById(1L);
		verify(deliveryStatusRepository, times(1)).existsByDeliveryStatusName("Delivered");
	}

	@Test
	void testDeleteDeliveryStatus() {
		doNothing().when(deliveryStatusRepository).deleteById(anyLong());

		deliveryStatusService.deleteDeliveryStatus(1L);

		verify(deliveryStatusRepository, times(1)).deleteById(1L);
	}
}
