package com.nhnacademy.bookstoreback.book.domain.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.jupiter.api.Test;

import com.nhnacademy.bookstoreback.author.domain.entity.Author;
import com.nhnacademy.bookstoreback.bookstatus.domain.entity.BookStatus;
import com.nhnacademy.bookstoreback.publisher.domain.entity.Publisher;

class BookTest {

	@Test
	void testBookCreationUsingBuilder() {
		Author author = new Author(1L, "Author");
		Publisher publisher = new Publisher("Publisher");
		BookStatus bookStatus = new BookStatus(1L, "Status");
		String bookTitle = "Book Title";
		String bookDescription = "Book Description";
		int bookQuantity = 10;
		Date bookPublishDate = new Date();
		String bookIsbn = "978-3-16-148410-0";
		BigDecimal bookPrice = new BigDecimal("29.99");
		BigDecimal bookSalePercent = new BigDecimal("10");
		BigDecimal bookSalePrice = new BigDecimal("26.99");

		Book book = Book.builder()
			.author(author)
			.publisher(publisher)
			.bookStatus(bookStatus)
			.bookTitle(bookTitle)
			.bookDescription(bookDescription)
			.bookQuantity(bookQuantity)
			.bookPublishDate(bookPublishDate)
			.bookIsbn(bookIsbn)
			.bookPrice(bookPrice)
			.bookSalePercent(bookSalePercent)
			.bookSalePrice(bookSalePrice)
			.build();

		assertNotNull(book);
		assertEquals(author, book.getAuthor());
		assertEquals(publisher, book.getPublisher());
		assertEquals(bookStatus, book.getBookStatus());
		assertEquals(bookTitle, book.getBookTitle());
		assertEquals(bookDescription, book.getBookDescription());
		assertEquals(bookQuantity, book.getBookQuantity());
		assertEquals(bookPublishDate, book.getBookPublishDate());
		assertEquals(bookIsbn, book.getBookIsbn());
		assertEquals(bookPrice, book.getBookPrice());
		assertEquals(bookSalePercent, book.getBookSalePercent());
		assertEquals(bookSalePrice, book.getBookSalePrice());
	}

	@Test
	void testToEntity() {
		Author author = new Author(1L, "Author");
		Publisher publisher = new Publisher("Publisher");
		BookStatus bookStatus = new BookStatus(1L, "Status");
		String bookTitle = "Book Title";
		String bookDescription = "Book Description";
		int bookQuantity = 10;
		Date bookPublishDate = new Date();
		String bookIsbn = "978-3-16-148410-0";
		BigDecimal bookPrice = new BigDecimal("29.99");
		BigDecimal bookSalePercent = new BigDecimal("10");
		BigDecimal bookSalePrice = new BigDecimal("26.99");

		Book book = Book.builder()
			.author(author)
			.publisher(publisher)
			.bookStatus(bookStatus)
			.bookTitle(bookTitle)
			.bookDescription(bookDescription)
			.bookQuantity(bookQuantity)
			.bookPublishDate(bookPublishDate)
			.bookIsbn(bookIsbn)
			.bookPrice(bookPrice)
			.bookSalePercent(bookSalePercent)
			.bookSalePrice(bookSalePrice)
			.build();

		assertNotNull(book);
		assertEquals(author, book.getAuthor());
		assertEquals(publisher, book.getPublisher());
		assertEquals(bookStatus, book.getBookStatus());
		assertEquals(bookTitle, book.getBookTitle());
		assertEquals(bookDescription, book.getBookDescription());
		assertEquals(bookQuantity, book.getBookQuantity());
		assertEquals(bookPublishDate, book.getBookPublishDate());
		assertEquals(bookIsbn, book.getBookIsbn());
		assertEquals(bookPrice, book.getBookPrice());
		assertEquals(bookSalePercent, book.getBookSalePercent());
		assertEquals(bookSalePrice, book.getBookSalePrice());
	}

	@Test
	void testUpdateBook() {
		Author newAuthor = new Author(2L, "New Author");
		Publisher newPublisher = new Publisher("New Publisher");
		BookStatus newBookStatus = new BookStatus(2L, "New Status");

		Book book = new Book(
			new Author(1L, "Old Author"),
			new Publisher("Old Publisher"),
			new BookStatus(1L, "Old Status"),
			"Old Title",
			"Old Description",
			100,
			new Date(),
			"978-1-23-456789-0",
			new BigDecimal("19.99"),
			new BigDecimal("5"),
			new BigDecimal("18.99")
		);

		book.updateBook(
			newAuthor,
			newPublisher,
			newBookStatus,
			"New Title",
			"New Description",
			200,
			new Date(),
			"978-1-23-456789-1",
			new BigDecimal("25.99"),
			new BigDecimal("10"),
			new BigDecimal("23.39")
		);

		assertNull(book.getBookId());
		assertEquals(newAuthor, book.getAuthor());
		assertEquals(newPublisher, book.getPublisher());
		assertEquals(newBookStatus, book.getBookStatus());
		assertEquals("New Title", book.getBookTitle());
		assertEquals("New Description", book.getBookDescription());
		assertEquals(200, book.getBookQuantity());
		assertEquals("978-1-23-456789-1", book.getBookIsbn());
		assertEquals(new BigDecimal("25.99"), book.getBookPrice());
		assertEquals(new BigDecimal("10"), book.getBookSalePercent());
		assertEquals(new BigDecimal("23.39"), book.getBookSalePrice());
	}

	@Test
	void testUpdateQuantity() {
		Book book = Book.builder()
			.bookQuantity(100)
			.build();

		book.updateQuantity(10);

		assertEquals(90, book.getBookQuantity());
	}
}
