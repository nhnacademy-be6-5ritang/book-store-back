package com.nhnacademy.bookstoreback.point.transaction.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.nhnacademy.bookstoreback.point.transaction.domain.entity.PointTransaction;

public interface PointTransactionRepository extends JpaRepository<PointTransaction, Long> {
	Page<PointTransaction> findAllByUserId(Long userId, Pageable pageable);
}
