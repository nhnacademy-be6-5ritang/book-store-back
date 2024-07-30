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

import com.nhnacademy.bookstoreback.auth.annotation.AuthorizeRole;
import com.nhnacademy.bookstoreback.point.earningpolicy.domain.dto.request.CreatePointEarningPolicyRequest;
import com.nhnacademy.bookstoreback.point.earningpolicy.domain.dto.request.UpdatePointEarningPolicyRequest;
import com.nhnacademy.bookstoreback.point.earningpolicy.domain.dto.response.CreatePointEarningPolicyResponse;
import com.nhnacademy.bookstoreback.point.earningpolicy.domain.dto.response.GetPointEarningPolicyResponse;
import com.nhnacademy.bookstoreback.point.earningpolicy.domain.dto.response.UpdatePointEarningPolicyResponse;
import com.nhnacademy.bookstoreback.point.earningpolicy.service.PointEarningPolicyService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * @author 김태환
 * 포인트 적립 정책 관련 HTTP 요청을 처리하는 컨트롤러입니다.
 */
@Tag(name = "Point Earning Policy", description = "포인트 적립 정책 관리 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/point-earning-policies")
public class PointEarningPolicyController {
	private final PointEarningPolicyService pointEarningPolicyService;

	/**
	 * 새로운 포인트 적립 정책을 생성합니다.
	 *
	 * @param createPointEarningPolicyRequest 포인트 적립 정책 생성 요청 데이터
	 * @return 생성된 포인트 적립 정책의 응답 데이터
	 */
	@Operation(
		summary = "포인트 적립 정책 생성",
		description = "새로운 포인트 적립 정책을 생성합니다.",
		responses = {
			@ApiResponse(responseCode = "201", description = "포인트 적립 정책이 성공적으로 생성되었습니다."),
			@ApiResponse(responseCode = "400", description = "잘못된 요청 데이터입니다."),
			@ApiResponse(responseCode = "409", description = "이미 존재하는 포인트 적립 정책입니다.")
		}
	)
	@AuthorizeRole({"POINT_ADMIN", "HEAD_ADMIN"})
	@PostMapping
	public ResponseEntity<CreatePointEarningPolicyResponse> createPointEarningPolicy(
		@RequestBody CreatePointEarningPolicyRequest createPointEarningPolicyRequest
	) {
		CreatePointEarningPolicyResponse createPointEarningPolicyResponse
			= pointEarningPolicyService.createPointEarningPolicy(createPointEarningPolicyRequest);
		return ResponseEntity.status(HttpStatus.CREATED).body(createPointEarningPolicyResponse);
	}

	/**
	 * 등록된 포인트 적립 정책의 목록을 조회합니다.
	 *
	 * @return 포인트 적립 정책 목록
	 */
	@Operation(
		summary = "포인트 적립 정책 목록 조회",
		description = "등록된 포인트 적립 정책의 목록을 조회합니다.",
		responses = {
			@ApiResponse(responseCode = "200", description = "포인트 적립 정책 목록이 성공적으로 반환되었습니다."),
			@ApiResponse(responseCode = "404", description = "포인트 적립 정책을 찾을 수 없습니다.")
		}
	)
	@GetMapping
	public ResponseEntity<List<GetPointEarningPolicyResponse>> getPointEarningPolicies() {
		List<GetPointEarningPolicyResponse> getPointEarningPolicyResponseList
			= pointEarningPolicyService.getPointEarningPolicies();
		return ResponseEntity.status(HttpStatus.OK)
			.body(getPointEarningPolicyResponseList);
	}

	/**
	 * 특정 포인트 적립 정책을 조회합니다.
	 *
	 * @param pointEarningPolicyId 포인트 적립 정책 ID
	 * @return 해당 포인트 적립 정책의 응답 데이터
	 */
	@Operation(
		summary = "포인트 적립 정책 조회",
		description = "특정 포인트 적립 정책을 조회합니다.",
		responses = {
			@ApiResponse(responseCode = "200", description = "포인트 적립 정책이 성공적으로 반환되었습니다."),
			@ApiResponse(responseCode = "404", description = "포인트 적립 정책을 찾을 수 없습니다.")
		}
	)
	@GetMapping("/{pointEarningPolicyId}")
	public ResponseEntity<GetPointEarningPolicyResponse> getPointEarningPolicy(
		@PathVariable Long pointEarningPolicyId
	) {
		GetPointEarningPolicyResponse getPointEarningPolicyResponse
			= pointEarningPolicyService.getPointEarningPolicy(pointEarningPolicyId);
		return ResponseEntity.status(HttpStatus.OK).body(getPointEarningPolicyResponse);
	}

