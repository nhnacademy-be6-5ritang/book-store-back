package com.nhnacademy.bookstoreback.deliverypolicy.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nhnacademy.bookstoreback.deliverypolicy.domain.entity.DeliveryPolicy;

public interface DeliveryPolicyRepository extends JpaRepository<DeliveryPolicy, Long> {
}
