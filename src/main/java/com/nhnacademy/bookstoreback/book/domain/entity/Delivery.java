package com.nhnacademy.bookstoreback.book.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Deliveries")
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
	private String deliverySenderDate;

	@Column(name = "delivery_sender_address")
	private String deliverySenderAddress;

	@Column(name = "delivery_receiver")
	private String deliveryReceiver;

	@Column(name = "delivery_receiver_phone")
	private String deliveryReceiverPhone;

	@Column(name = "delivery_receiver_date")
	private String deliveryReceiverDate;

	@Column(name = "delivery_receiver_address")
	private String deliveryReceiverAddress;

	@ManyToOne
	@JoinColumn(name = "order_id")
	@JsonBackReference(value = "order-delivery")
	private Order order;

	public void setOrder(Order order) {
		if (this.order != null) {
			this.order.removeDelivery(this);
		}
		this.order = order;
		this.order.addDelivery(this);
	}

	@OneToOne
	@JoinColumn(name = "delivery_status_id")
	private DeliveryStatus deliveryStatus;

}
