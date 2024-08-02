package com.nhnacademy.bookstoreback.order.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nhnacademy.bookstoreback.global.exception.RefundFailException;
import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;
import com.nhnacademy.bookstoreback.order.domain.dto.request.CreateRefundPolicyRequest;
import com.nhnacademy.bookstoreback.order.domain.dto.request.UpdateRefundPolicyRequest;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetAllRefundResponse;
import com.nhnacademy.bookstoreback.order.domain.entity.RefundPolicy;
import com.nhnacademy.bookstoreback.order.repository.RefundPolicyRepository;
import com.nhnacademy.bookstoreback.order.service.RefundPolicyService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional
public class RefundPolicyServiceImpl implements RefundPolicyService {
	private final RefundPolicyRepository refundPolicyRepository;

	public static final String ERROR_REFUND_EXITS = "반품 정책을 가져올 수 없습니다";

	/**
	 *{@inheritDoc}
	 */
	@Override
	public void createRefundPolicy(CreateRefundPolicyRequest request) {
		refundPolicyRepository.save(RefundPolicy.toEntity(request.refundPolicyContent(), request.refundPolicyDate()));
	}

	/**
	 *{@inheritDoc}
	 */
	@Override
	public void updateRefundPolicy(UpdateRefundPolicyRequest request, Long refundPolicyId) {
		RefundPolicy refundPolicy = refundPolicyRepository.findById(refundPolicyId).orElse(null);
		if (refundPolicy == null) {
			ErrorStatus errorStatus = ErrorStatus.from(ERROR_REFUND_EXITS, HttpStatus.NOT_FOUND, LocalDateTime.now());
			throw new RefundFailException(errorStatus);
		}
		refundPolicy.update(request);
		refundPolicyRepository.save(refundPolicy);
	}

	/**
	 *{@inheritDoc}
	 */
	@Override
	public void deleteRefundPolicy(Long refundPolicyId) {
		refundPolicyRepository.deleteById(refundPolicyId);
	}

	/**
	 *{@inheritDoc}
	 */
	@Override
	public GetAllRefundResponse getAllRefundPolicies() {
		List<RefundPolicy> refundPolicies = refundPolicyRepository.findAll();
		if (refundPolicies.isEmpty()) {
			return null;
		}
		return GetAllRefundResponse.from(refundPolicies);
	}
}
