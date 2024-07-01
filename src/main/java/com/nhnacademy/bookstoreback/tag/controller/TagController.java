package com.nhnacademy.bookstoreback.tag.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.nhnacademy.bookstoreback.tag.domain.dto.respnse.TagDto;
import com.nhnacademy.bookstoreback.tag.service.TagService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TagController {
	private final TagService tagService;

	@GetMapping("/tags")
	public ResponseEntity<List<TagDto>> getTags() {
		return ResponseEntity.status(HttpStatus.OK).body(tagService.getTags());
	}

	@GetMapping("/books/{bookId}/tags")
	public ResponseEntity<List<TagDto>> getTagsByTagId(@PathVariable Long bookId) {
		return ResponseEntity.status(HttpStatus.OK).body(tagService.getTagsByTagId(bookId));
	}

	@GetMapping("/tags/{tagId}")
	public ResponseEntity<TagDto> getTag(@PathVariable Long tagId) {
		return ResponseEntity.status(HttpStatus.OK).body(tagService.getTag(tagId));
	}

	@PostMapping("/tags")
	public ResponseEntity<TagDto> createTag(
		@RequestBody TagDto request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(tagService.createTag(request));
	}

	@PutMapping("/tags/{tagId}")
	public ResponseEntity<TagDto> updateTag(@PathVariable Long tagId,
		@RequestBody TagDto request) {
		return ResponseEntity.status(HttpStatus.OK).body(tagService.updateTag(tagId, request));
	}

	@DeleteMapping("/tags/{tagId}")
	public ResponseEntity<Void> deleteTag(@PathVariable Long tagId) {
		tagService.deleteTag(tagId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
