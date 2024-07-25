package com.nhnacademy.bookstoreback.order.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.nhnacademy.bookstoreback.order.domain.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
	Page<Order> findByOrderStatus_OrderStatusId(Long orderStatusId, Pageable page);

	List<Order> findAllByUserId(Long userId);

	Order findByOrderInfoId(String orderInfoId);

	Order findByOrderId(Long orderId);

	List<Order> findAllByOrderStatus_OrderStatusId(Long orderStatusId);

	Page<Order> findAllByUser_Id(Long userId, Pageable page);
}
