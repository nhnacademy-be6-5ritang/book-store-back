package com.nhnacademy.bookstoreback.delivery.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.nhnacademy.bookstoreback.delivery.domain.entity.Delivery;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
	Delivery findByDeliveryStatus_DeliveryStatusId(Long deliveryStatusId);

	Page<Delivery> findAllByOrder_Cart_User_Id(Long userId, Pageable pageable);
}
