package com.nhnacademy.bookstoreback.order.service;

import org.springframework.stereotype.Service;

import com.nhnacademy.bookstoreback.order.domain.entity.DeliveryStatus;
import com.nhnacademy.bookstoreback.order.repository.DeliveryStatusRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeliveryStatusService {

	private final DeliveryStatusRepository deliveryStatusRepository;

	public DeliveryStatus create(DeliveryStatus deliveryStatus) {
		return deliveryStatusRepository.save(deliveryStatus);
	}
}
