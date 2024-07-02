package com.nhnacademy.bookstoreback.payment.dto.entitiy;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.nhnacademy.bookstoreback.order.domain.entity.Order;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "payments")
public class Payment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "payment_id")
	private Long paymentId;

	@Column(name = "payment_Key")
	private String paymentKey;

	@OneToOne
	@JoinColumn(name = "order_id")
	private Order order;

	@Column(name = "payment_status")
	private String status;

	@Column(name = "payemnt_amount")
	private BigDecimal amount;

	@Column(name = "payment_date")
	private LocalDateTime paymentDate;

	@Builder
	public Payment(String paymentKey, Order order, BigDecimal amount, String status, LocalDateTime paymentDate) {
		this.paymentKey = paymentKey;
		this.order = order;
		this.amount = amount;
		this.status = status;
		this.paymentDate = paymentDate;
	}

	public static Payment toEntity(String paymentKey, Order order, BigDecimal amount, String status,
		LocalDateTime paymentDate) {
		return Payment.builder()
			.paymentKey(paymentKey)
			.order(order)
			.amount(amount)
			.status(status)
			.paymentDate(paymentDate)
			.build();
	}

}
