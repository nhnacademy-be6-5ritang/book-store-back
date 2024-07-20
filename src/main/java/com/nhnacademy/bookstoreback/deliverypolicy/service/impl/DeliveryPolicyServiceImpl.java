package com.nhnacademy.bookstoreback.deliverypolicy.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
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
import com.nhnacademy.bookstoreback.deliverypolicy.exception.DeliveryPolicyNotFoundException;
import com.nhnacademy.bookstoreback.deliverypolicy.repository.DeliveryPolicyRepository;
import com.nhnacademy.bookstoreback.deliverypolicy.service.DeliveryPolicyService;
import com.nhnacademy.bookstoreback.global.exception.NotFoundException;
import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;

import lombok.RequiredArgsConstructor;

/**
 * @author 이경헌
 * DeliveryPolicyService 구현 클래스.
 * 배송비 정책과 관련된 비즈니스 로직을 처리합니다.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class DeliveryPolicyServiceImpl implements DeliveryPolicyService {
	private final DeliveryPolicyRepository deliveryPolicyRepository;
	private final DeliveryRepository deliveryRepository;

	/**
	 * 모든 배송비 정책을 조회합니다.
	 *
	 * @return 모든 배송비 정책의 목록.
	 */
	@Override
	public List<GetDeliveryPoliciesResponse> getDeliveryPolicies() {
		return deliveryPolicyRepository.findAll().stream()
			.map(GetDeliveryPoliciesResponse::fromEntity)
			.toList();
	}

	/**
	 * 특정 배송비 정책을 조회합니다.
	 *
	 * @param deliveryPolicyId 조회할 배송비 정책의 ID.
	 * @return 조회된 배송비 정책의 정보.
	 * @throws NotFoundException 배송비 정책이 존재하지 않는 경우 발생.
	 */
	@Override
	public GetDeliveryPolicyResponse getDeliveryPolicy(Long deliveryPolicyId) {
		DeliveryPolicy deliveryPolicy = deliveryPolicyRepository.findById(deliveryPolicyId).orElseThrow(() -> {
			String errorMessage = String.format("해당 배송비정책 '%d'는 존재하지 않는 배송비정책입니다.", deliveryPolicyId);
			ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.NOT_FOUND, LocalDateTime.now());
			return new NotFoundException(errorStatus);
		});

		return GetDeliveryPolicyResponse.fromEntity(deliveryPolicy);
	}

	/**
	 * 새로운 배송비 정책을 생성합니다.
	 *
	 * @param request 생성할 배송비 정책의 정보.
	 */
	@Override
	public void createDeliveryPolicy(CreateDeliveryPolicyRequest request) {
		DeliveryPolicy deliveryPolicy = new DeliveryPolicy(request.deliveryPolicyName(), request.deliveryPolicyPrice(),
			request.deliveryPolicyContent(), request.deliveryPolicyStandardPrice());

		deliveryPolicyRepository.save(deliveryPolicy);
	}

	/**
	 * 특정 배송비 정책을 업데이트합니다.
	 *
	 * @param deliveryPolicyId 업데이트할 배송비 정책의 ID.
	 * @param request          업데이트할 배송비 정책의 정보.
	 * @throws NotFoundException 배송비 정책이 존재하지 않는 경우 발생.
	 */
	@Override
	public void updateDeliveryPolicy(Long deliveryPolicyId,
		UpdateDeliveryPolicyRequest request) {
		DeliveryPolicy deliveryPolicy = deliveryPolicyRepository.findById(deliveryPolicyId)
			.orElseThrow(() -> new DeliveryPolicyNotFoundException(deliveryPolicyId));

		deliveryPolicy.updateDeliveryPolicy(request.deliveryPolicyName(), request.deliveryPolicyPrice(),
			request.deliveryPolicyContent(), request.deliveryPolicyStandardPrice());

		deliveryPolicyRepository.save(deliveryPolicy);
	}

	/**
	 * 특정 배송비 정책을 삭제합니다.
	 *
	 * @param deliveryPolicyId 삭제할 배송비 정책의 ID.
	 * @throws DeliveryNotFoundException 배송비 정책이 존재하지 않는 경우 발생.
	 */
	@Override
	public void deleteDeliveryPolicy(Long deliveryPolicyId) {
		deliveryPolicyRepository.deleteById(deliveryPolicyId);
	}

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
