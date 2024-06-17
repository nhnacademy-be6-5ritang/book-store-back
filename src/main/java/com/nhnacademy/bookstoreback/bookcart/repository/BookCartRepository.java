package com.nhnacademy.bookstoreback.bookcart.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nhnacademy.bookstoreback.bookcart.controller.BookCartController;

public interface BookCartRepository extends JpaRepository<BookCartController, Long> {
}
