package com.nhnacademy.bookstoreback.deliverystatus.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nhnacademy.bookstoreback.deliverystatus.domain.dto.request.CreateDeliveryStatusRequest;
import com.nhnacademy.bookstoreback.deliverystatus.domain.dto.request.UpdateDeliveryStatusRequest;
import com.nhnacademy.bookstoreback.deliverystatus.domain.dto.response.GetDeliveryStatusResponse;
import com.nhnacademy.bookstoreback.deliverystatus.domain.entity.DeliveryStatus;
import com.nhnacademy.bookstoreback.deliverystatus.exception.DeliveryStatusAlreadyExistsException;
import com.nhnacademy.bookstoreback.deliverystatus.exception.DeliveryStatusNotFoundException;
import com.nhnacademy.bookstoreback.deliverystatus.repository.DeliveryStatusRepository;
import com.nhnacademy.bookstoreback.deliverystatus.service.DeliveryStatusService;

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
	 *{@inheritDoc}
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
	 *{@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public GetDeliveryStatusResponse getDeliveryStatus(Long deliveryStatusId) {
		DeliveryStatus deliveryStatus = deliveryStatusRepository.findById(deliveryStatusId)
			.orElseThrow(() -> new DeliveryStatusNotFoundException(deliveryStatusId));

		return GetDeliveryStatusResponse.builder()
			.deliveryStatusId(deliveryStatusId)
			.deliveryStatusName(deliveryStatus.getDeliveryStatusName())
			.build();
	}

	/**
	 *{@inheritDoc}
	 */
	@Override
	public void createDeliveryStatus(CreateDeliveryStatusRequest request) {
		if (deliveryStatusRepository.existsByDeliveryStatusName(request.deliveryStatusName())) {
			throw new DeliveryStatusAlreadyExistsException(request.deliveryStatusName());
		}
		deliveryStatusRepository.save(new DeliveryStatus(request.deliveryStatusName()));
	}

	/**
	 *{@inheritDoc}
	 */
	@Override
	public void updateDeliveryStatus(Long deliveryStatusId,
		UpdateDeliveryStatusRequest request) {
		DeliveryStatus deliveryStatus = deliveryStatusRepository.findById(deliveryStatusId)
			.orElseThrow(() -> new DeliveryStatusNotFoundException(deliveryStatusId));

		if (deliveryStatusRepository.existsByDeliveryStatusName(request.deliveryStatusName())) {
			throw new DeliveryStatusAlreadyExistsException(request.deliveryStatusName());
		}
		deliveryStatus.updateDeliveryStatus(request.deliveryStatusName());
	}

	/**
	 *{@inheritDoc}
	 */
	@Override
	public void deleteDeliveryStatus(Long deliveryStatusId) {
		deliveryStatusRepository.deleteById(deliveryStatusId);
	}
}
