package com.nhnacademy.bookstoreback.deliverypolicy.service;

import java.util.List;

import com.nhnacademy.bookstoreback.deliverypolicy.domain.dto.request.CreateDeliveryPolicyRequest;
import com.nhnacademy.bookstoreback.deliverypolicy.domain.dto.request.UpdateDeliveryPolicyRequest;
import com.nhnacademy.bookstoreback.deliverypolicy.domain.dto.response.CreateDeliveryPolicyResponse;
import com.nhnacademy.bookstoreback.deliverypolicy.domain.dto.response.GetDeliveryPoliciesResponse;
import com.nhnacademy.bookstoreback.deliverypolicy.domain.dto.response.GetDeliveryPolicyResponse;
import com.nhnacademy.bookstoreback.deliverypolicy.domain.dto.response.UpdateDeliveryPolicyResponse;

public interface DeliveryPolicyService {
	List<GetDeliveryPoliciesResponse> getDeliveryPolicies();

	GetDeliveryPolicyResponse getDeliveryPolicy(Long deliveryPolicyId);

	CreateDeliveryPolicyResponse createDeliveryPolicy(CreateDeliveryPolicyRequest request);

	UpdateDeliveryPolicyResponse updateDeliveryPolicy(Long deliveryPolicyId, UpdateDeliveryPolicyRequest request);

	void deleteDeliveryPolicy(Long deliveryPolicyId);

}
