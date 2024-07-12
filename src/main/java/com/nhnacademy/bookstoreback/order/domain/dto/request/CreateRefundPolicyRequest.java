package com.nhnacademy.bookstoreback.order.domain.dto.request;

public record CreateRefundPolicyRequest(
	String refundPolicyContent,
	int refundPolicyDate
) {
}
