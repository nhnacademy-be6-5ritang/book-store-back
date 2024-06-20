package com.nhnacademy.bookstoreback.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nhnacademy.bookstoreback.order.domain.entity.BookOrder;

public interface BookOrderRepository extends JpaRepository<BookOrder, Long> {
	BookOrder findByOrder_OrderInfoId(String orderInfoId);
}
