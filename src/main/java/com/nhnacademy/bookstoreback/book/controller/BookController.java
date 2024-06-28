package com.nhnacademy.bookstoreback.book.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nhnacademy.bookstoreback.book.service.BookService;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetBookResponse;

/**
 * @author 김기욱
 * @version 1.0
 * @
 */
@RestController
@RequestMapping("/books")
public class BookController {

	private final BookService bookService;

	/**
	 * @param bookService
	 */
	@Autowired
	public BookController(BookService bookService) {
		this.bookService = bookService;
	}

	/**
	 * @return 도서저장결과
	 */
	@GetMapping("/fetch")
	public String fetchAndSaveBooks() {
		try {
			bookService.fetchAndSaveBook(
				"http://www.aladin.co.kr/ttb/api/ItemList.aspx?ttbkey=ttb2897robo0933001&QueryType=ItemNewAll&MaxResults=10&start=1&SearchTarget=Book&output=js&Version=20131101");
			return "Books fetched and saved successfully";
		} catch (Exception e) {
			return "Error while fetching and saving books: " + e.getMessage();
		}
	}

	@GetMapping("/packaging/{book_id}")
	public void packaging(@PathVariable("book_id") Long bookId) {
		bookService.updateBookById(bookId);
	}

	@GetMapping("/books/{book_id}")
	public ResponseEntity<GetBookResponse> getBook(@PathVariable("book_id") Long bookId) {
		return ResponseEntity.status(HttpStatus.OK).body(bookService.findBookById(bookId));
	}
}
