package com.nhnacademy.bookstoreback.bookcart.repository;

import org.springframework.data.repository.CrudRepository;

import com.nhnacademy.bookstoreback.bookcart.domain.entity.BookCart;

public interface BookCartRepository extends CrudRepository<BookCart, String> {
}
