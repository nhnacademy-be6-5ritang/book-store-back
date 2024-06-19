package com.nhnacademy.bookstoreback.order.domain.entity;

import com.nhnacademy.bookstoreback.order.domain.dto.request.CreateOrderStatusRequest;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
}
