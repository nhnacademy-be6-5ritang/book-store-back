package com.nhnacademy.bookstoreback.deliverystatus.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nhnacademy.bookstoreback.deliverystatus.domain.entity.DeliveryStatus;

public interface DeliveryStatusRepository extends JpaRepository<DeliveryStatus, Long> {
	DeliveryStatus findDeliveryStatusByDeliveryStatusName(String deliveryStatusName);
}
