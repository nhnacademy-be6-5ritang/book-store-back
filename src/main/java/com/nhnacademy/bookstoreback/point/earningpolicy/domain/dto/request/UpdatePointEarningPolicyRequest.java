package com.nhnacademy.bookstoreback.point.earningpolicy.domain.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdatePointEarningPolicyRequest(
	@NotBlank @Size(max = 15) String pointEarningPolicyType,
	@NotNull BigDecimal pointEarningAmount
) {
}
