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
 * @author 이경헌
 * 배달 정보를 관리하는 컨트롤러입니다.
 */
@Tag(name = "Delivery", description = "배송 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/deliveries")
public class DeliveryController {
	private final DeliveryService deliveryService;

	/**
	 * 현재 사용자에 대한 배달 정보를 페이지 단위로 조회합니다.
	 *
	 * @param currentUser 현재 사용자 정보
	 * @param pageable 페이징 정보
	 * @return 현재 사용자에 대한 배달 정보의 페이지
	 */
	@Operation(
		summary = "현재 사용자 배달 목록 조회",
		description = "현재 사용자에 대한 배달 정보를 페이지 단위로 조회합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "배달 목록 조회 성공"),
	})
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
	@Operation(
		summary = "배달 정보 조회",
		description = "주어진 ID에 해당하는 배달 정보를 조회합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "배달 정보 조회 성공"),
		@ApiResponse(responseCode = "404", description = "배달 정보를 찾을 수 없습니다.")
	})
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
	@Operation(
		summary = "배달 생성",
		description = "새로운 배달을 생성합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "배달 생성 성공"),
		@ApiResponse(responseCode = "400", description = "잘못된 요청 데이터입니다.")
	})
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
	@Operation(
		summary = "배달 정보 업데이트",
		description = "주어진 ID에 해당하는 배달 정보를 업데이트합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "배달 정보 업데이트 성공"),
		@ApiResponse(responseCode = "404", description = "배달 정보를 찾을 수 없습니다."),
		@ApiResponse(responseCode = "400", description = "잘못된 요청 데이터입니다.")
	})
	@PutMapping("/{deliveryId}")
	public ResponseEntity<UpdateDeliveryResponse> updateDelivery(@PathVariable Long deliveryId,
		@Valid @RequestBody UpdateDeliveryRequest request) {
		return ResponseEntity.status(HttpStatus.OK).body(deliveryService.updateDelivery(deliveryId, request));
	}

	/**
	 * 배달에 주문을 추가합니다.
	 *
	 * @param deliveryId 배달 ID
	 * @param orderId    주문 ID
	 * @return 업데이트된 배달 정보
	 */
	@Operation(
		summary = "배달에 주문 추가",
		description = "주어진 배달에 주문을 추가합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "배달에 주문 추가 성공"),
		@ApiResponse(responseCode = "404", description = "배달 또는 주문 정보를 찾을 수 없습니다.")
	})
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
	 * @return 상태 코드 200 (OK)를 포함하는 {@link ResponseEntity} 객체
	 */
	@Operation(
		summary = "배달 삭제",
		description = "주어진 ID에 해당하는 배달 정보를 삭제합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "204", description = "배달 삭제 성공")
	})
	@DeleteMapping("/{deliveryId}")
	public ResponseEntity<Void> deleteDelivery(@PathVariable Long deliveryId) {
		deliveryService.deleteDelivery(deliveryId);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	/**
	 * 주문 아이디로 배송 정보를 가져옵니다.
	 *
	 * @param orderId 주문 아이디
	 * @return 배송 정보
	 */
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

	/**
	 * 관리자가 배송중으로 배송 상태를 변경합니다.
	 *
	 * @param orderId 주문 아이디
	 * @param request 배송 상태 업데이트 요청
	 * @return 상태 코드 200 (OK)
	 */
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
		return ResponseEntity.status(HttpStatus.OK).build();
	}
}
