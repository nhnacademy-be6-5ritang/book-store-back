package com.nhnacademy.bookstoreback.order.domain.entity;

import com.nhnacademy.bookstoreback.order.domain.dto.request.UpdateRefundPolicyRequest;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "refunds_policies")
public class RefundPolicy {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "refund_policy_id")
	private Long refundPolicyId;

	@Column(name = "refund_policy_content")
	private String refundPolicyContent;

	@Column(name = "refund_policy_date")
	private int refundPolicyDate;

	@Builder
	public RefundPolicy(
		Long refundPolicyId,
		String refundPolicyContent,
		int refundPolicyDate
	) {
		this.refundPolicyId = refundPolicyId;
		this.refundPolicyContent = refundPolicyContent;
		this.refundPolicyDate = refundPolicyDate;
	}

	public static RefundPolicy toEntity(String refundPolicyContent, int refundPolicyDate) {
		return RefundPolicy.builder()
			.refundPolicyContent(refundPolicyContent)
			.refundPolicyDate(refundPolicyDate)
			.build();
	}

	public void update(UpdateRefundPolicyRequest request) {
		this.refundPolicyContent = request.refundPolicyContent();
		this.refundPolicyDate = request.refundPolicyDate();
	}
}
