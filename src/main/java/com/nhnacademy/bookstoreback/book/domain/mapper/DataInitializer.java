package com.nhnacademy.bookstoreback.book.domain.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.nhnacademy.bookstoreback.book.service.BookService;

/**
 * @author 김기욱
 * @version 1.0
 */
@Component
public class DataInitializer implements CommandLineRunner {

	private final BookService bookService;

	@Autowired
	public DataInitializer(BookService bookService) {
		this.bookService = bookService;
	}

	@Override
	public void run(String... args) throws Exception {
		String apiUrl = "http://www.aladin.co.kr/ttb/api/ItemList.aspx?ttbkey=ttb2897robo0933001&QueryType=ItemNewSpecial&MaxResults=50&start=1&SearchTarget=Book&output=js&Version=20131101";
		bookService.fetchAndSaveBook(apiUrl);
	}
}
