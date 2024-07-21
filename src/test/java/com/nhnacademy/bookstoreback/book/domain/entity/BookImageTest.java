package com.nhnacademy.bookstoreback.book.domain.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.nhnacademy.bookstoreback.image.domain.entity.Image;

class BookImageTest {

	@Test
	void testBookImageCreation() {
		Book book = new Book();
		book.setBookId(1L);

		Image image = new Image();
		image.setImageId(1L);

		BookImage bookImage = new BookImage();
		bookImage.setBook(book);
		bookImage.setImage(image);

		assertNull(bookImage.getBookImageId());
		assertNotNull(bookImage);
		assertEquals(book, bookImage.getBook());
		assertEquals(image, bookImage.getImage());
	}

	@Test
	void testBookImageFieldSettersAndGetters() {
		Book book = new Book();
		book.setBookId(2L);

		Image image = new Image();
		image.setImageId(2L);

		BookImage bookImage = new BookImage();
		bookImage.setBook(book);
		bookImage.setImage(image);

		assertEquals(2L, book.getBookId());
		assertEquals(2L, image.getImageId());
		assertEquals(book, bookImage.getBook());
		assertEquals(image, bookImage.getImage());
	}
}
