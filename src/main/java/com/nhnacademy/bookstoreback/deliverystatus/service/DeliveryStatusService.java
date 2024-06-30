package com.nhnacademy.bookstoreback.deliverystatus.service;

import java.util.List;

import com.nhnacademy.bookstoreback.deliverystatus.domain.dto.request.CreateDeliveryStatusRequest;
import com.nhnacademy.bookstoreback.deliverystatus.domain.dto.request.UpdateDeliveryStatusRequest;
import com.nhnacademy.bookstoreback.deliverystatus.domain.dto.response.CreateDeliveryStatusResponse;
import com.nhnacademy.bookstoreback.deliverystatus.domain.dto.response.GetDeliveryStatusResponse;
import com.nhnacademy.bookstoreback.deliverystatus.domain.dto.response.UpdateDeliveryStatusResponse;

/**
 * @author 이경헌
 * 배송 상태 관리 서비스의 인터페이스입니다.
 */
public interface DeliveryStatusService {
	/**
	 * 주어진 배송 상태 ID에 해당하는 배송 상태를 조회합니다.
	 *
	 * @param deliveryStatusId 배송 상태의 ID
	 * @return 조회된 배송 상태 정보
	 */
	GetDeliveryStatusResponse getDeliveryStatus(Long deliveryStatusId);

	/**
	 * 모든 배송 상태 목록을 조회합니다.
	 *
	 * @return 모든 배송 상태 목록
	 */
	List<GetDeliveryStatusResponse> getDeliveryStatuses();

	/**
	 * 새로운 배송 상태를 생성합니다.
	 *
	 * @param request 생성할 배송 상태에 대한 요청 정보
	 * @return 생성된 배송 상태 정보
	 */
	CreateDeliveryStatusResponse createDeliveryStatus(CreateDeliveryStatusRequest request);

	/**
	 * 주어진 배송 상태 ID에 해당하는 배송 상태를 수정합니다.
	 *
	 * @param deliveryStatusId 수정할 배송 상태의 ID
	 * @param request          수정할 배송 상태에 대한 요청 정보
	 * @return 수정된 배송 상태 정보
	 */
	UpdateDeliveryStatusResponse updateDeliveryStatus(Long deliveryStatusId, UpdateDeliveryStatusRequest request);

	/**
	 * 주어진 배송 상태 ID에 해당하는 배송 상태를 삭제합니다.
	 *
	 * @param deliveryStatusId 삭제할 배송 상태의 ID
	 */
	void deleteDeliveryStatus(Long deliveryStatusId);
}
