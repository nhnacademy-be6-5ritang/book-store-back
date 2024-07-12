package com.nhnacademy.bookstoreback.order.domain.dto.request;

public record UpdateRefundPolicyRequest(
	String refundPolicyContent,
	int refundPolicyDate
) {
}
