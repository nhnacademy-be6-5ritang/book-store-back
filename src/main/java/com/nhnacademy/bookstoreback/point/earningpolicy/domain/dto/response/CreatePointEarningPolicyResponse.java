package com.nhnacademy.bookstoreback.point.earningpolicy.domain.dto.response;

import java.math.BigDecimal;

import com.nhnacademy.bookstoreback.point.earningpolicy.domain.entity.PointEarningPolicy;

public record CreatePointEarningPolicyResponse(
	String pointEarningPolicyType,
	BigDecimal pointEarningAmount
) {
	public static CreatePointEarningPolicyResponse fromEntity(PointEarningPolicy savedPointEarningPolicy) {
		return new CreatePointEarningPolicyResponse(
			savedPointEarningPolicy.getPointEarningPolicyType(),
			savedPointEarningPolicy.getPointEarningAmount()
		);
	}
}
