package com.nhnacademy.bookstoreback.delivery.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

import com.nhnacademy.bookstoreback.delivery.domain.dto.request.CreateDeliveryRequest;
import com.nhnacademy.bookstoreback.delivery.domain.dto.request.GetDeliveriesRequest;
import com.nhnacademy.bookstoreback.delivery.domain.dto.request.UpdateDeliveryRequest;
import com.nhnacademy.bookstoreback.delivery.domain.dto.response.CreateDeliveryResponse;
import com.nhnacademy.bookstoreback.delivery.domain.dto.response.GetDeliveryResponse;
import com.nhnacademy.bookstoreback.delivery.domain.dto.response.UpdateDeliveryResponse;
import com.nhnacademy.bookstoreback.delivery.service.DeliveryService;

import lombok.RequiredArgsConstructor;

/**
 * 배달 정보를 관리하는 컨트롤러입니다.
 * 배달 생성, 조회, 업데이트, 삭제 기능을 제공합니다.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/deliveries")
public class DeliveryController {
	private final DeliveryService deliveryService;

	@GetMapping
	public ResponseEntity<Page<GetDeliveryResponse>> getDeliveriesByUserId(Pageable pageable,
		@RequestBody GetDeliveriesRequest request) {

		return ResponseEntity.status(HttpStatus.OK).body(deliveryService.getDeliveriesByUserId(request, pageable));
	}

	/**
	 * 주어진 ID에 해당하는 배달 정보를 조회합니다.
	 *
	 * @param deliveryId 조회할 배달의 ID
	 * @return 조회된 배달 정보를 포함하는 {@link ResponseEntity} 객체
	 */
	@GetMapping("/{deliveryId}")
	public ResponseEntity<GetDeliveryResponse> getDelivery(@PathVariable Long deliveryId) {
		return ResponseEntity.status(HttpStatus.OK).body(deliveryService.getDelivery(deliveryId));
	}

	/**
	 * 새로운 배달을 생성합니다.
	 *
	 * @param request 배달 생성 요청의 세부 사항을 포함하는 객체
	 * @return 생성된 배달 정보를 포함하는 {@link ResponseEntity} 객체
	 */
	@PostMapping
	public ResponseEntity<CreateDeliveryResponse> createDelivery(@RequestBody CreateDeliveryRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(deliveryService.createDelivery(request));
	}

	/**
	 * 주어진 ID에 해당하는 배달 정보를 업데이트합니다.
	 *
	 * @param deliveryId 업데이트할 배달의 ID
	 * @param request    업데이트할 배달 정보가 포함된 객체
	 * @return 업데이트된 배달 정보를 포함하는 {@link ResponseEntity} 객체
	 */
	@PutMapping("/{deliveryId}")
	public ResponseEntity<UpdateDeliveryResponse> updateDelivery(@PathVariable Long deliveryId,
		@RequestBody UpdateDeliveryRequest request) {
		return ResponseEntity.status(HttpStatus.OK).body(deliveryService.updateDelivery(deliveryId, request));
	}

	/**
	 * 주어진 ID에 해당하는 배달 정보를 삭제합니다.
	 *
	 * @param deliveryId 삭제할 배달의 ID
	 * @return 상태 코드 204 (콘텐츠 없음)을 포함하는 {@link ResponseEntity} 객체
	 */
	@DeleteMapping("/{deliveryId}")
	public ResponseEntity<Void> deleteDelivery(@PathVariable Long deliveryId) {
		deliveryService.deleteDelivery(deliveryId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

}
