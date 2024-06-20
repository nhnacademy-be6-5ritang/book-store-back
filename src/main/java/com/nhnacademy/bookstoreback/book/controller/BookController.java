package com.nhnacademy.bookstoreback.book.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nhnacademy.bookstoreback.book.service.BookService;

@RestController
@RequestMapping("/books")
public class BookController {

	private final BookService bookService;

	@Autowired
	public BookController(BookService bookService) {
		this.bookService = bookService;
	}

	@GetMapping("/fetch")
	public String fetchAndSaveBooks() {
		try {
			bookService.fetchAndSaveBook(
				"http://www.aladin.co.kr/ttb/api/ItemLookUp.aspx?ttbkey=ttb2897robo0933001&itemIdType=ISBN&ItemId=9788930041683&output=js&Version=20131101&OptResult=ebookList,usedList,reviewList");
			return "Books fetched and saved successfully";
		} catch (Exception e) {
			return "Error while fetching and saving books: " + e.getMessage();
		}
	}
}
