package com.nhnacademy.bookstoreback.image.controller;

import com.nhnacademy.bookstoreback.book.domain.entity.Book;
import com.nhnacademy.bookstoreback.image.service.BookImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
public class BookImageTestController {

	private final BookImageService bookImageService;

	@Autowired
	public BookImageTestController(BookImageService bookImageService) {
		this.bookImageService = bookImageService;
	}

	@PostMapping("/mapAllImages")
	public void mapAllBooksToImages() {
		bookImageService.mapAllBooksToImages();
	}

	@PostMapping("/mapAllImage")
	public void mapImageForSingleBook(Book book) {
		bookImageService.mapImageForSingleBook(book);
	}
}
