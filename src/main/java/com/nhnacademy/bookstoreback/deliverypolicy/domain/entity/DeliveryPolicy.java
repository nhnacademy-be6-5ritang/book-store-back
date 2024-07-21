package com.nhnacademy.bookstoreback.deliverypolicy.domain.entity;

import java.math.BigDecimal;

import com.nhnacademy.bookstoreback.deliverypolicy.domain.dto.request.CreateDeliveryPolicyRequest;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author 이경헌
 * 배송 정책을 나타내는 엔티티입니다.
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "deliveries_policies")
public class DeliveryPolicy {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "delivery_policy_id")
	private Long deliveryPolicyId;

	@NotBlank
	@Size(max = 20)
	@Column(name = "delivery_policy_name", nullable = false, length = 20)
	private String deliveryPolicyName;

	@NotNull
	@Column(name = "delivery_policy_price", nullable = false)
	private BigDecimal deliveryPolicyPrice;

	@NotBlank
	@Size(max = 200)
	@Column(name = "delivery_policy_content", nullable = false, length = 200)
	private String deliveryPolicyContent;

	@NotNull
	@Column(name = "delivery_policy_standard_price", nullable = false)
	private BigDecimal deliveryPolicyStandardPrice;

	@Builder
	public DeliveryPolicy(String deliveryPolicyName, BigDecimal deliveryPolicyPrice, String deliveryPolicyContent,
		BigDecimal deliveryPolicyStandardPrice) {
		this.deliveryPolicyName = deliveryPolicyName;
		this.deliveryPolicyPrice = deliveryPolicyPrice;
		this.deliveryPolicyContent = deliveryPolicyContent;
		this.deliveryPolicyStandardPrice = deliveryPolicyStandardPrice;
	}

	public static DeliveryPolicy toEntity(CreateDeliveryPolicyRequest request) {
		return DeliveryPolicy.builder()
			.deliveryPolicyName(request.deliveryPolicyName())
			.deliveryPolicyPrice(request.deliveryPolicyPrice())
			.deliveryPolicyContent(request.deliveryPolicyContent())
			.deliveryPolicyStandardPrice(request.deliveryPolicyStandardPrice())
			.build();
	}

	public void updateDeliveryPolicy(String deliveryPolicyName, BigDecimal deliveryPolicyPrice,
		String deliveryPolicyContent, BigDecimal deliveryPolicyStandardPrice) {
		this.deliveryPolicyName = deliveryPolicyName;
		this.deliveryPolicyPrice = deliveryPolicyPrice;
		this.deliveryPolicyContent = deliveryPolicyContent;
		this.deliveryPolicyStandardPrice = deliveryPolicyStandardPrice;
	}
}
