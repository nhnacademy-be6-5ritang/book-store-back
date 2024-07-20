package com.nhnacademy.bookstoreback.address.domain.entity;

import com.nhnacademy.bookstoreback.address.domain.dto.request.RegisterAddressRequest;
import com.nhnacademy.bookstoreback.address.domain.dto.request.UpdateAddressRequest;
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
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.ws.rs.DefaultValue;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// TODO: Valation 처리 추가, 테스트 코드 작성
@Entity
@NoArgsConstructor
@Getter
@Table(name = "addresses")
public class Address {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "address_id")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "user_id")
	@NotNull
	private User user;

	@Column(name = "address_post_code")
	@NotBlank
	@Size(min = 5, max = 5)
	private String postCode;

	@Column(name = "address_base")
	@NotBlank
	@Size(max = 100)
	private String base;

	@Column(name = "address_detail")
	@NotBlank
	@Size(max = 50)
	private String detail;

	@Column(name = "address_alias")
	@Size(max = 30)
	private String alias;

	@Column(name = "is_default")
	@NotNull
	@DefaultValue("false")
	private Boolean isDefault;

	@Builder
	public Address(Long id, User user, String postCode, String base, String detail, String alias, Boolean isDefault) {
		this.id = id;
		this.user = user;
		this.postCode = postCode;
		this.base = base;
		this.detail = detail;
		this.alias = alias;
		this.isDefault = isDefault;
	}

	public static Address toEntity(RegisterAddressRequest registerAddressRequest, User user) {
		return Address.builder()
			.user(user)
			.alias(registerAddressRequest.alias())
			.postCode(registerAddressRequest.postCode())
			.base(registerAddressRequest.baseAddress())
			.detail(registerAddressRequest.detailAddress())
			.isDefault(false)
			.build();
	}

	public void update(UpdateAddressRequest updateAddressRequest) {
		this.alias = updateAddressRequest.alias();
		this.postCode = updateAddressRequest.postCode();
		this.base = updateAddressRequest.baseAddress();
		this.detail = updateAddressRequest.detailAddress();
	}

	public void updateIsDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}
}
