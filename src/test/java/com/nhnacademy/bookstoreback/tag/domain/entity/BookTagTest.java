package com.nhnacademy.bookstoreback.tag.domain.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Constructor;

import org.junit.jupiter.api.Test;

import com.nhnacademy.bookstoreback.book.domain.entity.Book;

class BookTagTest {

	@Test
	void testNoArgsConstructorIsProtected() {
		try {
			Constructor<BookTag> constructor = BookTag.class.getDeclaredConstructor();
			assertTrue(java.lang.reflect.Modifier.isProtected(constructor.getModifiers()),
				"Default constructor is not protected");
		} catch (NoSuchMethodException e) {
			fail("No default constructor found");
		}
		new BookTag();
	}

	@Test
	void testBookTagConstructor() {
		Book book = new Book();  // Book 객체는 테스트를 위해 간단하게 생성합니다.
		Tag tag = new Tag("exampleTag");

		BookTag bookTag = new BookTag(book, tag);

		assertNull(bookTag.getBookTagId());
		assertEquals(book, bookTag.getBook());
		assertEquals(tag, bookTag.getTag());
	}
}
