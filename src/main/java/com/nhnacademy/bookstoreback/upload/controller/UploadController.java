package com.nhnacademy.bookstoreback.upload.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nhnacademy.bookstoreback.upload.service.impl.UploadServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/uploads")
public class UploadController {
	private final UploadServiceImpl uploadService;

	@PostMapping
	public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
		return ResponseEntity.status(HttpStatus.CREATED).body(uploadService.upload(file));
	}
}
