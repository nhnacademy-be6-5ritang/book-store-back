package com.nhnacademy.bookstoreback.bookstatus.domain.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Constructor;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.nhnacademy.bookstoreback.bookstatus.domain.dto.respnse.BookStatusDto;

class BookStatusTest {

	@Test
	void testNoArgsConstructorIsProtected() {
		try {
			Constructor<BookStatus> constructor = BookStatus.class.getDeclaredConstructor();
			assertTrue(java.lang.reflect.Modifier.isProtected(constructor.getModifiers()),
				"Default constructor is not protected");
		} catch (NoSuchMethodException e) {
			fail("No default constructor found");
		}
		new BookStatus();
	}

	@Test
	void testBookStatusCreationWithBuilder() {
		String statusName = "ON_SALE";

		BookStatus bookStatus = BookStatus.builder()
			.bookStatusName(statusName)
			.build();

		assertNull(bookStatus.getBookStatusId());
		assertNotNull(bookStatus);
		assertEquals(statusName, bookStatus.getBookStatusName());
	}

	@Test
	void testBookStatusCreationWithStaticMethod() {
		String statusName = "SOLD_OUT";

		BookStatusDto dto = Mockito.mock(BookStatusDto.class);
		Mockito.when(dto.bookStatusName()).thenReturn(statusName);

		BookStatus bookStatus = BookStatus.toEntity(dto);

		assertNotNull(bookStatus);
		assertEquals(statusName, bookStatus.getBookStatusName());
	}

	@Test
	void testUpdateBookStatusName() {
		String initialName = "DELETED";
		String updatedName = "UNKNOWN";
		BookStatus bookStatus = BookStatus.builder()
			.bookStatusName(initialName)
			.build();

		bookStatus.updateBookStatusName(updatedName);

		assertEquals(updatedName, bookStatus.getBookStatusName());
	}
}
