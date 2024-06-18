package com.nhnacademy.bookstoreback.book.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nhnacademy.bookstoreback.book.domain.entity.Delivery;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
	public Delivery findByDeliveryStatus_DeliveryStatusId(Long deliveryStatusId);
}
