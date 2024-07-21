package com.nhnacademy.bookstoreback.point;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.transaction.annotation.Transactional;

import com.nhnacademy.bookstoreback.point.earningpolicy.domain.dto.request.CreatePointEarningPolicyRequest;
import com.nhnacademy.bookstoreback.point.earningpolicy.domain.dto.request.UpdatePointEarningPolicyRequest;
import com.nhnacademy.bookstoreback.point.earningpolicy.domain.dto.response.CreatePointEarningPolicyResponse;
import com.nhnacademy.bookstoreback.point.earningpolicy.domain.dto.response.GetPointEarningPolicyResponse;
import com.nhnacademy.bookstoreback.point.earningpolicy.domain.dto.response.UpdatePointEarningPolicyResponse;
import com.nhnacademy.bookstoreback.point.earningpolicy.domain.entity.PointEarningPolicy;
import com.nhnacademy.bookstoreback.point.earningpolicy.exception.PointEarningPolicyAlreadyExistsException;
import com.nhnacademy.bookstoreback.point.earningpolicy.exception.PointEarningPolicyNotFoundException;
import com.nhnacademy.bookstoreback.point.earningpolicy.repository.PointEarningPolicyRepository;
import com.nhnacademy.bookstoreback.point.earningpolicy.service.impl.PointEarningPolicyServiceImpl;

@Transactional
public class PointEarningPolicyServiceImplTest {

	@InjectMocks
	private PointEarningPolicyServiceImpl pointEarningPolicyService;

	@Mock
	private PointEarningPolicyRepository pointEarningPolicyRepository;

