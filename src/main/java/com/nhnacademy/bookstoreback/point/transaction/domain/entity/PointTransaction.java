package com.nhnacademy.bookstoreback.point.transaction.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.nhnacademy.bookstoreback.point.earningpolicy.domain.entity.PointEarningPolicy;
import com.nhnacademy.bookstoreback.point.transaction.domain.dto.request.CreatePointTransactionRequest;
import com.nhnacademy.bookstoreback.user.domain.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "point_transactions")
public class PointTransaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "point_transaction_id")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "point_earning_policy_id")
	private PointEarningPolicy pointEarningPolicy;

	@ManyToOne
	@JoinColumn(name = "user_id")
	@NotNull
	private User user;

	@Column(name = "point_transaction_amount")
	@NotNull
	private BigDecimal pointTransactionAmount;

	@Column(name = "point_transaction_date")
	@NotNull
	private LocalDateTime pointTransactionDate;

	@Builder
	public PointTransaction(
		PointEarningPolicy pointEarningPolicy,
		User user,
		BigDecimal pointTransactionAmount
	) {
		this.pointEarningPolicy = pointEarningPolicy;
		this.user = user;
		this.pointTransactionAmount = pointTransactionAmount;
		this.pointTransactionDate = LocalDateTime.now();
	}

	public static PointTransaction toEntity(User user, PointEarningPolicy pointEarningPolicy,
		CreatePointTransactionRequest createPointTransactionRequest) {
		return PointTransaction.builder()
			.user(user)
			.pointEarningPolicy(pointEarningPolicy)
			.pointTransactionAmount(createPointTransactionRequest.pointTransactionAmount())
			.build();
	}
}
