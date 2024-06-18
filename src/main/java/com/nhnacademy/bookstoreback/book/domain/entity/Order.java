package com.nhnacademy.bookstoreback.book.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
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
@Table(name = "Orders")
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "order_id")
	private Long orderId;

	@Column(name = "order_payer_name")
	private String orderPayerName;

	@Column(name = "order_payer_number")
	private String orderPayerNumber;

	@Column(name = "order_payer_email")
	private String orderPayerEmail;

	@Column(name = "order_payer_address")
	private String orderPayerAddress;

	@Column(name = "order_date")
	private LocalDateTime orderDate;

	@Column(name = "order_price")
	private BigDecimal orderPrice;

	@Column(name = "order_point_sale")
	private BigDecimal orderPointSale;

	@Column(name = "order_coupon_sale")
	private BigDecimal orderCouponSale;

	@Column(name = "order_info_id")
	private String orderInfoId;

	@OneToOne
	@JoinColumn(name = "order_status_id")
	private OrderStatus orderStatus;

	@OneToOne
	@JoinColumn(name = "wrapping_paper_id")
	private WrappingPaper wrappingPaper;

	@OneToMany(mappedBy = "order", fetch = FetchType.EAGER)
	@Fetch(FetchMode.SUBSELECT)
	@JsonManagedReference(value = "order-delivery")
	private List<Delivery> deliveries = new ArrayList<>();

	public void addDelivery(Delivery delivery) {
		this.deliveries.add(delivery);
	}

	public void removeDelivery(Delivery delivery) {
		this.deliveries.remove(delivery);
	}
}
