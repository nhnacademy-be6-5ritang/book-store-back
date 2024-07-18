package com.nhnacademy.bookstoreback.order.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record OrderCheckNonRequest(
	@NotBlank @Size(max = 30) String payerEmail,
	@NotBlank @Size(max = 64) String orderInfoId
) {
}
