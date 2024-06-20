package com.nhnacademy.bookstoreback.bookcart.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nhnacademy.bookstoreback.bookcart.domain.entity.BookCart;

public interface BookCartRepository extends JpaRepository<BookCart, Long> {
	List<BookCart> findAllByCartCartId(Long cartId);
}
