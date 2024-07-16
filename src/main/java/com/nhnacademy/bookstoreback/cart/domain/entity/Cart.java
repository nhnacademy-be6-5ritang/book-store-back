package com.nhnacademy.bookstoreback.cart.domain.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author 이경헌
 * 장바구니를 나타내는 엔티티입니다.
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "carts")
public class Cart {
	@Id
	@NotNull
	@Size(min = 1, max = 36)
	@Column(name = "cart_id", nullable = false, length = 36)
	private String cartId = UUID.randomUUID().toString();

	@NotNull
	@Column(name = "user_id", nullable = false)
	private Long userId;

	public Cart(Long userId) {
		this.userId = userId;
	}
}
