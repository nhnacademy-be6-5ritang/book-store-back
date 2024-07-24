package com.nhnacademy.bookstoreback.order.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.nhnacademy.bookstoreback.order.domain.dto.request.CreateOrderRequest;
import com.nhnacademy.bookstoreback.user.domain.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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

	@Size(max = 10)
	@Column(name = "order_payer_name")
	private String orderPayerName;

	@Size(max = 11)
	@Column(name = "order_payer_number")
	private String orderPayerNumber;

	@Size(max = 30)
	@Column(name = "order_payer_email")
	private String orderPayerEmail;

	@Size(max = 100)
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

	@Size(max = 64)
	@NotBlank
	@Column(name = "order_info_id", nullable = false)
	private String orderInfoId;

	@ManyToOne
	@JoinColumn(name = "order_status_id")
	private OrderStatus orderStatus;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

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
		OrderStatus orderStatus) {
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

	public void updateUser(User user) {
		this.user = user;
	}

	public void updateCartOrder(CreateOrderRequest createOrderRequest, OrderStatus orderStatus) {
		this.orderPayerName = createOrderRequest.payerName();
		this.orderPayerEmail = createOrderRequest.payerEmail();
		this.orderPayerNumber = createOrderRequest.payerNumber();
		this.orderPayerAddress = createOrderRequest.payerAddress();
		this.orderPrice = createOrderRequest.orderPrice();
		this.orderPointSale = createOrderRequest.pointSale();
		this.orderCouponSale = createOrderRequest.couponSale();
		this.orderStatus = orderStatus;
		this.orderDate = LocalDateTime.now();
	}
}
