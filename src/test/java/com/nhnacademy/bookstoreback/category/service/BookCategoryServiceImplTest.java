package com.nhnacademy.bookstoreback.category.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.nhnacademy.bookstoreback.book.domain.entity.Book;
import com.nhnacademy.bookstoreback.category.domain.entity.BookCategory;
import com.nhnacademy.bookstoreback.category.domain.entity.Category;
import com.nhnacademy.bookstoreback.category.repository.BookCategoryRepository;
import com.nhnacademy.bookstoreback.category.service.impl.BookCategoryServiceImpl;

class BookCategoryServiceImplTest {

	@Mock
	private BookCategoryRepository bookCategoryRepository;

	@InjectMocks
	private BookCategoryServiceImpl bookCategoryService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testSaveBookCategory() {
		Book book = new Book();
		Category category = new Category("CategoryName", null);
		BookCategory bookCategory = new BookCategory(book, category);

		when(bookCategoryRepository.save(any(BookCategory.class))).thenReturn(bookCategory);

		BookCategory savedBookCategory = bookCategoryService.saveBookCategory(book, category);

		assertEquals(book, savedBookCategory.getBook());
		assertEquals(category, savedBookCategory.getCategory());
		verify(bookCategoryRepository, times(1)).save(any(BookCategory.class));
	}
}
