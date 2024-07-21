package com.nhnacademy.bookstoreback.deliverypolicy.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.nhnacademy.bookstoreback.delivery.domain.entity.Delivery;
import com.nhnacademy.bookstoreback.delivery.exception.DeliveryNotFoundException;
import com.nhnacademy.bookstoreback.delivery.repository.DeliveryRepository;
import com.nhnacademy.bookstoreback.deliverypolicy.domain.dto.request.CreateDeliveryPolicyRequest;
import com.nhnacademy.bookstoreback.deliverypolicy.domain.dto.request.UpdateDeliveryPolicyRequest;
import com.nhnacademy.bookstoreback.deliverypolicy.domain.dto.response.GetDeliveryPoliciesResponse;
import com.nhnacademy.bookstoreback.deliverypolicy.domain.dto.response.GetDeliveryPolicyResponse;
import com.nhnacademy.bookstoreback.deliverypolicy.domain.entity.DeliveryPolicy;
import com.nhnacademy.bookstoreback.deliverypolicy.exception.DeliveryPolicyAlreadyExistsException;
import com.nhnacademy.bookstoreback.deliverypolicy.exception.DeliveryPolicyNotFoundException;
import com.nhnacademy.bookstoreback.deliverypolicy.repository.DeliveryPolicyRepository;
import com.nhnacademy.bookstoreback.deliverypolicy.service.impl.DeliveryPolicyServiceImpl;

@ExtendWith(MockitoExtension.class)
class DeliveryPolicyServiceImplTest {

	@InjectMocks
	private DeliveryPolicyServiceImpl deliveryPolicyService;

	@Mock
	private DeliveryPolicyRepository deliveryPolicyRepository;

	@Mock
	private DeliveryRepository deliveryRepository;

	private DeliveryPolicy deliveryPolicy;
	private Delivery delivery;
	private CreateDeliveryPolicyRequest createRequest;
	private UpdateDeliveryPolicyRequest updateRequest;

	@BeforeEach
	void setUp() {
		deliveryPolicy = new DeliveryPolicy(
			"Standard Policy",
			BigDecimal.valueOf(1000),
			"Standard Delivery Policy",
			BigDecimal.valueOf(5000)
		);

		delivery = new Delivery(); // 초기화는 필요에 따라 적절히 수정

		createRequest = new CreateDeliveryPolicyRequest(
			"Standard Policy",
			"Description",
			BigDecimal.valueOf(1000),
			BigDecimal.valueOf(5000)
		);

		updateRequest = new UpdateDeliveryPolicyRequest(
			"Updated Policy",
			"Description",
			BigDecimal.valueOf(2000),
			BigDecimal.valueOf(6000)
		);
	}

	@Test
	void testGetDeliveryPolicies() {
		when(deliveryPolicyRepository.findAll()).thenReturn(Collections.singletonList(deliveryPolicy));

		List<GetDeliveryPoliciesResponse> result = deliveryPolicyService.getDeliveryPolicies();
		assertEquals(1, result.size());
		assertEquals("Standard Policy", result.get(0).deliveryPolicyName());
	}

	@Test
	void testGetDeliveryPolicy_Success() {
		when(deliveryPolicyRepository.findById(anyLong())).thenReturn(Optional.of(deliveryPolicy));

		GetDeliveryPolicyResponse result = deliveryPolicyService.getDeliveryPolicy(1L);
		assertEquals("Standard Policy", result.deliveryPolicyName());
	}

	@Test
	void testGetDeliveryPolicy_NotFound() {
		when(deliveryPolicyRepository.findById(anyLong())).thenReturn(Optional.empty());

		assertThrows(DeliveryPolicyNotFoundException.class, () -> {
			deliveryPolicyService.getDeliveryPolicy(1L);
		});
	}

	@Test
	void testCreateDeliveryPolicy_Success() {
		when(deliveryPolicyRepository.existsByDeliveryPolicyName(anyString())).thenReturn(false);

		deliveryPolicyService.createDeliveryPolicy(createRequest);
	}

	@Test
	void testCreateDeliveryPolicy_AlreadyExists() {
		when(deliveryPolicyRepository.existsByDeliveryPolicyName(anyString())).thenReturn(true);

		assertThrows(DeliveryPolicyAlreadyExistsException.class, () -> {
			deliveryPolicyService.createDeliveryPolicy(createRequest);
		});
	}

	@Test
	void testUpdateDeliveryPolicy_Success() {
		when(deliveryPolicyRepository.findById(anyLong())).thenReturn(Optional.of(deliveryPolicy));
		when(deliveryPolicyRepository.existsByDeliveryPolicyName(anyString())).thenReturn(false);
		when(deliveryPolicyRepository.save(any(DeliveryPolicy.class))).thenReturn(deliveryPolicy);

		deliveryPolicyService.updateDeliveryPolicy(1L, updateRequest);
	}

	@Test
	void testUpdateDeliveryPolicy_NotFound() {
		when(deliveryPolicyRepository.findById(anyLong())).thenReturn(Optional.empty());

		assertThrows(DeliveryPolicyNotFoundException.class, () -> {
			deliveryPolicyService.updateDeliveryPolicy(1L, updateRequest);
		});
	}

	@Test
	void testUpdateDeliveryPolicy_AlreadyExists() {
		when(deliveryPolicyRepository.findById(anyLong())).thenReturn(Optional.of(deliveryPolicy));
		when(deliveryPolicyRepository.existsByDeliveryPolicyName(anyString())).thenReturn(true);

		assertThrows(DeliveryPolicyAlreadyExistsException.class, () -> {
			deliveryPolicyService.updateDeliveryPolicy(1L, updateRequest);
		});
	}

	@Test
	void testDeleteDeliveryPolicy_Success() {
		doNothing().when(deliveryPolicyRepository).deleteById(anyLong());

		deliveryPolicyService.deleteDeliveryPolicy(1L);

		verify(deliveryPolicyRepository, times(1)).deleteById(1L);
	}

	@Test
	void testFindByDeliveryPolicyStandardPriceLessThanEqualOrderByDeliveryPolicyStandardPriceDesc() {
		when(deliveryRepository.findById(anyLong())).thenReturn(Optional.of(delivery));
		when(
			deliveryPolicyRepository.findByDeliveryPolicyStandardPriceLessThanEqualOrderByDeliveryPolicyStandardPriceDesc(
				any()))
			.thenReturn(Collections.singletonList(deliveryPolicy));

		GetDeliveryPolicyResponse result = deliveryPolicyService.findByDeliveryPolicyStandardPriceLessThanEqualOrderByDeliveryPolicyStandardPriceDesc(
			1L, BigDecimal.valueOf(5000));
		assertEquals("Standard Policy", result.deliveryPolicyName());
	}

	@Test
	void testFindByDeliveryPolicyStandardPriceLessThanEqualOrderByDeliveryPolicyStandardPriceDesc_DeliveryNotFound() {
		when(deliveryRepository.findById(anyLong())).thenReturn(Optional.empty());

		assertThrows(DeliveryNotFoundException.class, () -> {
			deliveryPolicyService.findByDeliveryPolicyStandardPriceLessThanEqualOrderByDeliveryPolicyStandardPriceDesc(
				1L, BigDecimal.valueOf(5000));
		});
	}
}