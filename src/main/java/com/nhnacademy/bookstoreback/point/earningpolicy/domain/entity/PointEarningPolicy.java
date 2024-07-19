package com.nhnacademy.bookstoreback.point.earningpolicy.domain.entity;

import java.math.BigDecimal;

import org.hibernate.annotations.Check;

import com.nhnacademy.bookstoreback.point.earningpolicy.domain.dto.request.CreatePointEarningPolicyRequest;
import com.nhnacademy.bookstoreback.point.earningpolicy.domain.dto.request.UpdatePointEarningPolicyRequest;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "point_earning_policies")
@Check(constraints = "point_earning_policy_status IN ('ACTIVE', 'DORMANT')")
public class PointEarningPolicy {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "point_earning_policy_id")
	private Long id;

	@NotBlank
	@Column(name = "point_earning_policy_type", nullable = false)
	@Size(max = 15)
	private String pointEarningPolicyType;

	@NotNull
	@Column(name = "point_earning_amount", nullable = false)
	@NotNull
	private BigDecimal pointEarningAmount;

	@NotBlank
	@Column(name = "point_earning_policy_status", nullable = false)
	@Size(max = 20)
	private String pointEarningPolicyStatus;

	@Builder
	public PointEarningPolicy(BigDecimal pointEarningAmount, String pointEarningPolicyType) {
		this.pointEarningPolicyType = pointEarningPolicyType;
		this.pointEarningAmount = pointEarningAmount;
	}

	public static PointEarningPolicy toEntity(CreatePointEarningPolicyRequest createPointEarningPolicyRequest) {
		return PointEarningPolicy.builder()
			.pointEarningPolicyType(createPointEarningPolicyRequest.pointEarningPolicyType())
			.pointEarningAmount(createPointEarningPolicyRequest.pointEarningAmount())
			.build();
	}

	public void update(UpdatePointEarningPolicyRequest updatePointEarningPolicyRequest) {
		this.pointEarningPolicyType = updatePointEarningPolicyRequest.pointEarningPolicyType();
		this.pointEarningAmount = updatePointEarningPolicyRequest.pointEarningAmount();
	}

	public void activate() {
		this.pointEarningPolicyStatus = "ACTIVE";
	}

	public void deactivate() {
		this.pointEarningPolicyStatus = "DORMANT";
	}

}
