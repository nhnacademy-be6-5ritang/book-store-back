package com.nhnacademy.bookstoreback.order.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.nhnacademy.bookstoreback.cart.domain.entity.Cart;
import com.nhnacademy.bookstoreback.delivery.domain.entity.Delivery;
import com.nhnacademy.bookstoreback.order.domain.dto.request.CreateOrderRequest;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders")
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

	@ManyToOne
	@JoinColumn(name = "order_status_id")
	private OrderStatus orderStatus;

	@ManyToOne
	@JoinColumn(name = "cart_id")
	private Cart cart;

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

	@Builder
	public Order(
		String orderPayerName,
		String orderPayerEmail,
		String orderPayerNumber,
		String orderPayerAddress,
		BigDecimal orderPrice,
		BigDecimal orderPointSale,
		BigDecimal orderCouponSale,
		String orderInfoId,
		LocalDateTime orderDate,
		OrderStatus orderStatus,
		WrappingPaper wrappingPaper) {
		this.orderPayerName = orderPayerName;
		this.orderPayerNumber = orderPayerNumber;
		this.orderPayerEmail = orderPayerEmail;
		this.orderPayerAddress = orderPayerAddress;
		this.orderPrice = orderPrice;
		this.orderPointSale = orderPointSale;
		this.orderCouponSale = orderCouponSale;
		this.orderInfoId = orderInfoId;
		this.orderDate = orderDate;
		this.orderStatus = orderStatus;
	}

	public static Order toEntity(CreateOrderRequest createOrderRequest,
		OrderStatus orderStatus) {
		return Order.builder()
			.orderPayerName(createOrderRequest.payerName())
			.orderPayerEmail(createOrderRequest.payerEmail())
			.orderPayerNumber(createOrderRequest.payerNumber())
			.orderPayerAddress(createOrderRequest.payerAddress())
			.orderPrice(createOrderRequest.orderPrice())
			.orderPointSale(createOrderRequest.pointSale())
			.orderCouponSale(createOrderRequest.couponSale())
			.orderInfoId(UUID.randomUUID().toString())
			.orderDate(LocalDateTime.now())
			.orderStatus(orderStatus)
			.build();
	}

	public void updateOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}
}
