package com.nhnacademy.bookstoreback.point.earningpolicy.service;

import java.util.List;

import com.nhnacademy.bookstoreback.point.earningpolicy.domain.dto.request.CreatePointEarningPolicyRequest;
import com.nhnacademy.bookstoreback.point.earningpolicy.domain.dto.request.UpdatePointEarningPolicyRequest;
import com.nhnacademy.bookstoreback.point.earningpolicy.domain.dto.response.CreatePointEarningPolicyResponse;
import com.nhnacademy.bookstoreback.point.earningpolicy.domain.dto.response.GetPointEarningPolicyResponse;
import com.nhnacademy.bookstoreback.point.earningpolicy.domain.dto.response.UpdatePointEarningPolicyResponse;

/**
 * @author 김태환
 * 포인트 적립 정책 관련 서비스 인터페이스입니다.
 */
public interface PointEarningPolicyService {

	/**
	 * 새로운 포인트 적립 정책을 생성합니다.
	 *
	 * @param createPointEarningPolicyRequest 포인트 적립 정책 생성 요청 데이터
	 * @return 생성된 포인트 적립 정책의 응답 데이터
	 */
	CreatePointEarningPolicyResponse createPointEarningPolicy(
		CreatePointEarningPolicyRequest createPointEarningPolicyRequest
	);

	/**
	 * 모든 포인트 적립 정책을 조회합니다.
	 *
	 * @return 포인트 적립 정책 목록
	 */
	List<GetPointEarningPolicyResponse> getPointEarningPolicies();

	/**
	 * 특정 포인트 적립 정책을 조회합니다.
	 *
	 * @param pointEarningPolicyId 포인트 적립 정책 ID
	 * @return 해당 포인트 적립 정책의 응답 데이터
	 */
	GetPointEarningPolicyResponse getPointEarningPolicy(Long pointEarningPolicyId);

	/**
	 * 특정 포인트 적립 정책을 수정합니다.
	 *
	 * @param pointEarningPolicyId 포인트 적립 정책 ID
	 * @param updatePointEarningPolicyRequest 포인트 적립 정책 수정 요청 데이터
	 * @return 수정된 포인트 적립 정책의 응답 데이터
	 */
	UpdatePointEarningPolicyResponse updatePointEarningPolicy(
		Long pointEarningPolicyId, UpdatePointEarningPolicyRequest updatePointEarningPolicyRequest
	);

	/**
	 * 특정 포인트 적립 정책을 활성화합니다.
	 *
	 * @param pointEarningPolicyId 포인트 적립 정책 ID
	 */
	void activatePointEarningPolicy(Long pointEarningPolicyId);

	/**
	 * 특정 포인트 적립 정책을 비활성화합니다.
	 *
	 * @param pointEarningPolicyId 포인트 적립 정책 ID
	 */
	void deactivatePointEarningPolicy(Long pointEarningPolicyId);

	/**
	 * 특정 포인트 적립 정책을 삭제합니다.
	 *
	 * @param pointEarningPolicyId 포인트 적립 정책 ID
	 */
	void deletePointEarningPolicy(Long pointEarningPolicyId);
}
