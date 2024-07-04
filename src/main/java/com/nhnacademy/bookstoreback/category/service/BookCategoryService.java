package com.nhnacademy.bookstoreback.category.service;

import com.nhnacademy.bookstoreback.category.domain.entity.BookCategory;
import com.nhnacademy.bookstoreback.book.domain.entity.Book;
import com.nhnacademy.bookstoreback.category.domain.entity.Category;

public interface BookCategoryService {
	/**
	 * 책과 카테고리를 연결하는 BookCategory를 저장합니다.
	 *
	 * @param book     책
	 * @param category 카테고리
	 * @return 저장된 BookCategory
	 */
	BookCategory saveBookCategory(Book book, Category category);
}
