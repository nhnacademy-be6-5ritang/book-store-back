package com.nhnacademy.bookstoreback.upload.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nhnacademy.bookstoreback.auth.annotation.AuthorizeRole;
import com.nhnacademy.bookstoreback.upload.service.impl.UploadServiceImpl;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Upload", description = "업로드 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/uploads")
public class UploadController {
	private final UploadServiceImpl uploadService;

	@AuthorizeRole({"MEMBER", "HEAD_ADMIN"})
	@PostMapping
	public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file,
		@RequestParam String folderName) {
		return ResponseEntity.status(HttpStatus.CREATED).body(uploadService.upload(file, folderName));
	}
}
