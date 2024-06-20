package com.nhnacademy.bookstoreback.book.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nhnacademy.bookstoreback.book.domain.entity.Book;
import com.nhnacademy.bookstoreback.book.service.BookService;

@RestController
@RequestMapping("/books")
public class BookController {

	private final BookService bookService;

	@Autowired
	public BookController(BookService bookService) {
		this.bookService = bookService;
	}

	@GetMapping
	public List<Book> getAllBooks() {
		return bookService.getAllBooks();
	}

	@GetMapping("/{bookId}")
	public Book getBookById(@PathVariable Long bookId) {
		return bookService.getBookById(bookId);
	}

	@PostMapping
	public Book createBook(@RequestBody Book book) {
		return bookService.createBook(book);
	}

	@DeleteMapping("/{bookId}")
	public void deleteBook(@PathVariable Long bookId) {
		bookService.deleteBook(bookId);
	}
}