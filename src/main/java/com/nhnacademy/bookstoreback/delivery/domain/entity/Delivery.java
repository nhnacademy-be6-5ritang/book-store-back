package com.nhnacademy.bookstoreback.delivery.domain.entity;

import java.time.LocalDateTime;

import com.nhnacademy.bookstoreback.delivery.domain.dto.request.CreateDeliveryRequest;
import com.nhnacademy.bookstoreback.deliverypolicy.domain.entity.DeliveryPolicy;
import com.nhnacademy.bookstoreback.deliverystatus.domain.entity.DeliveryStatus;
import com.nhnacademy.bookstoreback.order.domain.entity.Order;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author 이경헌
 * 배송을 나타내는 엔티티입니다.
 */
@Entity
@Getter
@NoArgsConstructor
@Table(name = "deliveries")
public class Delivery {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "delivery_id")
	private Long deliveryId;

	@Size(max = 20)
	@Column(name = "delivery_sender_name", length = 20)
	private String deliverySenderName;

	@Size(max = 20)
	@Column(name = "delivery_sender_phone", length = 20)
	private String deliverySenderPhone;

	@Column(name = "delivery_sender_date")
	private LocalDateTime deliverySenderDate;

	@Size(max = 100)
	@Column(name = "delivery_sender_address", length = 100)
	private String deliverySenderAddress;

	@NotNull
	@Size(min = 1, max = 20)
	@Column(name = "delivery_receiver", nullable = false, length = 20)
	private String deliveryReceiver;

	@NotNull
	@Size(min = 1, max = 20)
	@Column(name = "delivery_receiver_phone", nullable = false, length = 20)
	private String deliveryReceiverPhone;

	@Column(name = "delivery_receiver_date")
	private LocalDateTime deliveryReceiverDate;

	@NotNull
	@Size(min = 1, max = 100)
	@Column(name = "delivery_receiver_address", nullable = false, length = 100)
	private String deliveryReceiverAddress;

	@ManyToOne
	@JoinColumn(name = "order_id")
	private Order order;

	@NotNull
	@ManyToOne(optional = false)
	@JoinColumn(name = "delivery_status_id", nullable = false)
	private DeliveryStatus deliveryStatus;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "delivery_policy_id", nullable = false)
	private DeliveryPolicy deliveryPolicy;

	@Builder
	public Delivery(String deliverySenderName, String deliverySenderPhone, LocalDateTime deliverySenderDate,
		String deliverySenderAddress, String deliveryReceiver, String deliveryReceiverPhone,
		LocalDateTime deliveryReceiverDate, String deliveryReceiverAddress, Order order,
		DeliveryStatus deliveryStatus, DeliveryPolicy deliveryPolicy) {
		this.deliverySenderName = deliverySenderName;
		this.deliverySenderPhone = deliverySenderPhone;
		this.deliverySenderDate = deliverySenderDate;
		this.deliverySenderAddress = deliverySenderAddress;
		this.deliveryReceiver = deliveryReceiver;
		this.deliveryReceiverPhone = deliveryReceiverPhone;
		this.deliveryReceiverDate = deliveryReceiverDate;
		this.deliveryReceiverAddress = deliveryReceiverAddress;
		this.order = order;
		this.deliveryStatus = deliveryStatus;
		this.deliveryPolicy = deliveryPolicy;
	}

	public static Delivery toEntity(CreateDeliveryRequest request, DeliveryStatus deliveryStatus) {
		return Delivery.builder()
			.deliveryReceiver(request.deliveryReceiver())
			.deliveryReceiverPhone(request.deliveryReceiverPhone())
			.deliveryReceiverDate(request.deliveryReceiverDate())
			.deliveryReceiverAddress(request.deliveryReceiverAddress() + " " + request.deliveryReceiverAddress2())
			.deliveryStatus(deliveryStatus)
			.build();
	}

	public void updateDeliveryStatus(DeliveryStatus deliveryStatus) {
		this.deliveryStatus = deliveryStatus;
	}

	public void updateDeliveryReceiverDate(LocalDateTime deliveryReceiverDate) {
		this.deliveryReceiverDate = deliveryReceiverDate;
	}

	public void updateDeliveryAddOrder(Order order) {
		this.order = order;
	}

	public void updateDeliveryAddPolicy(DeliveryPolicy deliveryPolicy) {
		this.deliveryPolicy = deliveryPolicy;
	}

	public void updateDeliverySender(String deliverySenderName, String deliverySenderPhone,
		String deliverySenderAddress, DeliveryStatus deliveryStatus) {
		this.deliverySenderName = deliverySenderName;
		this.deliverySenderPhone = deliverySenderPhone;
		this.deliverySenderDate = LocalDateTime.now();
		this.deliverySenderAddress = deliverySenderAddress;
		this.deliveryStatus = deliveryStatus;
	}
}
