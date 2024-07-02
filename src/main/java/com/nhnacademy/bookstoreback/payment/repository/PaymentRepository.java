package com.nhnacademy.bookstoreback.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nhnacademy.bookstoreback.payment.dto.entitiy.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
