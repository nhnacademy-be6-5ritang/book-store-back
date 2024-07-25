package com.nhnacademy.bookstoreback.order.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nhnacademy.bookstoreback.order.domain.entity.BookOrder;

public interface BookOrderRepository extends JpaRepository<BookOrder, Long>, CustomBookOrderRepository {
	BookOrder findByOrder_OrderId(Long orderId);

	List<BookOrder> findByOrder_OrderInfoId(String orderId);

	List<BookOrder> findAllByOrder_User_IdAndOrder_OrderStatus_OrderStatusName(Long userId, String orderStatus);

	List<BookOrder> findAllByBook_BookId(Long bookId);
}
