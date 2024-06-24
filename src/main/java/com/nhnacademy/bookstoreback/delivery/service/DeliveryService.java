package com.nhnacademy.bookstoreback.delivery.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nhnacademy.bookstoreback.delivery.domain.dto.request.CreateDeliveryRequest;
import com.nhnacademy.bookstoreback.delivery.domain.dto.request.GetDeliveriesRequest;
import com.nhnacademy.bookstoreback.delivery.domain.dto.request.UpdateDeliveryRequest;
import com.nhnacademy.bookstoreback.delivery.domain.dto.response.CreateDeliveryResponse;
import com.nhnacademy.bookstoreback.delivery.domain.dto.response.GetDeliveryResponse;
import com.nhnacademy.bookstoreback.delivery.domain.dto.response.UpdateDeliveryResponse;

/**
 * DeliveryService는 배달과 관련된 서비스 인터페이스입니다.
 * 이 인터페이스는 배달의 생성, 조회, 수정 및 삭제 작업을 정의합니다.
 */
public interface DeliveryService {
	/**
	 * 주어진 사용자의 배송 목록을 페이지 단위로 조회합니다.
	 *
	 * @param request  사용자의 배송 조회 요청 정보를 포함하는 객체
	 * @param pageable 페이지 정보 및 정렬 기준을 포함하는 객체
	 * @return 조회된 배송 목록의 페이지 객체
	 */
	Page<GetDeliveryResponse> getDeliveriesByUserId(GetDeliveriesRequest request, Pageable pageable);

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
}
