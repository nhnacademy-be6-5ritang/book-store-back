package com.nhnacademy.bookstoreback.deliverypolicy.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nhnacademy.bookstoreback.auth.annotation.AuthorizeRole;
import com.nhnacademy.bookstoreback.deliverypolicy.domain.dto.request.CreateDeliveryPolicyRequest;
import com.nhnacademy.bookstoreback.deliverypolicy.domain.dto.request.UpdateDeliveryPolicyRequest;
import com.nhnacademy.bookstoreback.deliverypolicy.domain.dto.response.GetDeliveryPoliciesResponse;
import com.nhnacademy.bookstoreback.deliverypolicy.domain.dto.response.GetDeliveryPolicyResponse;
import com.nhnacademy.bookstoreback.deliverypolicy.service.DeliveryPolicyService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * @author 이경헌
 * 배송지 정책 정보를 관리하는 컨트롤러입니다.
 */
@Tag(name = "DeliveryPolicy", description = "배송비 정책 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/deliveryPolicies")
public class DeliveryPolicyController {
	private final DeliveryPolicyService deliveryPolicyService;

	/**
	 * 모든 배송비 정책을 조회합니다.
	 *
	 * @return 모든 배송비 정책의 목록을 포함하는 {@link ResponseEntity} 객체
	 */
	@Operation(
		summary = "모든 배송비 정책 조회",
		description = "모든 배송비 정책의 목록을 조회합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "배송비 정책 목록 조회 성공")
	})
	@GetMapping
	public ResponseEntity<List<GetDeliveryPoliciesResponse>> getDeliveryPolicies() {
		return ResponseEntity.status(HttpStatus.OK).body(deliveryPolicyService.getDeliveryPolicies());
	}

	/**
	 * 주어진 배송비 정책 ID에 대한 상세 정보를 조회합니다.
	 *
	 * @param deliveryPolicyId 조회할 배송비 정책의 ID
	 * @return 조회된 배송비 정책의 상세 정보를 포함하는 {@link ResponseEntity} 객체
	 */
	@Operation(
		summary = "특정 배송비 정책 조회",
		description = "주어진 배송비 정책 ID에 대한 상세 정보를 조회합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "배송비 정책 조회 성공"),
		@ApiResponse(responseCode = "404", description = "요청한 배송비 정책을 찾을 수 없음")
	})
	@GetMapping("/{deliveryPolicyId}")
	public ResponseEntity<GetDeliveryPolicyResponse> getDeliveryPolicy(@PathVariable Long deliveryPolicyId) {
		return ResponseEntity.status(HttpStatus.OK).body(deliveryPolicyService.getDeliveryPolicy(deliveryPolicyId));
	}

	/**
	 * 새로운 배송비 정책을 생성합니다.
	 *
	 * @param request 생성할 배송비 정책의 정보가 포함된 {@link CreateDeliveryPolicyRequest} 객체
	 * @return 상태 코드 201 (CREATED)을 포함하는 {@link ResponseEntity} 객체
	 */
	@Operation(
		summary = "새로운 배송비 정책 생성",
		description = "새로운 배송비 정책을 생성합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "새로운 배송비 정책이 성공적으로 생성됨"),
		@ApiResponse(responseCode = "400", description = "잘못된 요청 데이터입니다."),
		@ApiResponse(responseCode = "409", description = "이미 존재하는 배송비 정책입니다.")
	})
	@AuthorizeRole({"DELIVERY_ADMIN", "HEAD_ADMIN"})
	@PostMapping
	public ResponseEntity<Void> createDeliveryPolicy(
		@Valid @RequestBody CreateDeliveryPolicyRequest request) {
		deliveryPolicyService.createDeliveryPolicy(request);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	/**
	 * 주어진 배송비 정책 ID에 대한 배송비 정책을 업데이트합니다.
	 *
	 * @param deliveryPolicyId 업데이트할 배송비 정책의 ID
	 * @param request          업데이트할 배송비 정책의 정보가 포함된 {@link UpdateDeliveryPolicyRequest} 객체
	 * @return 상태 코드 200 (OK)을 포함하는 {@link ResponseEntity} 객체
	 */
	@Operation(
		summary = "배송비 정책 업데이트",
		description = "주어진 배송비 정책 ID에 대한 배송비 정책을 업데이트합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "배송비 정책이 성공적으로 업데이트됨"),
		@ApiResponse(responseCode = "400", description = "잘못된 요청 데이터입니다."),
		@ApiResponse(responseCode = "404", description = "요청한 배송비 정책을 찾을 수 없음"),
		@ApiResponse(responseCode = "409", description = "이미 존재하는 배송비 정책입니다.")
	})
	@AuthorizeRole({"DELIVERY_ADMIN", "HEAD_ADMIN"})
	@PutMapping("/{deliveryPolicyId}")
	public ResponseEntity<Void> updateDeliveryPolicy(@PathVariable Long deliveryPolicyId,
		@Valid @RequestBody UpdateDeliveryPolicyRequest request) {
		deliveryPolicyService.updateDeliveryPolicy(deliveryPolicyId, request);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	/**
	 * 주어진 배송비 정책 ID에 대한 배송비 정책을 삭제합니다.
	 *
	 * @param deliveryPolicyId 삭제할 배송비 정책의 ID
	 * @return 상태 코드 200 (성공)을 포함하는 {@link ResponseEntity} 객체
	 */
	@Operation(
		summary = "배송비 정책 삭제",
		description = "주어진 배송비 정책 ID에 대한 배송비 정책을 삭제합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "배송비 정책이 성공적으로 삭제됨")
	})
	@AuthorizeRole({"DELIVERY_ADMIN", "HEAD_ADMIN"})
	@DeleteMapping("/{deliveryPolicyId}")
	public ResponseEntity<Void> deleteDeliveryPolicy(@PathVariable Long deliveryPolicyId) {
		deliveryPolicyService.deleteDeliveryPolicy(deliveryPolicyId);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	/**
	 * 주어진 배송 ID와 가격 이하의 배송비 정책을 조회합니다.
	 *
	 * @param deliveryId 배송비 정책이 적용될 배송의 ID
	 * @param price      배송비 정책의 기준 가격
	 * @return 기준 가격 이하의 배송비 정책을 포함하는 {@link GetDeliveryPolicyResponse} 객체
	 */
	@Operation(
		summary = "정책 추가 조회",
		description = "주어진 배송 ID와 가격 이하의 배송비 정책을 조회합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "배송비 정책 조회 성공"),
		@ApiResponse(responseCode = "404", description = "요청한 배송비 정책을 찾을 수 없음")
	})
	@PutMapping("/{deliveryId}/{price}/addPolicies")
	public ResponseEntity<GetDeliveryPolicyResponse> addPolicy(@PathVariable Long deliveryId,
		@PathVariable BigDecimal price) {
		return ResponseEntity.status(HttpStatus.OK)
			.body(
				deliveryPolicyService.findByDeliveryPolicyStandardPriceLessThanEqualOrderByDeliveryPolicyStandardPriceDesc(
					deliveryId, price));
	}
}
