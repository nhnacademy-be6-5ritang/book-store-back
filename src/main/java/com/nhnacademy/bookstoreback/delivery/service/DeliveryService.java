package com.nhnacademy.bookstoreback.delivery.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nhnacademy.bookstoreback.auth.jwt.dto.CurrentUserDetails;
import com.nhnacademy.bookstoreback.delivery.domain.dto.request.CreateDeliveryRequest;
import com.nhnacademy.bookstoreback.delivery.domain.dto.request.UpdateDeliveryByOrderIdRequest;
import com.nhnacademy.bookstoreback.delivery.domain.dto.request.UpdateDeliveryRequest;
import com.nhnacademy.bookstoreback.delivery.domain.dto.response.CreateDeliveryResponse;
import com.nhnacademy.bookstoreback.delivery.domain.dto.response.GetDeliveryResponse;
import com.nhnacademy.bookstoreback.delivery.domain.dto.response.UpdateDeliveryAddOrderPolicyResponse;
import com.nhnacademy.bookstoreback.delivery.domain.dto.response.UpdateDeliveryResponse;
import com.nhnacademy.bookstoreback.delivery.domain.entity.Delivery;

/**
 * @author 이경헌
 * 배달과 관련된 서비스 인터페이스입니다.
 */
public interface DeliveryService {

	/**
	 * 배송을 스케줄링합니다.
	 *
	 * 배송의 발송 날짜가 설정되어 있고, 배송 상태가 특정 상태인 경우에만 호출됩니다.
	 *
	 * @param delivery 배송 정보 객체
	 */
	void scheduleDeliveries(Delivery delivery);

	/**
	 * 주어진 사용자의 배송 목록을 페이지 단위로 조회합니다.
	 *
	 * @param pageable 페이지 정보 및 정렬 기준을 포함하는 객체
	 * @return 조회된 배송 목록의 페이지 객체
	 */
	Page<GetDeliveryResponse> getDeliveriesByUserId(CurrentUserDetails currentUser, Pageable pageable);

	/**
	 * 주어진 배달 ID에 대한 배달 정보를 반환합니다.
	 *
	 * @param deliveryId 배달 ID
	 * @return 주어진 배달 ID에 대한 배달 정보
	 */
	GetDeliveryResponse getDelivery(Long deliveryId);

	/**
	 * 새로운 배달을 생성합니다.
	 *
	 * @param request 배달 생성 요청 정보
	 * @return 생성된 배달 정보
	 */
	CreateDeliveryResponse createDelivery(CreateDeliveryRequest request);

	/**
	 * 주어진 배달 ID에 대한 배달 정보를 수정합니다.
	 *
	 * @param deliveryId 수정할 배달 ID
	 * @param request 수정할 배달 정보 요청
	 * @return 수정된 배달 정보
	 */
	UpdateDeliveryResponse updateDelivery(Long deliveryId, UpdateDeliveryRequest request);

	/**
	 * 주어진 배달 ID에 대한 배달 정보를 삭제합니다.
	 *
	 * @param deliveryId 삭제할 배달 ID
	 */
	void deleteDelivery(Long deliveryId);

	/**
	 * 주어진 배달 ID에 대해 배달에 주문을 추가합니다.
	 *
	 * @param deliveryId 배달 ID
	 * @param orderId    주문 ID
	 * @return 주문 추가 후 업데이트된 배달 정보
	 */
	UpdateDeliveryAddOrderPolicyResponse updateDeliveryAddOrder(Long deliveryId, Long orderId);

	/**
	 * 주어진 주문 ID에 대한 배송 정보를 조회합니다.
	 *
	 * @param orderId 주문 ID
	 * @return 주어진 주문 ID에 대한 배송 정보
	 */
	GetDeliveryResponse getDeliveryByOrderId(Long orderId);

	/**
	 * 주어진 주문 ID에 대한 배송 정보를 업데이트합니다.
	 *
	 * @param orderId 주문 ID
	 * @param request 배송 상태 업데이트 요청
	 */
	void updateDeliveryByOrderId(Long orderId, UpdateDeliveryByOrderIdRequest request);
}
