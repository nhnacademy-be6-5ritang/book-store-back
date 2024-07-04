package com.nhnacademy.bookstoreback.point.earningpolicy.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nhnacademy.bookstoreback.point.earningpolicy.domain.dto.request.CreatePointEarningPolicyRequest;
import com.nhnacademy.bookstoreback.point.earningpolicy.domain.dto.request.UpdatePointEarningPolicyRequest;
import com.nhnacademy.bookstoreback.point.earningpolicy.domain.dto.response.CreatePointEarningPolicyResponse;
import com.nhnacademy.bookstoreback.point.earningpolicy.domain.dto.response.GetPointEarningPolicyResponse;
import com.nhnacademy.bookstoreback.point.earningpolicy.domain.dto.response.UpdatePointEarningPolicyResponse;
import com.nhnacademy.bookstoreback.point.earningpolicy.service.PointEarningPolicyService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/point-earning-policies")
public class PointEarningPolicyController {
	private final PointEarningPolicyService pointEarningPolicyService;

	@PostMapping
	public ResponseEntity<CreatePointEarningPolicyResponse> createPointEarningPolicy(
		@RequestBody CreatePointEarningPolicyRequest createPointEarningPolicyRequest
	) {
		CreatePointEarningPolicyResponse createPointEarningPolicyResponse
			= pointEarningPolicyService.createPointEarningPolicy(createPointEarningPolicyRequest);
		return ResponseEntity.status(HttpStatus.CREATED).body(createPointEarningPolicyResponse);
	}

	@GetMapping
	public ResponseEntity<List<GetPointEarningPolicyResponse>> getPointEarningPolicies() {
		List<GetPointEarningPolicyResponse> getPointEarningPolicyResponseList
			= pointEarningPolicyService.getPointEarningPolicies();
		return ResponseEntity.status(HttpStatus.OK)
			.body(getPointEarningPolicyResponseList);
	}

	@GetMapping("/{pointEarningPolicyId}")
	public ResponseEntity<GetPointEarningPolicyResponse> getPointEarningPolicy(
		@PathVariable Long pointEarningPolicyId
	) {
		GetPointEarningPolicyResponse getPointEarningPolicyResponse
			= pointEarningPolicyService.getPointEarningPolicy(pointEarningPolicyId);
		return ResponseEntity.status(HttpStatus.OK).body(getPointEarningPolicyResponse);
	}

	@PatchMapping("/{pointEarningPolicyId}")
	public ResponseEntity<UpdatePointEarningPolicyResponse> updatePointEarningPolicy(
		@PathVariable Long pointEarningPolicyId,
		@RequestBody UpdatePointEarningPolicyRequest updatePointEarningPolicyRequest
	) {
		UpdatePointEarningPolicyResponse updatePointEarningPolicyResponse
			= pointEarningPolicyService.updatePointEarningPolicy(pointEarningPolicyId, updatePointEarningPolicyRequest);
		return ResponseEntity.status(HttpStatus.OK).body(updatePointEarningPolicyResponse);
	}

	@PatchMapping("/{pointEarningPolicyId}/activate")
	public ResponseEntity<Void> activatePointEarningPolicy(@PathVariable Long pointEarningPolicyId) {
		pointEarningPolicyService.activatePointEarningPolicy(pointEarningPolicyId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@PatchMapping("/{pointEarningPolicyId}/deactivate")
	public ResponseEntity<Void> deactivatePointEarningPolicy(@PathVariable Long pointEarningPolicyId) {
		pointEarningPolicyService.deactivatePointEarningPolicy(pointEarningPolicyId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@DeleteMapping("/{pointEarningPolicyId}")
	public ResponseEntity<Void> deletePointEarningPolicy(@PathVariable Long pointEarningPolicyId) {
		pointEarningPolicyService.deletePointEarningPolicy(pointEarningPolicyId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
