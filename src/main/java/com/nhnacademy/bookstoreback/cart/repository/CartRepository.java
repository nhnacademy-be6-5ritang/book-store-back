package com.nhnacademy.bookstoreback.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nhnacademy.bookstoreback.cart.domain.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {
	boolean existsByUserId(Long userId);
}
