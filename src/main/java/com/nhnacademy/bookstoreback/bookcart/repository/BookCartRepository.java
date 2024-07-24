package com.nhnacademy.bookstoreback.bookcart.repository;

import org.springframework.data.repository.CrudRepository;

import com.nhnacademy.bookstoreback.bookcart.domain.entity.BookCart;

/**
 * @author
 * 도서 장바구니 엔티티를 저장하고 관리하는 레포지토리입니다.
 */
public interface BookCartRepository extends CrudRepository<BookCart, String> {
	void deleteAllByCartId(String cartId);
}