	/**
	 * 특정 포인트 적립 정책의 정보를 수정합니다.
	 *
	 * @param pointEarningPolicyId 포인트 적립 정책 ID
	 * @param updatePointEarningPolicyRequest 포인트 적립 정책 수정 요청 데이터
	 * @return 수정된 포인트 적립 정책의 응답 데이터
	 */
	@Operation(
		summary = "포인트 적립 정책 수정",
		description = "특정 포인트 적립 정책의 정보를 수정합니다.",
		responses = {
			@ApiResponse(responseCode = "200", description = "포인트 적립 정책이 성공적으로 수정되었습니다."),
			@ApiResponse(responseCode = "400", description = "잘못된 요청 데이터입니다."),
			@ApiResponse(responseCode = "404", description = "포인트 적립 정책을 찾을 수 없습니다."),
			@ApiResponse(responseCode = "409", description = "이미 존재하는 포인트 적립 정책입니다.")
		}
	)
	@AuthorizeRole({"POINT_ADMIN", "HEAD_ADMIN"})
	@PatchMapping("/{pointEarningPolicyId}")
	public ResponseEntity<UpdatePointEarningPolicyResponse> updatePointEarningPolicy(
		@PathVariable Long pointEarningPolicyId,
		@RequestBody UpdatePointEarningPolicyRequest updatePointEarningPolicyRequest
	) {
		UpdatePointEarningPolicyResponse updatePointEarningPolicyResponse
			= pointEarningPolicyService.updatePointEarningPolicy(pointEarningPolicyId, updatePointEarningPolicyRequest);
		return ResponseEntity.status(HttpStatus.OK).body(updatePointEarningPolicyResponse);
	}

	/**
	 * 특정 포인트 적립 정책을 활성화합니다.
	 *
	 * @param pointEarningPolicyId 포인트 적립 정책 ID
	 * @return 응답 상태 코드 (204 NO CONTENT)
	 */
	@Operation(
		summary = "포인트 적립 정책 활성화",
		description = "특정 포인트 적립 정책을 활성화합니다.",
		responses = {
			@ApiResponse(responseCode = "204", description = "포인트 적립 정책이 성공적으로 활성화되었습니다."),
			@ApiResponse(responseCode = "404", description = "포인트 적립 정책을 찾을 수 없습니다.")
		}
	)
	@AuthorizeRole({"POINT_ADMIN", "HEAD_ADMIN"})
	@PatchMapping("/{pointEarningPolicyId}/activate")
	public ResponseEntity<Void> activatePointEarningPolicy(@PathVariable Long pointEarningPolicyId) {
		pointEarningPolicyService.activatePointEarningPolicy(pointEarningPolicyId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	/**
	 * 특정 포인트 적립 정책을 비활성화합니다.
	 *
	 * @param pointEarningPolicyId 포인트 적립 정책 ID
	 * @return 응답 상태 코드 (204 NO CONTENT)
	 */
	@Operation(
		summary = "포인트 적립 정책 비활성화",
		description = "특정 포인트 적립 정책을 비활성화합니다.",
		responses = {
			@ApiResponse(responseCode = "204", description = "포인트 적립 정책이 성공적으로 비활성화되었습니다."),
			@ApiResponse(responseCode = "404", description = "포인트 적립 정책을 찾을 수 없습니다.")
		}
	)
	@AuthorizeRole({"POINT_ADMIN", "HEAD_ADMIN"})
	@PatchMapping("/{pointEarningPolicyId}/deactivate")
	public ResponseEntity<Void> deactivatePointEarningPolicy(@PathVariable Long pointEarningPolicyId) {
		pointEarningPolicyService.deactivatePointEarningPolicy(pointEarningPolicyId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	/**
	 * 특정 포인트 적립 정책을 삭제합니다.
	 *
	 * @param pointEarningPolicyId 포인트 적립 정책 ID
	 * @return 응답 상태 코드 (204 NO CONTENT)
	 */
	@Operation(
		summary = "포인트 적립 정책 삭제",
		description = "특정 포인트 적립 정책을 삭제합니다.",
		responses = {
			@ApiResponse(responseCode = "204", description = "포인트 적립 정책이 성공적으로 삭제되었습니다."),
			@ApiResponse(responseCode = "404", description = "포인트 적립 정책을 찾을 수 없습니다.")
		}
	)
	@AuthorizeRole({"POINT_ADMIN", "HEAD_ADMIN"})
	@DeleteMapping("/{pointEarningPolicyId}")
	public ResponseEntity<Void> deletePointEarningPolicy(@PathVariable Long pointEarningPolicyId) {
		pointEarningPolicyService.deletePointEarningPolicy(pointEarningPolicyId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
