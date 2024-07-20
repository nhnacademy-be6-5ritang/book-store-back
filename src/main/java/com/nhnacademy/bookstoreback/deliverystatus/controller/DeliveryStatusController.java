package com.nhnacademy.bookstoreback.deliverystatus.controller;

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
import com.nhnacademy.bookstoreback.deliverystatus.domain.dto.request.CreateDeliveryStatusRequest;
import com.nhnacademy.bookstoreback.deliverystatus.domain.dto.request.UpdateDeliveryStatusRequest;
import com.nhnacademy.bookstoreback.deliverystatus.domain.dto.response.CreateDeliveryStatusResponse;
import com.nhnacademy.bookstoreback.deliverystatus.domain.dto.response.GetDeliveryStatusResponse;
import com.nhnacademy.bookstoreback.deliverystatus.domain.dto.response.UpdateDeliveryStatusResponse;
import com.nhnacademy.bookstoreback.deliverystatus.service.DeliveryStatusService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * @author 이경헌
 * 배송 상태에 관련된 API 요청을 처리하는 컨트롤러입니다.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/deliveryStatuses")
public class DeliveryStatusController {
	private final DeliveryStatusService deliveryStatusService;

	/**
	 * 모든 배송 상태 목록을 조회합니다.
	 *
	 * @return 모든 배송 상태 목록과 HTTP 상태 코드 200(OK)
	 */
	@GetMapping
	public ResponseEntity<List<GetDeliveryStatusResponse>> getDeliveryStatuses() {
		List<GetDeliveryStatusResponse> responses = deliveryStatusService.getDeliveryStatuses();
		return ResponseEntity.status(HttpStatus.OK).body(responses);
	}

	/**
	 * 특정 배송 상태를 조회합니다.
	 *
	 * @param deliveryStatusId 조회할 배송 상태의 ID
	 * @return 조회된 배송 상태 정보와 HTTP 상태 코드 200(OK)
	 */
	@GetMapping("/{deliveryStatusId}")
	public ResponseEntity<GetDeliveryStatusResponse> getDeliveryStatus(@PathVariable Long deliveryStatusId) {
		GetDeliveryStatusResponse response = deliveryStatusService.getDeliveryStatus(deliveryStatusId);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	/**
	 * 새로운 배송 상태를 생성합니다.
	 *
	 * @param request 생성할 배송 상태 정보
	 * @return 생성된 배송 상태 정보와 HTTP 상태 코드 201(CREATED)
	 */
	@AuthorizeRole({"DELIVERY_ADMIN", "HEAD_ADMIN"})
	@PostMapping
	public ResponseEntity<CreateDeliveryStatusResponse> createDeliveryStatus(
		@Valid @RequestBody CreateDeliveryStatusRequest request) {
		CreateDeliveryStatusResponse response = deliveryStatusService.createDeliveryStatus(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	/**
	 * 특정 배송 상태를 수정합니다.
	 *
	 * @param deliveryStatusId 수정할 배송 상태의 ID
	 * @param request 수정할 배송 상태 정보
	 * @return 수정된 배송 상태 정보와 HTTP 상태 코드 200(OK)
	 */
	@AuthorizeRole({"DELIVERY_ADMIN", "HEAD_ADMIN"})
	@PutMapping("/{deliveryStatusId}")
	public ResponseEntity<UpdateDeliveryStatusResponse> updateDeliveryStatus(@PathVariable Long deliveryStatusId,
		@Valid @RequestBody UpdateDeliveryStatusRequest request) {
		UpdateDeliveryStatusResponse response = deliveryStatusService.updateDeliveryStatus(deliveryStatusId, request);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	/**
	 * 특정 배송 상태를 삭제합니다.
	 *
	 * @param deliveryStatusId 삭제할 배송 상태의 ID
	 * @return HTTP 상태 코드 200(OK)
	 */
	@AuthorizeRole({"DELIVERY_ADMIN", "HEAD_ADMIN"})
	@DeleteMapping("/{deliveryStatusId}")
	public ResponseEntity<Void> deleteDeliveryStatus(@PathVariable Long deliveryStatusId) {
		deliveryStatusService.deleteDeliveryStatus(deliveryStatusId);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
}
