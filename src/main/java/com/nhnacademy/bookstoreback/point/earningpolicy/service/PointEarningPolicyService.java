package com.nhnacademy.bookstoreback.point.earningpolicy.service;

import java.util.List;

import com.nhnacademy.bookstoreback.point.earningpolicy.domain.dto.request.CreatePointEarningPolicyRequest;
import com.nhnacademy.bookstoreback.point.earningpolicy.domain.dto.request.UpdatePointEarningPolicyRequest;
import com.nhnacademy.bookstoreback.point.earningpolicy.domain.dto.response.CreatePointEarningPolicyResponse;
import com.nhnacademy.bookstoreback.point.earningpolicy.domain.dto.response.GetPointEarningPolicyResponse;
import com.nhnacademy.bookstoreback.point.earningpolicy.domain.dto.response.UpdatePointEarningPolicyResponse;

public interface PointEarningPolicyService {
	CreatePointEarningPolicyResponse createPointEarningPolicy(
		CreatePointEarningPolicyRequest createPointEarningPolicyRequest
	);

	List<GetPointEarningPolicyResponse> getPointEarningPolicies();

	GetPointEarningPolicyResponse getPointEarningPolicy(Long pointEarningPolicyId);

	UpdatePointEarningPolicyResponse updatePointEarningPolicy(
		Long pointEarningPolicyId, UpdatePointEarningPolicyRequest updatePointEarningPolicyRequest
	);

	void activatePointEarningPolicy(Long pointEarningPolicyId);

	void deactivatePointEarningPolicy(Long pointEarningPolicyId);

	void deletePointEarningPolicy(Long pointEarningPolicyId);
}
