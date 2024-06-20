package com.nhnacademy.bookstoreback.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nhnacademy.bookstoreback.order.domain.entity.WrappingPaper;

public interface WrappingPaperRepository extends JpaRepository<WrappingPaper, Long> {
	public WrappingPaper findByOrder_OrderId(Long orderId);
}
