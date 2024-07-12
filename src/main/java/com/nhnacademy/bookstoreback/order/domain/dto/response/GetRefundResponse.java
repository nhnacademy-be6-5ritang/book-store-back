package com.nhnacademy.bookstoreback.order.domain.dto.response;

import com.nhnacademy.bookstoreback.order.domain.entity.RefundPolicy;

import lombok.Builder;

@Builder
public record GetRefundResponse(
	Long refundPolicyId,
	String refundPolicyContent,
	int refundPolicyDate
) {
	public static GetRefundResponse from(RefundPolicy refundPolicy) {
		return GetRefundResponse.builder()
			.refundPolicyId(refundPolicy.getRefundPolicyId())
			.refundPolicyContent(refundPolicy.getRefundPolicyContent())
			.refundPolicyDate(refundPolicy.getRefundPolicyDate())
			.build();
	}
}
