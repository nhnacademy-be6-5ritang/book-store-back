package com.nhnacademy.bookstoreback.order.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateRefundPolicyRequest(
	@NotBlank @Size(max = 300) String refundPolicyContent,
	@NotNull int refundPolicyDate
) {
}
