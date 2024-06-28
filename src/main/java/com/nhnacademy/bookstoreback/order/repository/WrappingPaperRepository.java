package com.nhnacademy.bookstoreback.order.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nhnacademy.bookstoreback.order.domain.entity.WrappingPaper;

public interface WrappingPaperRepository extends JpaRepository<WrappingPaper, Long> {
	List<WrappingPaper> findAllByBookOrder_OrderListId(Long orderId);
}
