package com.nhnacademy.bookstoreback.image.controller;

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
		cloudImageService.downloadCoverImages();
		return "Images downloaded successfully!";
	}

	@PostMapping("/uploadImages")
	public String uploadImages() {
		cloudImageService.uploadAllImages();
		return "Images uploaded successfully!";
	}
}
