package com.nhnacademy.bookstoreback.deliverypolicy.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nhnacademy.bookstoreback.deliverypolicy.domain.dto.request.CreateDeliveryPolicyRequest;
import com.nhnacademy.bookstoreback.deliverypolicy.domain.dto.request.UpdateDeliveryPolicyRequest;
import com.nhnacademy.bookstoreback.deliverypolicy.domain.dto.response.CreateDeliveryPolicyResponse;
import com.nhnacademy.bookstoreback.deliverypolicy.domain.dto.response.GetDeliveryPoliciesResponse;
import com.nhnacademy.bookstoreback.deliverypolicy.domain.dto.response.GetDeliveryPolicyResponse;
import com.nhnacademy.bookstoreback.deliverypolicy.domain.dto.response.UpdateDeliveryPolicyResponse;
import com.nhnacademy.bookstoreback.deliverypolicy.domain.entity.DeliveryPolicy;
import com.nhnacademy.bookstoreback.deliverypolicy.repository.DeliveryPolicyRepository;
import com.nhnacademy.bookstoreback.deliverypolicy.service.DeliveryPolicyService;
import com.nhnacademy.bookstoreback.global.exception.NotFoundException;
import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class DeliveryPolicyServiceImpl implements DeliveryPolicyService {
	private final DeliveryPolicyRepository deliveryPolicyRepository;

	@Override
	public List<GetDeliveryPoliciesResponse> getDeliveryPolicies() {
		return deliveryPolicyRepository.findAll().stream()
			.map(deliveryPolicy -> GetDeliveryPoliciesResponse.builder()
				.deliveryPolicyId(deliveryPolicy.getDeliveryPolicyId())
				.deliveryPolicyName(deliveryPolicy.getDeliveryPolicyName())
				.build())
			.toList();
	}

	@Override
	public GetDeliveryPolicyResponse getDeliveryPolicy(Long deliveryPolicyId) {
		DeliveryPolicy deliveryPolicy = deliveryPolicyRepository.findById(deliveryPolicyId).orElseThrow(() -> {
			String errorMessage = String.format("해당 배송정책 '%d'는 존재하지 않는 배송정책입니다.", deliveryPolicyId);
			ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.NOT_FOUND, LocalDateTime.now());
			return new NotFoundException(errorStatus);
		});

		return GetDeliveryPolicyResponse.builder()
			.deliveryPolicyId(deliveryPolicy.getDeliveryPolicyId())
			.deliveryPolicyName(deliveryPolicy.getDeliveryPolicyName())
			.deliveryPolicyPrice(deliveryPolicy.getDeliveryPolicyPrice())
			.deliveryPolicyContent(deliveryPolicy.getDeliveryPolicyContent())
			.build();
	}

	@Override
	public CreateDeliveryPolicyResponse createDeliveryPolicy(CreateDeliveryPolicyRequest request) {
		DeliveryPolicy deliveryPolicy = new DeliveryPolicy(request.deliveryPolicyName(), request.deliveryPolicyPrice(),
			request.deliveryPolicyContent());

		deliveryPolicyRepository.save(deliveryPolicy);

		return CreateDeliveryPolicyResponse.builder()
			.deliveryPolicyId(deliveryPolicy.getDeliveryPolicyId())
			.deliveryPolicyName(deliveryPolicy.getDeliveryPolicyName())
			.deliveryPolicyPrice(deliveryPolicy.getDeliveryPolicyPrice())
			.deliveryPolicyContent(deliveryPolicy.getDeliveryPolicyContent())
			.build();
	}

	@Override
	public UpdateDeliveryPolicyResponse updateDeliveryPolicy(Long deliveryPolicyId,
		UpdateDeliveryPolicyRequest request) {
		DeliveryPolicy deliveryPolicy = deliveryPolicyRepository.findById(deliveryPolicyId).orElseThrow(() -> {
			String errorMessage = String.format("해당 배송정책 '%d'는 존재하지 않는 배송정책입니다.", deliveryPolicyId);
			ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.NOT_FOUND, LocalDateTime.now());
			return new NotFoundException(errorStatus);
		});

		deliveryPolicy.updateDeliveryPolicy(request.deliveryPolicyName(), request.deliveryPolicyPrice(),
			request.deliveryPolicyContent());

		deliveryPolicyRepository.save(deliveryPolicy);

		return UpdateDeliveryPolicyResponse.builder()
			.deliveryPolicyId(deliveryPolicy.getDeliveryPolicyId())
			.deliveryPolicyName(deliveryPolicy.getDeliveryPolicyName())
			.deliveryPolicyPrice(deliveryPolicy.getDeliveryPolicyPrice())
			.deliveryPolicyContent(deliveryPolicy.getDeliveryPolicyContent())
			.build();
	}

	@Override
	public void deleteDeliveryPolicy(Long deliveryPolicyId) {
		deliveryPolicyRepository.deleteById(deliveryPolicyId);
	}
}
