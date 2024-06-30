package com.nhnacademy.bookstoreback.deliverypolicy.service;

import java.util.List;

import com.nhnacademy.bookstoreback.deliverypolicy.domain.dto.request.CreateDeliveryPolicyRequest;
import com.nhnacademy.bookstoreback.deliverypolicy.domain.dto.request.UpdateDeliveryPolicyRequest;
import com.nhnacademy.bookstoreback.deliverypolicy.domain.dto.response.CreateDeliveryPolicyResponse;
import com.nhnacademy.bookstoreback.deliverypolicy.domain.dto.response.GetDeliveryPoliciesResponse;
import com.nhnacademy.bookstoreback.deliverypolicy.domain.dto.response.GetDeliveryPolicyResponse;
import com.nhnacademy.bookstoreback.deliverypolicy.domain.dto.response.UpdateDeliveryPolicyResponse;

/**
 * @author 이경헌
 * 배송비 정책 서비스 인터페이스.
 * 배송비 정책과 관련된 비즈니스 로직을 처리합니다.
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
	 * @return 생성된 배송비 정책의 정보.
	 */
	CreateDeliveryPolicyResponse createDeliveryPolicy(CreateDeliveryPolicyRequest request);

	/**
	 * 특정 배송비 정책을 업데이트합니다.
	 *
	 * @param deliveryPolicyId 업데이트할 배송비 정책의 ID.
	 * @param request          업데이트할 배송비 정책의 정보.
	 * @return 업데이트된 배송비 정책의 정보.
	 */
	UpdateDeliveryPolicyResponse updateDeliveryPolicy(Long deliveryPolicyId, UpdateDeliveryPolicyRequest request);

	/**
	 * 특정 배송비 정책을 삭제합니다.
	 *
	 * @param deliveryPolicyId 삭제할 배송비 정책의 ID.
	 */
	void deleteDeliveryPolicy(Long deliveryPolicyId);

}
