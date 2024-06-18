package com.nhnacademy.bookstoreback.book.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nhnacademy.bookstoreback.book.domain.entity.Delivery;
import com.nhnacademy.bookstoreback.book.domain.entity.DeliveryStatus;
import com.nhnacademy.bookstoreback.book.domain.entity.Order;
import com.nhnacademy.bookstoreback.book.repository.DeliveryRepository;
import com.nhnacademy.bookstoreback.book.repository.DeliveryStatusRepository;
import com.nhnacademy.bookstoreback.book.repository.OrderRepository;

@Service
public class DeliveryService {
	@Autowired
	private DeliveryRepository deliveryRepository;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private DeliveryStatusRepository deliveryStatusRepository;

	public Delivery creatDeliveries(Long ordersId, Delivery delivery) {
		Order order = orderRepository.findById(ordersId).orElse(null);
		delivery.setOrder(order);
		List<DeliveryStatus> deliveryStatuses = deliveryStatusRepository.findAll();
		for (DeliveryStatus deliveryStatus : deliveryStatuses) {
			if (deliveryStatus.getDeliveryStatusName().equals("배송중")) {
				delivery.setDeliveryStatus(deliveryStatus);
			}
		}
		return deliveryRepository.save(delivery);
	}

}
