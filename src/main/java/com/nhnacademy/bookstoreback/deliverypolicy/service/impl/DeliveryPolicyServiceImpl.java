package com.nhnacademy.bookstoreback.deliverypolicy.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
import com.nhnacademy.bookstoreback.deliverypolicy.service.DeliveryPolicyService;

import lombok.RequiredArgsConstructor;

/**
 * @author 이경헌
 * 배송비 정책과 관련된 비즈니스 로직을 처리합니다.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class DeliveryPolicyServiceImpl implements DeliveryPolicyService {
	private final DeliveryPolicyRepository deliveryPolicyRepository;
	private final DeliveryRepository deliveryRepository;

	/**
	 *{@inheritDoc}
	 */
	@Override
	public List<GetDeliveryPoliciesResponse> getDeliveryPolicies() {
		return deliveryPolicyRepository.findAll().stream()
			.map(GetDeliveryPoliciesResponse::fromEntity).toList();
	}

	/**
	 *{@inheritDoc}
	 */
	@Override
	public GetDeliveryPolicyResponse getDeliveryPolicy(Long deliveryPolicyId) {
		DeliveryPolicy deliveryPolicy = deliveryPolicyRepository.findById(deliveryPolicyId)
			.orElseThrow(() -> new DeliveryPolicyNotFoundException(deliveryPolicyId));

		return GetDeliveryPolicyResponse.fromEntity(deliveryPolicy);
	}

	/**
	 *{@inheritDoc}
	 */
	@Override
	public void createDeliveryPolicy(CreateDeliveryPolicyRequest request) {
		if (deliveryPolicyRepository.existsByDeliveryPolicyName(request.deliveryPolicyName())) {
			throw new DeliveryPolicyAlreadyExistsException(request.deliveryPolicyName());
		}

		deliveryPolicyRepository.save(DeliveryPolicy.toEntity(request));
	}

	/**
	 *{@inheritDoc}
	 */
	@Override
	public void updateDeliveryPolicy(Long deliveryPolicyId,
		UpdateDeliveryPolicyRequest request) {
		DeliveryPolicy deliveryPolicy = deliveryPolicyRepository.findById(deliveryPolicyId)
			.orElseThrow(() -> new DeliveryPolicyNotFoundException(deliveryPolicyId));

		if (deliveryPolicyRepository.existsByDeliveryPolicyName(request.deliveryPolicyName())) {
			throw new DeliveryPolicyAlreadyExistsException(request.deliveryPolicyName());
		}

		deliveryPolicy.updateDeliveryPolicy(request.deliveryPolicyName(), request.deliveryPolicyPrice(),
			request.deliveryPolicyContent(), request.deliveryPolicyStandardPrice());

		deliveryPolicyRepository.save(deliveryPolicy);
	}

	/**
	 *{@inheritDoc}
	 */
	@Override
	public void deleteDeliveryPolicy(Long deliveryPolicyId) {
		deliveryPolicyRepository.deleteById(deliveryPolicyId);
	}

	/**
	 *{@inheritDoc}
	 */
	@Override
	public GetDeliveryPolicyResponse findByDeliveryPolicyStandardPriceLessThanEqualOrderByDeliveryPolicyStandardPriceDesc(
		Long deliveryId, BigDecimal price) {
		Delivery delivery = deliveryRepository.findById(deliveryId)
			.orElseThrow(() -> new DeliveryNotFoundException(deliveryId));

		List<DeliveryPolicy> deliveryPolicy = deliveryPolicyRepository
			.findByDeliveryPolicyStandardPriceLessThanEqualOrderByDeliveryPolicyStandardPriceDesc(price);

		delivery.updateDeliveryAddPolicy(deliveryPolicy.getFirst());
		deliveryRepository.save(delivery);
		
		return GetDeliveryPolicyResponse.fromEntity(deliveryPolicy.getFirst());
	}
}
