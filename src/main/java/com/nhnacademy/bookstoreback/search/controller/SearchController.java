package com.nhnacademy.bookstoreback.search.controller;

import org.elasticsearch.action.search.SearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.logging.Logger;

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
	public SearchResponse searchBooks(@RequestParam String query) throws IOException {
		logger.info("Received request to search books with query: " + query);
		return searchService.searchBooks(query);
	}

	@GetMapping("/search/authors")
	public SearchResponse searchAuthors(@RequestParam String query) throws IOException {
		logger.info("Received request to search authors with query: " + query);
		return searchService.searchAuthors(query);
	}

	@GetMapping("/search/category")
	public SearchResponse searchBooksByCategory(@RequestParam String query) throws IOException {
		logger.info("Received request to search books by category with query: " + query);
		return searchService.searchBooksByCategory(query);
	}

	@GetMapping("/search/publisher")
	public SearchResponse searchPublishers(@RequestParam String query) throws IOException {
		logger.info("Received request to search Publishers with query: " + query);
		return searchService.searchPublishers(query);
	}

	@GetMapping("/search/tag")
	public SearchResponse searchBooksByTag(@RequestParam String query) throws IOException {
		logger.info("Received request to search books by tag with query: " + query);
		return searchService.searchBooksByTag(query);
	}
}