package com.nhnacademy.bookstoreback.category.domain.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Constructor;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.nhnacademy.bookstoreback.book.domain.entity.Book;

class BookCategoryTest {

	@Test
	void testNoArgsConstructorIsProtected() {
		try {
			Constructor<BookCategory> constructor = BookCategory.class.getDeclaredConstructor();
			assertTrue(java.lang.reflect.Modifier.isProtected(constructor.getModifiers()),
				"Default constructor is not protected");
		} catch (NoSuchMethodException e) {
			fail("No default constructor found");
		}
	}

	@Test
	void testBookCategoryCreation() {
		Book book = Mockito.mock(Book.class);
		Category category = Mockito.mock(Category.class);

		BookCategory bookCategory = new BookCategory(book, category);

		assertNull(bookCategory.getBookCategoryId());
		assertNotNull(bookCategory);
		assertEquals(book, bookCategory.getBook());
		assertEquals(category, bookCategory.getCategory());
	}
}
