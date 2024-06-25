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

@Service
@RequiredArgsConstructor
@Transactional
public class DeliveryStatusServiceImpl implements DeliveryStatusService {
	private final DeliveryStatusRepository deliveryStatusRepository;

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

	@Override
	public List<GetDeliveryStatusResponse> getDeliveryStatuses() {
		return deliveryStatusRepository.findAll().stream()
			.map(deliveryStatus -> GetDeliveryStatusResponse.builder()
				.deliveryStatusId(deliveryStatus.getDeliveryStatusId())
				.deliveryStatusName(deliveryStatus.getDeliveryStatusName())
				.build())
			.toList();
	}

	@Override
	public CreateDeliveryStatusResponse createDeliveryStatus(CreateDeliveryStatusRequest request) {
		DeliveryStatus deliveryStatus = new DeliveryStatus(request.deliveryStatusName());
		deliveryStatusRepository.save(deliveryStatus);

		return CreateDeliveryStatusResponse.builder()
			.deliveryStatusId(deliveryStatus.getDeliveryStatusId())
			.deliveryStatusName(deliveryStatus.getDeliveryStatusName())
			.build();
	}

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

	@Override
	public void deleteDeliveryStatus(Long deliveryStatusId) {
		deliveryStatusRepository.deleteById(deliveryStatusId);
	}
}
