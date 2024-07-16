package com.nhnacademy.bookstoreback.bookcart.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author 이경헌
 * Redis 에 저장되는 도서, 수량 정보 엔티티입니다.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Book {
	private Long bookId;
	private Integer bookQuantity;

	public void updateBookQuantity(Integer bookQuantity) {
		this.bookQuantity = bookQuantity;
	}
}
