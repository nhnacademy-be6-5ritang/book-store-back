package com.nhnacademy.bookstoreback.cart.domain.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "carts")
public class Cart {
	@Id
	@Column(name = "cart_id", nullable = false)
	private String cartId = UUID.randomUUID().toString();

	@Column(name = "user_id", nullable = false)
	private Long userId;

	public Cart(Long userId) {
		this.userId = userId;
	}
}
