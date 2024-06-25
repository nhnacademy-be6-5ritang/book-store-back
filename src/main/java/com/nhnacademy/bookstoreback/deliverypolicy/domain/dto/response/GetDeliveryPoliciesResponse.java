package com.nhnacademy.bookstoreback.deliverypolicy.domain.dto.response;

import lombok.Builder;

@Builder
public record GetDeliveryPoliciesResponse(Long deliveryPolicyId, String deliveryPolicyName) {
}
