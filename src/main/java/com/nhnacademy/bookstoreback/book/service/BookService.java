package com.nhnacademy.bookstoreback.book.service;

import com.nhnacademy.bookstoreback.book.domain.entity.Book;
import com.nhnacademy.bookstoreback.book.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

	private final BookRepository bookRepository;

	@Autowired
	public BookService(BookRepository bookRepository) {
		this.bookRepository = bookRepository;
	}

	public List<Book> getAllBooks() {
		return bookRepository.findAll();
	}

	public Book getBookById(Long bookId) {
		return bookRepository.findById(bookId).orElse(null);
	}

	public Book createBook(Book book) {
		return bookRepository.save(book);
	}

	public void deleteBook(Long bookId) {
		bookRepository.deleteById(bookId);
	}
}