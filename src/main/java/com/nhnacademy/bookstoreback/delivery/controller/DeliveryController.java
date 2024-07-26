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

import com.nhnacademy.bookstoreback.auth.annotation.CurrentUser;
import com.nhnacademy.bookstoreback.auth.jwt.dto.CurrentUserDetails;
import com.nhnacademy.bookstoreback.delivery.domain.dto.request.CreateDeliveryRequest;
import com.nhnacademy.bookstoreback.delivery.domain.dto.request.UpdateDeliveryByOrderIdRequest;
import com.nhnacademy.bookstoreback.delivery.domain.dto.request.UpdateDeliveryRequest;
import com.nhnacademy.bookstoreback.delivery.domain.dto.response.CreateDeliveryResponse;
import com.nhnacademy.bookstoreback.delivery.domain.dto.response.GetDeliveryResponse;
import com.nhnacademy.bookstoreback.delivery.domain.dto.response.UpdateDeliveryAddOrderPolicyResponse;
import com.nhnacademy.bookstoreback.delivery.domain.dto.response.UpdateDeliveryResponse;
import com.nhnacademy.bookstoreback.delivery.service.DeliveryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * 배달 정보를 관리하는 컨트롤러입니다.
 * 배달 생성, 조회, 업데이트, 삭제 기능을 제공합니다.
 */
@Tag(name = "Delivery", description = "배송 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/deliveries")
public class DeliveryController {
	private final DeliveryService deliveryService;

	@GetMapping("/me/page")
	public ResponseEntity<Page<GetDeliveryResponse>> getDeliveriesByUserId(@CurrentUser CurrentUserDetails currentUser,
		Pageable pageable) {

		return ResponseEntity.status(HttpStatus.OK).body(deliveryService.getDeliveriesByUserId(currentUser, pageable));
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
	public ResponseEntity<CreateDeliveryResponse> createDelivery(@Valid @RequestBody CreateDeliveryRequest request) {
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
		@Valid @RequestBody UpdateDeliveryRequest request) {
		return ResponseEntity.status(HttpStatus.OK).body(deliveryService.updateDelivery(deliveryId, request));
	}

	@PutMapping("/{deliveryId}/{orderId}/orders")
	ResponseEntity<UpdateDeliveryAddOrderPolicyResponse> addOrder(@PathVariable Long deliveryId,
		@PathVariable Long orderId) {
		return ResponseEntity.status(HttpStatus.OK)
			.body(deliveryService.updateDeliveryAddOrder(deliveryId, orderId));
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

	@Operation(
		summary = "주문 아이디로 배송 정보 가져오기",
		description = "주문 아이디로 배송 정보를 가져옵니다"
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "404", description = "배송 정보를 찾을 수 없습니다")
	})
	@GetMapping("/{orderId}/orders")
	public ResponseEntity<GetDeliveryResponse> getDeliveryByOrder(@PathVariable Long orderId) {
		return ResponseEntity.status(HttpStatus.OK).body(deliveryService.getDeliveryByOrderId(orderId));
	}

	@Operation(
		summary = "관리자 배송 승인",
		description = "관리자가 배송중으로 배송 상태를 변경시킵니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "404", description = "배송 정보를 찾을 수 없습니다")
	})
	@PutMapping("/sender/{orderId}")
	public ResponseEntity<Void> updateDeliveryByOrderId(@PathVariable Long orderId,
		@Valid @RequestBody UpdateDeliveryByOrderIdRequest request) {
		deliveryService.updateDeliveryByOrderId(orderId, request);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