	private PointEarningPolicy policy;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		policy = new PointEarningPolicy(BigDecimal.TEN, "TYPE1");
		policy.activate();
	}

	@Test
	void createPointEarningPolicy_ShouldThrowException_WhenPolicyAlreadyExists() {
		CreatePointEarningPolicyRequest request = new CreatePointEarningPolicyRequest("TYPE1", BigDecimal.TEN);

		when(pointEarningPolicyRepository.existsByPointEarningPolicyType("TYPE1")).thenReturn(true);

		assertThrows(PointEarningPolicyAlreadyExistsException.class, () -> {
			pointEarningPolicyService.createPointEarningPolicy(request);
		});
	}

	@Test
	void createPointEarningPolicy_ShouldCreatePolicy_WhenPolicyDoesNotExist() {
		// Given
		String policyType = "TYPE2";
		CreatePointEarningPolicyRequest request = new CreatePointEarningPolicyRequest(policyType, BigDecimal.TEN);

		// Ensure that the repository will not find an existing policy with the same type
		when(pointEarningPolicyRepository.existsByPointEarningPolicyType(policyType)).thenReturn(false);

		// Create a policy with the correct type for the response
		PointEarningPolicy createdPolicy = new PointEarningPolicy(BigDecimal.TEN, policyType);
		createdPolicy.activate();

		// When
		when(pointEarningPolicyRepository.save(any(PointEarningPolicy.class))).thenReturn(createdPolicy);

		// Act
		CreatePointEarningPolicyResponse response = pointEarningPolicyService.createPointEarningPolicy(request);

		// Assert
		verify(pointEarningPolicyRepository).save(any(PointEarningPolicy.class));
		assertEquals(policyType, response.pointEarningPolicyType());
	}

	@Test
	void getPointEarningPolicies_ShouldReturnPolicies() {
		when(pointEarningPolicyRepository.findAll()).thenReturn(Collections.singletonList(policy));

		List<GetPointEarningPolicyResponse> responses = pointEarningPolicyService.getPointEarningPolicies();

		assertEquals(responses.size(), 1);
		assertEquals(responses.get(0).pointEarningPolicyType(), "TYPE1");
	}

	@Test
	void getPointEarningPolicy_ShouldReturnPolicy_WhenPolicyExists() {
		when(pointEarningPolicyRepository.findById(1L)).thenReturn(Optional.of(policy));

		GetPointEarningPolicyResponse response = pointEarningPolicyService.getPointEarningPolicy(1L);

		assertEquals(response.pointEarningPolicyType(), "TYPE1");
	}

	@Test
	void getPointEarningPolicy_ShouldThrowException_WhenPolicyDoesNotExist() {
		when(pointEarningPolicyRepository.findById(1L)).thenReturn(Optional.empty());

		assertThrows(PointEarningPolicyNotFoundException.class, () -> {
			pointEarningPolicyService.getPointEarningPolicy(1L);
		});
	}

	@Test
	void updatePointEarningPolicy_ShouldUpdatePolicy_WhenPolicyExists() {
		UpdatePointEarningPolicyRequest request = new UpdatePointEarningPolicyRequest("TYPE1", BigDecimal.ONE);

		when(pointEarningPolicyRepository.findById(1L)).thenReturn(Optional.of(policy));
		when(pointEarningPolicyRepository.save(any(PointEarningPolicy.class))).thenReturn(policy);

		UpdatePointEarningPolicyResponse response = pointEarningPolicyService.updatePointEarningPolicy(1L, request);

		verify(pointEarningPolicyRepository).save(any(PointEarningPolicy.class));
		assertEquals(response.pointEarningPolicyType(), "TYPE1");
	}

	@Test
	void updatePointEarningPolicy_ShouldThrowException_WhenPolicyDoesNotExist() {
		UpdatePointEarningPolicyRequest request = new UpdatePointEarningPolicyRequest("TYPE1", BigDecimal.ONE);

		when(pointEarningPolicyRepository.findById(1L)).thenReturn(Optional.empty());

		assertThrows(PointEarningPolicyNotFoundException.class, () -> {
			pointEarningPolicyService.updatePointEarningPolicy(1L, request);
		});
	}

	@Test
	void activatePointEarningPolicy_ShouldActivatePolicy_WhenPolicyExists() {
		when(pointEarningPolicyRepository.findById(1L)).thenReturn(Optional.of(policy));

		pointEarningPolicyService.activatePointEarningPolicy(1L);

		verify(pointEarningPolicyRepository).save(policy);
		assertEquals(policy.getPointEarningPolicyStatus(), "ACTIVE");
	}

	@Test
	void activatePointEarningPolicy_ShouldThrowException_WhenPolicyDoesNotExist() {
		when(pointEarningPolicyRepository.findById(1L)).thenReturn(Optional.empty());

		assertThrows(PointEarningPolicyNotFoundException.class, () -> {
			pointEarningPolicyService.activatePointEarningPolicy(1L);
		});
	}

	@Test
	void deactivatePointEarningPolicy_ShouldDeactivatePolicy_WhenPolicyExists() {
		when(pointEarningPolicyRepository.findById(1L)).thenReturn(Optional.of(policy));

		pointEarningPolicyService.deactivatePointEarningPolicy(1L);

		verify(pointEarningPolicyRepository).save(policy);
		assertEquals(policy.getPointEarningPolicyStatus(), "DORMANT");
	}

	@Test
	void deactivatePointEarningPolicy_ShouldThrowException_WhenPolicyDoesNotExist() {
		when(pointEarningPolicyRepository.findById(1L)).thenReturn(Optional.empty());

		assertThrows(PointEarningPolicyNotFoundException.class, () -> {
			pointEarningPolicyService.deactivatePointEarningPolicy(1L);
		});
	}

	@Test
	void deletePointEarningPolicy_ShouldDeletePolicy_WhenPolicyExists() {
		when(pointEarningPolicyRepository.findById(1L)).thenReturn(Optional.of(policy));

		pointEarningPolicyService.deletePointEarningPolicy(1L);

		verify(pointEarningPolicyRepository).delete(policy);
	}

	@Test
	void deletePointEarningPolicy_ShouldThrowException_WhenPolicyDoesNotExist() {
		when(pointEarningPolicyRepository.findById(1L)).thenReturn(Optional.empty());

		assertThrows(PointEarningPolicyNotFoundException.class, () -> {
			pointEarningPolicyService.deletePointEarningPolicy(1L);
		});
	}
}
