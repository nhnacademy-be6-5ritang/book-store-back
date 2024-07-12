package com.nhnacademy.bookstoreback.order.service;

import com.nhnacademy.bookstoreback.order.domain.dto.request.CreateRefundPolicyRequest;
import com.nhnacademy.bookstoreback.order.domain.dto.request.UpdateRefundPolicyRequest;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetAllRefundResponse;

public interface RefundPolicyService {
	/**
	 * 반품 정책 생성
	 * @param request 정책 내용 및 반품 날짜
	 */
	void createRefundPolicy(CreateRefundPolicyRequest request);

	/**
	 * 반품 정책 업데이트
	 * @param request 정책 내용 및 반품 날짜
	 * @param refundPolicyId 반품 정책 아이디
	 */
	void updateRefundPolicy(UpdateRefundPolicyRequest request, Long refundPolicyId);

	/**
	 * 반품 정책 삭제
	 * @param refundPolicyId 반품 정책 아이디
	 */
	void deleteRefundPolicy(Long refundPolicyId);

	/**
	 * 반품 정책 전부 가져오기
	 * @return 모든 반품 정책
	 */
	GetAllRefundResponse getAllRefundPolicies();
}
