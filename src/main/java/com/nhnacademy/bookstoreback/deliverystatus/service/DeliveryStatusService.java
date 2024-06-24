package com.nhnacademy.bookstoreback.deliverystatus.service;

import java.util.List;

import com.nhnacademy.bookstoreback.deliverystatus.domain.dto.request.CreateDeliveryStatusRequest;
import com.nhnacademy.bookstoreback.deliverystatus.domain.dto.request.UpdateDeliveryStatusRequest;
import com.nhnacademy.bookstoreback.deliverystatus.domain.dto.response.CreateDeliveryStatusResponse;
import com.nhnacademy.bookstoreback.deliverystatus.domain.dto.response.GetDeliveryStatusResponse;
import com.nhnacademy.bookstoreback.deliverystatus.domain.dto.response.UpdateDeliveryStatusResponse;

public interface DeliveryStatusService {
	GetDeliveryStatusResponse getDeliveryStatus(Long deliveryStatusId);

	List<GetDeliveryStatusResponse> getDeliveryStatuses();

	CreateDeliveryStatusResponse createDeliveryStatus(CreateDeliveryStatusRequest request);

	UpdateDeliveryStatusResponse updateDeliveryStatus(Long deliveryStatusId, UpdateDeliveryStatusRequest request);

	void deleteDeliveryStatus(Long deliveryStatusId);
}
