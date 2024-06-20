package com.nhnacademy.bookstoreback.cart.domain.entity;

import com.nhnacademy.bookstoreback.user.domain.entity.User;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JoinColumn(name = "cart_id", nullable = false)
	private Long cartId;

	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;

	public Cart(User user) {
		this.user = user;
	}
}
