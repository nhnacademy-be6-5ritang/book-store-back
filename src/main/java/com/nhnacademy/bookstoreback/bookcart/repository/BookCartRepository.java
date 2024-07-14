package com.nhnacademy.bookstoreback.bookcart.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.nhnacademy.bookstoreback.bookcart.domain.entity.BookCart;

@Repository
public interface BookCartRepository extends CrudRepository<BookCart, String> {
	<S extends BookCart> S saveWithTtl(S entity);
}
