package com.nhnacademy.bookstoreback.bookcart.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nhnacademy.bookstoreback.bookcart.domain.entity.BookCart;

public interface BookCartRepository extends JpaRepository<BookCart, Long> {
}
