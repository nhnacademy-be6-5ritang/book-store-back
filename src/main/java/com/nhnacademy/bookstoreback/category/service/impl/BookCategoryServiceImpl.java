package com.nhnacademy.bookstoreback.category.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nhnacademy.bookstoreback.category.domain.entity.BookCategory;
import com.nhnacademy.bookstoreback.book.domain.entity.Book;
import com.nhnacademy.bookstoreback.category.domain.entity.Category;
import com.nhnacademy.bookstoreback.category.repository.BookCategoryRepository;
import com.nhnacademy.bookstoreback.category.service.BookCategoryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class BookCategoryServiceImpl implements BookCategoryService {
	private final BookCategoryRepository bookCategoryRepository;

	@Override
	public BookCategory saveBookCategory(Book book, Category category) {
		BookCategory bookCategory = new BookCategory(book, category);
		return bookCategoryRepository.save(bookCategory);
	}
}
