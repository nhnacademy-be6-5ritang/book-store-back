package com.nhnacademy.bookstoreback.point.earningpolicy.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nhnacademy.bookstoreback.point.earningpolicy.domain.dto.request.CreatePointEarningPolicyRequest;
import com.nhnacademy.bookstoreback.point.earningpolicy.domain.dto.request.UpdatePointEarningPolicyRequest;
import com.nhnacademy.bookstoreback.point.earningpolicy.domain.dto.response.CreatePointEarningPolicyResponse;
import com.nhnacademy.bookstoreback.point.earningpolicy.domain.dto.response.GetPointEarningPolicyResponse;
import com.nhnacademy.bookstoreback.point.earningpolicy.domain.dto.response.UpdatePointEarningPolicyResponse;
import com.nhnacademy.bookstoreback.point.earningpolicy.domain.entity.PointEarningPolicy;
import com.nhnacademy.bookstoreback.point.earningpolicy.exception.PointEarningPolicyAlreadyExistsException;
import com.nhnacademy.bookstoreback.point.earningpolicy.exception.PointEarningPolicyNotFoundException;
import com.nhnacademy.bookstoreback.point.earningpolicy.repository.PointEarningPolicyRepository;
import com.nhnacademy.bookstoreback.point.earningpolicy.service.PointEarningPolicyService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PointEarningPolicyServiceImpl implements PointEarningPolicyService {
	private final PointEarningPolicyRepository pointEarningPolicyRepository;

	/**
	 *{@inheritDoc}
	 */
	@Override
	public CreatePointEarningPolicyResponse createPointEarningPolicy(
		CreatePointEarningPolicyRequest createPointEarningPolicyRequest
	) {
		if (pointEarningPolicyRepository.existsByPointEarningPolicyType(
			createPointEarningPolicyRequest.pointEarningPolicyType())
		) {
			throw new PointEarningPolicyAlreadyExistsException(
				createPointEarningPolicyRequest.pointEarningPolicyType()
			);
		}

		PointEarningPolicy pointEarningPolicy = PointEarningPolicy.toEntity(createPointEarningPolicyRequest);
		pointEarningPolicy.activate();
		PointEarningPolicy savedPointEarningPolicy = pointEarningPolicyRepository.save(pointEarningPolicy);

		return CreatePointEarningPolicyResponse.fromEntity(savedPointEarningPolicy);
	}

	/**
	 *{@inheritDoc}
	 */
	@Override
	public List<GetPointEarningPolicyResponse> getPointEarningPolicies() {
		List<PointEarningPolicy> pointEarningPolicies = pointEarningPolicyRepository.findAll();

		return pointEarningPolicies.stream()
			.map(GetPointEarningPolicyResponse::fromEntity)
			.toList();
	}

	/**
	 *{@inheritDoc}
	 */
	@Override
	public GetPointEarningPolicyResponse getPointEarningPolicy(Long pointEarningPolicyId) {
		PointEarningPolicy pointEarningPolicy = pointEarningPolicyRepository.findById(pointEarningPolicyId)
			.orElseThrow(() -> new PointEarningPolicyNotFoundException(pointEarningPolicyId));

		return GetPointEarningPolicyResponse.fromEntity(pointEarningPolicy);
	}

	/**
	 *{@inheritDoc}
	 */
	@Override
	public UpdatePointEarningPolicyResponse updatePointEarningPolicy(Long pointEarningPolicyId,
		UpdatePointEarningPolicyRequest updatePointEarningPolicyRequest) {
		PointEarningPolicy pointEarningPolicy = pointEarningPolicyRepository.findById(pointEarningPolicyId)
			.orElseThrow(() -> new PointEarningPolicyNotFoundException(pointEarningPolicyId));

		pointEarningPolicy.update(updatePointEarningPolicyRequest);

		PointEarningPolicy savedPointEarningPolicy = pointEarningPolicyRepository.save(pointEarningPolicy);

		return UpdatePointEarningPolicyResponse.fromEntity(savedPointEarningPolicy);
	}

	/**
	 *{@inheritDoc}
	 */
	@Override
	public void activatePointEarningPolicy(Long pointEarningPolicyId) {
		PointEarningPolicy pointEarningPolicy = pointEarningPolicyRepository.findById(pointEarningPolicyId)
			.orElseThrow(() -> new PointEarningPolicyNotFoundException(pointEarningPolicyId));

		pointEarningPolicy.activate();

		pointEarningPolicyRepository.save(pointEarningPolicy);
	}

	/**
	 *{@inheritDoc}
	 */
	@Override
	public void deactivatePointEarningPolicy(Long pointEarningPolicyId) {
		PointEarningPolicy pointEarningPolicy = pointEarningPolicyRepository.findById(pointEarningPolicyId)
			.orElseThrow(() -> new PointEarningPolicyNotFoundException(pointEarningPolicyId));

		pointEarningPolicy.deactivate();

		pointEarningPolicyRepository.save(pointEarningPolicy);
	}

	/**
	 *{@inheritDoc}
	 */
	@Override
	public void deletePointEarningPolicy(Long pointEarningPolicyId) {
		PointEarningPolicy pointEarningPolicy = pointEarningPolicyRepository.findById(pointEarningPolicyId)
			.orElseThrow(() -> new PointEarningPolicyNotFoundException(pointEarningPolicyId));

		pointEarningPolicyRepository.delete(pointEarningPolicy);
	}
}
