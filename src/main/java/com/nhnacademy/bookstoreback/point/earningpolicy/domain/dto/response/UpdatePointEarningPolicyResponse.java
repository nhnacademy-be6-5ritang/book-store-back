package com.nhnacademy.bookstoreback.point.earningpolicy.domain.dto.response;

import java.math.BigDecimal;

import com.nhnacademy.bookstoreback.point.earningpolicy.domain.entity.PointEarningPolicy;

public record UpdatePointEarningPolicyResponse(
	String pointEarningPolicyType,
	BigDecimal pointEarningAmount
) {
	public static UpdatePointEarningPolicyResponse fromEntity(PointEarningPolicy savedPointEarningPolicy) {
		return new UpdatePointEarningPolicyResponse(
			savedPointEarningPolicy.getPointEarningPolicyType(),
			savedPointEarningPolicy.getPointEarningAmount()
		);
	}
}
