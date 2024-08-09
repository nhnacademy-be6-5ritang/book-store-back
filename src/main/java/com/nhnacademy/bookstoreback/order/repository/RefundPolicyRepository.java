package com.nhnacademy.bookstoreback.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nhnacademy.bookstoreback.order.domain.entity.RefundPolicy;

/**
 * @author 김다운
 * RefundPolicy 엔티티에 대한 데이터베이스 작업을 수행하기 위한 JPA 리포지토리입니다.
 */
public interface RefundPolicyRepository extends JpaRepository<RefundPolicy, Long> {
}
