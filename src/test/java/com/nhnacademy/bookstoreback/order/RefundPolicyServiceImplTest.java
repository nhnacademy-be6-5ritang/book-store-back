package com.nhnacademy.bookstoreback.order;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import com.nhnacademy.bookstoreback.global.exception.RefundFailException;
import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;
import com.nhnacademy.bookstoreback.order.domain.dto.request.CreateRefundPolicyRequest;
import com.nhnacademy.bookstoreback.order.domain.dto.request.UpdateRefundPolicyRequest;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetAllRefundResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetRefundResponse;
import com.nhnacademy.bookstoreback.order.domain.entity.RefundPolicy;
import com.nhnacademy.bookstoreback.order.repository.RefundPolicyRepository;
import com.nhnacademy.bookstoreback.order.service.impl.RefundPolicyServiceImpl;

public class RefundPolicyServiceImplTest {

	@InjectMocks
	private RefundPolicyServiceImpl refundPolicyService;

	@Mock
	private RefundPolicyRepository refundPolicyRepository;

	@BeforeEach
	void setup() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testCreateRefundPolicy() {
		CreateRefundPolicyRequest request = new CreateRefundPolicyRequest("Refund Content", 30);
		RefundPolicy refundPolicy = RefundPolicy.toEntity("Refund Content", 30);

		when(refundPolicyRepository.save(any(RefundPolicy.class))).thenReturn(refundPolicy);

		refundPolicyService.createRefundPolicy(request);

		verify(refundPolicyRepository).save(any(RefundPolicy.class));
	}

	@Test
	void testUpdateRefundPolicy_Success() {
		Long refundPolicyId = 1L;
		UpdateRefundPolicyRequest request = new UpdateRefundPolicyRequest("Updated Content", 60);
		RefundPolicy existingPolicy = RefundPolicy.toEntity("Old Content", 30);

		// 기존 정책을 반환하도록 설정
		when(refundPolicyRepository.findById(refundPolicyId)).thenReturn(Optional.of(existingPolicy));

		// 업데이트 후 저장된 정책을 반환하도록 설정
		RefundPolicy updatedPolicy = RefundPolicy.toEntity("Updated Content", 60);
		when(refundPolicyRepository.save(any(RefundPolicy.class))).thenReturn(updatedPolicy);

		// 메서드 호출
		refundPolicyService.updateRefundPolicy(request, refundPolicyId);

		// 정책이 업데이트 되었는지 검증
		verify(refundPolicyRepository).findById(refundPolicyId);
		verify(refundPolicyRepository).save(argThat(p ->
			"Updated Content".equals(p.getRefundPolicyContent()) &&
				60 == p.getRefundPolicyDate()
		));
	}

	@Test
	void testUpdateRefundPolicy_NotFound() {
		Long refundPolicyId = 1L;
		UpdateRefundPolicyRequest request = new UpdateRefundPolicyRequest("Updated Content", 60);

		when(refundPolicyRepository.findById(refundPolicyId)).thenReturn(Optional.empty());

		ErrorStatus expectedErrorStatus = ErrorStatus.from("반품 정책을 가져올 수 없습니다", HttpStatus.NOT_FOUND,
			LocalDateTime.now());

		RefundFailException thrown = assertThrows(RefundFailException.class,
			() -> refundPolicyService.updateRefundPolicy(request, refundPolicyId));
		ErrorStatus actualErrorStatus = thrown.getErrorStatus();

		assertThat(actualErrorStatus.getMessage()).isEqualTo(expectedErrorStatus.getMessage());
		assertThat(actualErrorStatus.getStatus()).isEqualTo(expectedErrorStatus.getStatus());
		verify(refundPolicyRepository).findById(refundPolicyId);
		verify(refundPolicyRepository, never()).save(any(RefundPolicy.class));
	}

	@Test
	void testDeleteRefundPolicy() {
		Long refundPolicyId = 1L;
		doNothing().when(refundPolicyRepository).deleteById(refundPolicyId);

		refundPolicyService.deleteRefundPolicy(refundPolicyId);

		verify(refundPolicyRepository).deleteById(refundPolicyId);
	}

	@Test
	void testGetAllRefundPolicies() {
		RefundPolicy refundPolicy = RefundPolicy.toEntity("Refund Content", 30);
		List<RefundPolicy> refundPolicies = Collections.singletonList(refundPolicy);

		GetRefundResponse getRefundResponse = GetRefundResponse.from(refundPolicy);
		GetAllRefundResponse expectedResponse = GetAllRefundResponse.builder()
			.refunds(Collections.singletonList(getRefundResponse))
			.build();

		when(refundPolicyRepository.findAll()).thenReturn(refundPolicies);

		GetAllRefundResponse result = refundPolicyService.getAllRefundPolicies();

		assertThat(result).isEqualTo(expectedResponse);
		verify(refundPolicyRepository).findAll();
	}

	@Test
	void testGetAllRefundPolicies_EmptyList() {
		when(refundPolicyRepository.findAll()).thenReturn(Collections.emptyList());

		GetAllRefundResponse response = refundPolicyService.getAllRefundPolicies();

		assertThat(response).isNull();
		verify(refundPolicyRepository).findAll();
	}
}
