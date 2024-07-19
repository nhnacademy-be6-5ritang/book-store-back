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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
	@Size(max = 64)
	private String paymentKey;

	@NotNull
	@OneToOne
	@JoinColumn(name = "order_id", nullable = false)
	private Order order;

	@NotBlank
	@Size(max = 15)
	@Column(name = "payment_status", nullable = false)
	private String status;

	@NotNull
	@Column(name = "payemnt_amount", nullable = false)
	private BigDecimal amount;

	@NotNull
	@Column(name = "payment_date", nullable = false)
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

	public void updateStatus(String status) {
		this.status = status;
	}
}
