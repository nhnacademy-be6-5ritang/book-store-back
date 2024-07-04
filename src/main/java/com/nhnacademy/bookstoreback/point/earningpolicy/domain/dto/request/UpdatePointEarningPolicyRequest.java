package com.nhnacademy.bookstoreback.point.earningpolicy.domain.dto.request;

import java.math.BigDecimal;

public record UpdatePointEarningPolicyRequest(
	String pointEarningPolicyType,
	BigDecimal pointEarningAmount
) {
}
