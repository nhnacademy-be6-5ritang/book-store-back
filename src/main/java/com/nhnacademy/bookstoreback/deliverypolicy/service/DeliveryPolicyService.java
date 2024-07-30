package com.nhnacademy.bookstoreback.deliverypolicy.service;

import java.math.BigDecimal;
import java.util.List;

import com.nhnacademy.bookstoreback.deliverypolicy.domain.dto.request.CreateDeliveryPolicyRequest;
import com.nhnacademy.bookstoreback.deliverypolicy.domain.dto.request.UpdateDeliveryPolicyRequest;
import com.nhnacademy.bookstoreback.deliverypolicy.domain.dto.response.GetDeliveryPoliciesResponse;
import com.nhnacademy.bookstoreback.deliverypolicy.domain.dto.response.GetDeliveryPolicyResponse;

/**
 * @author 이경헌
 * 배송비 정책 서비스 인터페이스.
 */
public interface DeliveryPolicyService {
	/**
	 * 모든 배송비 정책을 조회합니다.
	 *
	 * @return 모든 배송비 정책의 목록.
	 */
	List<GetDeliveryPoliciesResponse> getDeliveryPolicies();

	/**
	 * 특정 배송비 정책을 조회합니다.
	 *
	 * @param deliveryPolicyId 조회할 배송비 정책의 ID.
	 * @return 조회된 배송비 정책의 정보.
	 */
	GetDeliveryPolicyResponse getDeliveryPolicy(Long deliveryPolicyId);

	/**
	 * 새로운 배송비 정책을 생성합니다.
	 *
	 * @param request 생성할 배송비 정책의 정보.
	 */
	void createDeliveryPolicy(CreateDeliveryPolicyRequest request);

	/**
	 * 특정 배송비 정책을 업데이트합니다.
	 *
	 * @param deliveryPolicyId 업데이트할 배송비 정책의 ID.
	 * @param request          업데이트할 배송비 정책의 정보.
	 */
	void updateDeliveryPolicy(Long deliveryPolicyId, UpdateDeliveryPolicyRequest request);

	/**
	 * 특정 배송비 정책을 삭제합니다.
	 *
	 * @param deliveryPolicyId 삭제할 배송비 정책의 ID.
	 */
	void deleteDeliveryPolicy(Long deliveryPolicyId);

	/**
	 * 특정 배송비 정책을 조회합니다.
	 *
	 * @param deliveryId 배송비 정책이 적용될 배송의 ID.
	 * @param price      배송비 정책의 기준 가격.
	 * @return 기준 가격 이하의 배송비 정책을 포함하는 응답 객체.
	 */
	GetDeliveryPolicyResponse findByDeliveryPolicyStandardPriceLessThanEqualOrderByDeliveryPolicyStandardPriceDesc(
		Long deliveryId, BigDecimal price);
}
