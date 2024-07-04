package com.nhnacademy.bookstoreback.point.earningpolicy.domain.dto.request;

import java.math.BigDecimal;

public record CreatePointEarningPolicyRequest(
	String pointEarningPolicyType,
	BigDecimal pointEarningAmount
) {
}
