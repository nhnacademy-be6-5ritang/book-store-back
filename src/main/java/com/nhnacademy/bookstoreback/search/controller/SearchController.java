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

	// 추가로 다른 엔티티에 대한 검색 엔드포인트를 구현할 수 있습니다.
}