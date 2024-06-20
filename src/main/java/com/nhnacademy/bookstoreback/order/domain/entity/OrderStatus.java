package com.nhnacademy.bookstoreback.order.domain.entity;

import com.nhnacademy.bookstoreback.order.domain.dto.request.CreateOrderStatusRequest;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderStatus {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "order_status_id")
	private Long orderStatusId;

	@Column(name = "order_status_name")
	private String orderStatusName;

	@Builder
	public OrderStatus(
		String orderStatusName) {
		this.orderStatusName = orderStatusName;
	}

	public static OrderStatus toEntity(CreateOrderStatusRequest createOrderStatusRequest) {
		return OrderStatus.builder()
			.orderStatusName(createOrderStatusRequest.orderStatusName())
			.build();
	}

	public void updateName(String name) {
		this.orderStatusName = name;
	}
}
