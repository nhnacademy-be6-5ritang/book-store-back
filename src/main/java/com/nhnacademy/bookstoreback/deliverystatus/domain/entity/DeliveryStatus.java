package com.nhnacademy.bookstoreback.deliverystatus.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "deliveries_statuses")
public class DeliveryStatus {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "delivery_status_id")
	private Long deliveryStatusId;

	@Column(name = "delivery_status_name")
	private String deliveryStatusName;

	public DeliveryStatus(String deliveryStatusName) {
		this.deliveryStatusName = deliveryStatusName;
	}

	public void updateDeliveryStatus(String deliveryStatusName) {
		this.deliveryStatusName = deliveryStatusName;
	}

}
