package com.nhnacademy.bookstoreback.search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.logging.Logger;

import com.nhnacademy.bookstoreback.search.dto.reponse.BookSearchResponse;
import com.nhnacademy.bookstoreback.search.service.SearchService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search")
public class SearchController {

	private static final Logger logger = Logger.getLogger(SearchController.class.getName());

	private SearchService searchService;

	@GetMapping("/books")
	public Page<BookSearchResponse> searchBooks(
		@RequestParam String query,
		@PageableDefault(page = 1, size = 20) Pageable pageable,
		@RequestParam(required = false, defaultValue = "bookTitle") String sortBy,
		@RequestParam(required = false, defaultValue = "asc") String sortOrder
	) throws IOException {
		logger.info("Received request to search books with query: " + query);
		return searchService.searchBooks(query, pageable, sortBy, sortOrder);
	}


	@GetMapping("/authors")
	public Page<BookSearchResponse> searchAuthors(@RequestParam String query, @PageableDefault(page = 1, size = 20) Pageable pageable) throws IOException {
		logger.info("Received request to search authors with query: " + query);
		return searchService.searchAuthors(query, pageable);
	}

	@GetMapping("/publisher")
	public Page<BookSearchResponse> searchPublishers(@RequestParam String query, @PageableDefault(page = 1, size = 20) Pageable pageable) throws IOException {
		logger.info("Received request to search publishers with query: " + query);
		return searchService.searchPublishers(query, pageable);
	}

	@GetMapping("/tag")
	public Page<BookSearchResponse> searchBooksByTag(@RequestParam String query, @PageableDefault(page = 1, size = 20) Pageable pageable) throws IOException {
		logger.info("Received request to search books by tag with query: " + query);
		return searchService.searchBooksByTag(query, pageable);
	}
}