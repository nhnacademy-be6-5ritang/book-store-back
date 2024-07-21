package com.nhnacademy.bookstoreback.author.domain.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Constructor;

import org.junit.jupiter.api.Test;

import com.nhnacademy.bookstoreback.author.domain.dto.respnse.AuthorDto;

class AuthorTest {

	@Test
	void testNoArgsConstructorIsProtected() {
		try {
			Constructor<Author> constructor = Author.class.getDeclaredConstructor();
			assertTrue(java.lang.reflect.Modifier.isProtected(constructor.getModifiers()),
				"Default constructor is not protected");
		} catch (NoSuchMethodException e) {
			fail("No default constructor found");
		}
		new Author();
	}

	@Test
	void testAuthorEntityCreation() {
		String authorName = "John Doe";
		Author author = Author.builder()
			.authorName(authorName)
			.build();

		assertNull(author.getAuthorId());
		assertNotNull(author);
		assertEquals(authorName, author.getAuthorName());
	}

	@Test
	void testToEntity() {
		String authorName = "Jane Doe";
		AuthorDto authorDto = new AuthorDto(1L, authorName);

		Author author = Author.toEntity(authorDto);

		assertNotNull(author);
		assertEquals(authorName, author.getAuthorName());
	}

	@Test
	void testUpdateAuthorName() {
		String initialAuthorName = "John Smith";
		String updatedAuthorName = "John Doe";

		Author author = Author.builder()
			.authorName(initialAuthorName)
			.build();

		author.updateAuthorName(updatedAuthorName);

		assertEquals(updatedAuthorName, author.getAuthorName());
	}
}
