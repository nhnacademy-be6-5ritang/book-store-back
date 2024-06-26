package com.nhnacademy.bookstoreback.deliverystatus.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nhnacademy.bookstoreback.deliverystatus.domain.dto.request.CreateDeliveryStatusRequest;
import com.nhnacademy.bookstoreback.deliverystatus.domain.dto.request.UpdateDeliveryStatusRequest;
import com.nhnacademy.bookstoreback.deliverystatus.domain.dto.response.CreateDeliveryStatusResponse;
import com.nhnacademy.bookstoreback.deliverystatus.domain.dto.response.GetDeliveryStatusResponse;
import com.nhnacademy.bookstoreback.deliverystatus.domain.dto.response.UpdateDeliveryStatusResponse;
import com.nhnacademy.bookstoreback.deliverystatus.domain.entity.DeliveryStatus;
import com.nhnacademy.bookstoreback.deliverystatus.repository.DeliveryStatusRepository;
import com.nhnacademy.bookstoreback.deliverystatus.service.DeliveryStatusService;
import com.nhnacademy.bookstoreback.global.exception.NotFoundException;
import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;

import lombok.RequiredArgsConstructor;

/**
 * @author 이경헌
 * 배송 상태 관리 서비스의 구현체입니다.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class DeliveryStatusServiceImpl implements DeliveryStatusService {
	private final DeliveryStatusRepository deliveryStatusRepository;

	/**
	 * 특정 배송 상태의 정보를 조회합니다.
	 *
	 * @param deliveryStatusId 조회할 배송 상태의 ID
	 * @return 조회된 배송 상태 정보
	 * @throws NotFoundException 해당 ID에 해당하는 배송 상태가 없는 경우 발생합니다.
	 */
	@Override
	@Transactional(readOnly = true)
	public GetDeliveryStatusResponse getDeliveryStatus(Long deliveryStatusId) {
		DeliveryStatus deliveryStatus = deliveryStatusRepository.findById(deliveryStatusId).orElseThrow(() -> {
			String errorMessage = String.format("해당 배달상태 '%d'는 존재하지 않는 배달 상태 입니다.", deliveryStatusId);
			ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.NOT_FOUND, LocalDateTime.now());
			return new NotFoundException(errorStatus);
		});

		return GetDeliveryStatusResponse.builder()
			.deliveryStatusId(deliveryStatusId)
			.deliveryStatusName(deliveryStatus.getDeliveryStatusName())
			.build();
	}

	/**
	 * 모든 배송 상태 목록을 조회합니다.
	 *
	 * @return 모든 배송 상태 목록
	 */
	@Override
	public List<GetDeliveryStatusResponse> getDeliveryStatuses() {
		return deliveryStatusRepository.findAll().stream()
			.map(deliveryStatus -> GetDeliveryStatusResponse.builder()
				.deliveryStatusId(deliveryStatus.getDeliveryStatusId())
				.deliveryStatusName(deliveryStatus.getDeliveryStatusName())
				.build())
			.toList();
	}

	/**
	 * 새로운 배송 상태를 생성합니다.
	 *
	 * @param request 생성할 배송 상태의 정보 요청 객체
	 * @return 생성된 배송 상태 정보
	 */
	@Override
	public CreateDeliveryStatusResponse createDeliveryStatus(CreateDeliveryStatusRequest request) {
		DeliveryStatus deliveryStatus = new DeliveryStatus(request.deliveryStatusName());
		deliveryStatusRepository.save(deliveryStatus);

		return CreateDeliveryStatusResponse.builder()
			.deliveryStatusId(deliveryStatus.getDeliveryStatusId())
			.deliveryStatusName(deliveryStatus.getDeliveryStatusName())
			.build();
	}

	/**
	 * 특정 배송 상태를 수정합니다.
	 *
	 * @param deliveryStatusId 수정할 배송 상태의 ID
	 * @param request          수정할 배송 상태의 정보 요청 객체
	 * @return 수정된 배송 상태 정보
	 * @throws NotFoundException 해당 ID에 해당하는 배송 상태가 없는 경우 발생합니다.
	 */
	@Override
	public UpdateDeliveryStatusResponse updateDeliveryStatus(Long deliveryStatusId,
		UpdateDeliveryStatusRequest request) {
		DeliveryStatus deliveryStatus = deliveryStatusRepository.findById(deliveryStatusId).orElseThrow(() -> {
			String errorMessage = String.format("해당 배달상태 '%d'는 존재하지 않는 배달 상태 입니다.", deliveryStatusId);
			ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.NOT_FOUND, LocalDateTime.now());
			return new NotFoundException(errorStatus);
		});

		deliveryStatus.updateDeliveryStatus(request.deliveryStatusName());

		deliveryStatusRepository.save(deliveryStatus);

		return UpdateDeliveryStatusResponse.builder()
			.deliveryStatusId(deliveryStatus.getDeliveryStatusId())
			.deliveryStatusName(deliveryStatus.getDeliveryStatusName())
			.build();
	}

	/**
	 * 특정 배송 상태를 삭제합니다.
	 *
	 * @param deliveryStatusId 삭제할 배송 상태의 ID
	 */
	@Override
	public void deleteDeliveryStatus(Long deliveryStatusId) {
		deliveryStatusRepository.deleteById(deliveryStatusId);
	}
}
