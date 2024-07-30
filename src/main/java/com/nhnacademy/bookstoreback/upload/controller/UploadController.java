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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Upload", description = "업로드 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/uploads")
public class UploadController {
	private final UploadServiceImpl uploadService;

	@Operation(
		summary = "파일 업로드",
		description = "파일과 폴더명을 받아 파일을 업로드합니다.",
		responses = {
			@ApiResponse(responseCode = "201", description = "파일이 성공적으로 업로드되었습니다."),
			@ApiResponse(responseCode = "500", description = "업로드에 실패했습니다. 업로드 서비스에 문제가 있을 수 있습니다.")
		}
	)
	@AuthorizeRole({"MEMBER", "HEAD_ADMIN"})
	@PostMapping
	public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file,
		@RequestParam String folderName) {
		return ResponseEntity.status(HttpStatus.CREATED).body(uploadService.upload(file, folderName));
	}
}
