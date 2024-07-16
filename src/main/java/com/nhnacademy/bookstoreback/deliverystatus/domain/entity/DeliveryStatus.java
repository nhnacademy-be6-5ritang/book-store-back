package com.nhnacademy.bookstoreback.deliverystatus.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author 이경헌
 * 배송 상태를 나타내는 엔티티입니다.
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "deliveries_statuses")
public class DeliveryStatus {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "delivery_status_id")
	private Long deliveryStatusId;

	@NotBlank
	@Size(max = 10)
	@Column(name = "delivery_status_name", nullable = false, length = 10)
	private String deliveryStatusName;

	public DeliveryStatus(String deliveryStatusName) {
		this.deliveryStatusName = deliveryStatusName;
	}

	public void updateDeliveryStatus(String deliveryStatusName) {
		this.deliveryStatusName = deliveryStatusName;
	}

}
