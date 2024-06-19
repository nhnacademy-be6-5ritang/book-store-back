package com.nhnacademy.bookstoreback.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nhnacademy.bookstoreback.order.domain.entity.DeliveryStatus;

public interface DeliveryStatusRepository extends JpaRepository<DeliveryStatus, Long> {
}
