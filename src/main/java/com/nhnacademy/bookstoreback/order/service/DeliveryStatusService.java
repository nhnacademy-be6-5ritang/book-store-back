package com.nhnacademy.bookstoreback.order.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nhnacademy.bookstoreback.order.domain.entity.DeliveryStatus;
import com.nhnacademy.bookstoreback.order.repository.DeliveryStatusRepository;

@Service
public class DeliveryStatusService {
	@Autowired
	private DeliveryStatusRepository deliveryStatusRepository;

	public DeliveryStatus create(DeliveryStatus deliveryStatus) {
		return deliveryStatusRepository.save(deliveryStatus);
	}
}