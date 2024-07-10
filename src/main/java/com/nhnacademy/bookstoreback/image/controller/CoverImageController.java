package com.nhnacademy.bookstoreback.image.controller;

import com.nhnacademy.bookstoreback.book.domain.entity.Book;
import com.nhnacademy.bookstoreback.image.service.CloudImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CoverImageController {

	@Autowired
	private CloudImageService cloudImageService;

	@GetMapping("/downloadImages")
	public String downloadImages() {
		cloudImageService.downloadCoverImagesForAllBooks();
		return "Images downloaded successfully!";
	}

	@GetMapping("/downloadImage")
	public String downloadImage(Book book) {
		cloudImageService.downloadCoverImageForBook(book);
		return "Images downloaded successfully!";
	}

	@PostMapping("/uploadImages")
	public String uploadImages() {
		cloudImageService.uploadAllImages();
		return "Images uploaded successfully!";
	}
}
