package com.nhnacademy.bookstoreback.publisher.controller;

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

import com.nhnacademy.bookstoreback.publisher.domain.dto.respnse.PublisherDto;
import com.nhnacademy.bookstoreback.publisher.service.PublisherService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/publishers")
public class PublisherController {
	private final PublisherService publisherService;

	@GetMapping
	public ResponseEntity<List<PublisherDto>> getPublishers() {
		return ResponseEntity.status(HttpStatus.OK).body(publisherService.getPublishers());
	}

	@GetMapping("/{publisherId}")
	public ResponseEntity<PublisherDto> getPublisher(@PathVariable Long publisherId) {
		return ResponseEntity.status(HttpStatus.OK).body(publisherService.getPublisher(publisherId));
	}

	@PostMapping
	public ResponseEntity<PublisherDto> createPublisher(@RequestBody PublisherDto request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(publisherService.createPublisher(request));
	}

	@PutMapping("/{publisherId}")
	public ResponseEntity<PublisherDto> updatePublisher(@PathVariable Long publisherId,
		@RequestBody PublisherDto request) {
		return ResponseEntity.status(HttpStatus.OK).body(publisherService.updatePublisher(publisherId, request));
	}

	@DeleteMapping("/{publisherId}")
	public ResponseEntity<Void> deletePublisher(@PathVariable Long publisherId) {
		publisherService.deletePublisher(publisherId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
