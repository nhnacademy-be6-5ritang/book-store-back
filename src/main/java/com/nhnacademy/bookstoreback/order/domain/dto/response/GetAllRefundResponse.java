package com.nhnacademy.bookstoreback.order.domain.dto.response;

import java.util.ArrayList;
import java.util.List;

import com.nhnacademy.bookstoreback.order.domain.entity.RefundPolicy;

import lombok.Builder;

@Builder
public record GetAllRefundResponse(
	List<GetRefundResponse> refunds
) {
	public static GetAllRefundResponse from(List<RefundPolicy> all) {
		List<GetRefundResponse> list = new ArrayList<>();
		for (RefundPolicy refund : all) {
			list.add(GetRefundResponse.from(refund));
		}
		return GetAllRefundResponse.builder()
			.refunds(list)
			.build();
	}
}
