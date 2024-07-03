package com.nhnacademy.bookstoreback.point.earningpolicy.domain.dto.response;

import java.math.BigDecimal;

import com.nhnacademy.bookstoreback.point.earningpolicy.domain.entity.PointEarningPolicy;

public record GetPointEarningPolicyResponse(
	Long pointEarningPolicyId,
	String pointEarningPolicyType,
	BigDecimal pointEarningAmount,
	String pointEarningPolicyStatus
) {
	public static GetPointEarningPolicyResponse fromEntity(PointEarningPolicy pointEarningPolicy) {
		return new GetPointEarningPolicyResponse(
			pointEarningPolicy.getId(),
			pointEarningPolicy.getPointEarningPolicyType(),
			pointEarningPolicy.getPointEarningAmount(),
			pointEarningPolicy.getPointEarningPolicyStatus()
		);
	}
}
