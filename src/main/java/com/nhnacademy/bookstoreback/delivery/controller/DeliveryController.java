package com.nhnacademy.bookstoreback.delivery.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nhnacademy.bookstoreback.delivery.domain.dto.request.CreateDeliveryRequest;
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
@RequestMapping("/users/{userId}/deliveries")
public class DeliveryController {
	private final DeliveryService deliveryService;

	/**
	 * 특정 사용자의 배달 목록을 페이징 처리하여 반환합니다.
	 *
	 * @param page 페이지 번호
	 * @param size 페이지 당 항목 수
	 * @param sort 정렬 기준 (옵션)
	 * @param userId 배달 목록을 조회할 사용자의 ID
	 * @return 사용자의 배달 목록을 포함하는 {@link Page} 객체
	 */
	@GetMapping
	public Page<GetDeliveryResponse> getDeliveriesByUserId(@RequestParam("page") int page,
		@RequestParam("size") int size,
		@RequestParam(required = false) String sort, @PathVariable Long userId) {

		Pageable pageable;
		if (sort != null) {
			pageable = PageRequest.of(page, size, Sort.by(sort));
		} else {
			pageable = PageRequest.of(page, size);
		}
		return deliveryService.getDeliveriesByUserId(userId, pageable);
	}

	/**
	 * 주어진 배달 ID에 해당하는 배달 정보를 조회합니다.
	 *
	 * @param deliveryId 조회할 배달의 ID
	 * @return HTTP 상태 코드 OK(200)와 함께 조회된 배달 정보
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
	 * 지정된 배달 ID에 대한 배달 상태 정보를 업데이트합니다.
	 *
	 * @param deliveryId 업데이트할 배달의 ID
	 * @param request 새로운 배달 정보를 포함하는 업데이트 요청의 세부 사항
	 * @return 업데이트된 배달 정보상태 정보를 포함하는 {@link UpdateDeliveryResponse} 객체
	 */
	@PutMapping("/{deliveryId}")
	public UpdateDeliveryResponse updateDelivery(@PathVariable Long deliveryId,
		@RequestBody UpdateDeliveryRequest request) {
		return deliveryService.updateDelivery(deliveryId, request);
	}

	/**
	 * 지정된 배달 ID에 대한 배달 정보를 삭제합니다.
	 *
	 * @param deliveryId 삭제할 배달의 ID
	 */
	@DeleteMapping("/{deliveryId}")
	public void deleteDelivery(@PathVariable Long deliveryId) {
		deliveryService.deleteDelivery(deliveryId);
	}

}
