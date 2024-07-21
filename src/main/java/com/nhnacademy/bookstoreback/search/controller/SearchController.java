package com.nhnacademy.bookstoreback.search.controller;

import org.elasticsearch.action.search.SearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.logging.Logger;

import com.nhnacademy.bookstoreback.book.domain.dto.response.GetBookDetailResponse;
import com.nhnacademy.bookstoreback.search.service.SearchService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class SearchController {

	private static final Logger logger = Logger.getLogger(SearchController.class.getName());

	@Autowired
	private SearchService searchService;

	@GetMapping("/search/books")
	public Page<GetBookDetailResponse> searchBooks(@RequestParam String query, Pageable pageable) throws IOException {
		logger.info("Received request to search books with query: " + query);
		return searchService.searchBooks(query, pageable);
	}

	@GetMapping("/search/authors")
	public Page<GetBookDetailResponse> searchAuthors(@RequestParam String query, Pageable pageable) throws IOException {
		logger.info("Received request to search authors with query: " + query);
		return searchService.searchAuthors(query, pageable);
	}

	@GetMapping("/search/publisher")
	public Page<GetBookDetailResponse> searchPublishers(@RequestParam String query, Pageable pageable) throws IOException {
		logger.info("Received request to search publishers with query: " + query);
		return searchService.searchPublishers(query, pageable);
	}

	@GetMapping("/search/tag")
	public Page<GetBookDetailResponse> searchBooksByTag(@RequestParam String query, Pageable pageable) throws IOException {
		logger.info("Received request to search books by tag with query: " + query);
		return searchService.searchBooksByTag(query, pageable);
	}
}