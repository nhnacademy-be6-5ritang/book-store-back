package com.nhnacademy.bookstoreback.delivery.domain.entity;

import java.time.LocalDateTime;

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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "deliveries")
public class Delivery {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "delivery_id")
	private Long deliveryId;

	@Column(name = "delivery_sender_name")
	private String deliverySenderName;

	@Column(name = "delivery_sender_phone")
	private String deliverySenderPhone;

	@Column(name = "delivery_sender_date")
	private LocalDateTime deliverySenderDate;

	@Column(name = "delivery_sender_address")
	private String deliverySenderAddress;

	@Column(name = "delivery_receiver")
	private String deliveryReceiver;

	@Column(name = "delivery_receiver_phone")
	private String deliveryReceiverPhone;

	@Column(name = "delivery_receiver_date")
	private LocalDateTime deliveryReceiverDate;

	@Column(name = "delivery_receiver_address")
	private String deliveryReceiverAddress;

	@ManyToOne(optional = false)
	@JoinColumn(name = "order_id")
	private Order order;

	@ManyToOne(optional = false)
	@JoinColumn(name = "delivery_status_id")
	private DeliveryStatus deliveryStatus;

	@Builder
	public Delivery(String deliverySenderName, String deliverySenderPhone, LocalDateTime deliverySenderDate,
		String deliverySenderAddress, String deliveryReceiver, String deliveryReceiverPhone,
		LocalDateTime deliveryReceiverDate, String deliveryReceiverAddress, Order order,
		DeliveryStatus deliveryStatus) {
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
	}

	public void setOrder(Order order) {
		if (this.order != null) {
			this.order.removeDelivery(this);
		}
		this.order = order;
		this.order.addDelivery(this);
	}

	public void updateDeliveryStatus(DeliveryStatus deliveryStatus) {
		this.deliveryStatus = deliveryStatus;
	}
}
