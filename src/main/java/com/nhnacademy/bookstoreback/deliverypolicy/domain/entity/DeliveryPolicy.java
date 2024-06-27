package com.nhnacademy.bookstoreback.deliverypolicy.domain.entity;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "deliveries_policies")
public class DeliveryPolicy {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JoinColumn(name = "delivery_policy_id")
	private Long deliveryPolicyId;

	@JoinColumn(name = "delivery_policy_name")
	private String deliveryPolicyName;

	@JoinColumn(name = "delivery_policy_price")
	private BigDecimal deliveryPolicyPrice;

	@JoinColumn(name = "delivery_policy_content")
	private String deliveryPolicyContent;

	@JoinColumn(name = "delivery_policy_standard_price")
	private BigDecimal deliveryPolicyStandardPrice;

	public DeliveryPolicy(String deliveryPolicyName, BigDecimal deliveryPolicyPrice, String deliveryPolicyContent,
		BigDecimal deliveryPolicyStandardPrice) {
		this.deliveryPolicyName = deliveryPolicyName;
		this.deliveryPolicyPrice = deliveryPolicyPrice;
		this.deliveryPolicyContent = deliveryPolicyContent;
		this.deliveryPolicyStandardPrice = deliveryPolicyStandardPrice;
	}

	public void updateDeliveryPolicy(String deliveryPolicyName, BigDecimal deliveryPolicyPrice,
		String deliveryPolicyContent, BigDecimal deliveryPolicyStandardPrice) {
		this.deliveryPolicyName = deliveryPolicyName;
		this.deliveryPolicyPrice = deliveryPolicyPrice;
		this.deliveryPolicyContent = deliveryPolicyContent;
		this.deliveryPolicyStandardPrice = deliveryPolicyStandardPrice;
	}
}
