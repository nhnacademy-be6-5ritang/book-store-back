package com.nhnacademy.bookstoreback.author.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nhnacademy.bookstoreback.author.domain.dto.respnse.AuthorDto;
import com.nhnacademy.bookstoreback.author.service.AuthorService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/authors")
public class AuthorController {
	private final AuthorService authorService;

	@GetMapping
	public ResponseEntity<List<AuthorDto>> getAuthors() {
		return ResponseEntity.status(HttpStatus.OK).body(authorService.getAuthors());
	}

	@GetMapping("/{authorId}")
	public ResponseEntity<AuthorDto> getAuthor(@PathVariable Long authorId) {
		return ResponseEntity.status(HttpStatus.OK).body(authorService.getAuthor(authorId));
	}

	@PostMapping
	public ResponseEntity<AuthorDto> createAuthor(
		@RequestBody AuthorDto request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(authorService.createAuthor(request));
	}

	@PutMapping("/{authorId}")
	public ResponseEntity<AuthorDto> updateAuthor(@PathVariable Long authorId,
		@RequestBody AuthorDto request) {
		return ResponseEntity.status(HttpStatus.OK).body(authorService.updateAuthor(authorId, request));
	}

	@DeleteMapping("/{authorId}")
	public ResponseEntity<Void> deleteAuthor(@PathVariable Long authorId) {
		authorService.deleteAuthor(authorId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
