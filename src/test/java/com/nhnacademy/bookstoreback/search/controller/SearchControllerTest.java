package com.nhnacademy.bookstoreback.search.controller;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookstoreback.search.dto.reponse.BookSearchResponse;
import com.nhnacademy.bookstoreback.search.service.SearchService;

@ExtendWith(MockitoExtension.class)
class SearchControllerTest {

	@InjectMocks
	private SearchController searchController;

	@Mock
	private SearchService searchService;

	private MockMvc mockMvc;
	private ObjectMapper objectMapper;

	private BookSearchResponse bookSearchResponse;

	@BeforeEach
	void setUp() {
		objectMapper = new ObjectMapper();
		mockMvc = MockMvcBuilders.standaloneSetup(new SearchController(searchService))
			.setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver()).build();
		bookSearchResponse = BookSearchResponse.builder()
			.bookId(1L)
			.authorName("Sample Author")
			.publisherName("Sample Publisher")
			.bookStatusName("Available")
			.bookTitle("Sample Book")
			.bookDescription("Sample Description")
			.bookQuantity(10)
			.bookPublishDate(new Date())
			.bookIsbn("123-456-789")
			.bookPrice(BigDecimal.valueOf(19.99))
			.bookSalePrice(BigDecimal.valueOf(17.99))
			.bookSalePercent(BigDecimal.valueOf(10))
			.bookImageUrl("http://example.com/image.jpg")
			.build();
	}

	@Test
	void testSearchBooks() throws Exception {
		Pageable pageable = PageRequest.of(0, 20);
		List<BookSearchResponse> bookSearchResponses = Collections.singletonList(bookSearchResponse);
		Page<BookSearchResponse> page = new PageImpl<>(bookSearchResponses, pageable, bookSearchResponses.size());

		given(searchService.searchBooks(anyString(), eq(pageable))).willReturn(page);

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/search/books")
				.param("query", "sample query")
				.param("page", "0")
				.param("size", "20")
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn();

		String expectedJson = objectMapper.writeValueAsString(page);
		MockMvcResultMatchers.content().json(expectedJson).match(result);
	}

	@Test
	void testSearchAuthors() throws Exception {
		Pageable pageable = PageRequest.of(0, 20);
		List<BookSearchResponse> bookSearchResponses = Collections.singletonList(bookSearchResponse);
		Page<BookSearchResponse> page = new PageImpl<>(bookSearchResponses, pageable, bookSearchResponses.size());

		given(searchService.searchAuthors(anyString(), eq(pageable))).willReturn(page);

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/search/authors")
				.param("query", "sample query")
				.param("page", "0")
				.param("size", "20")
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn();

		String expectedJson = objectMapper.writeValueAsString(page);
		MockMvcResultMatchers.content().json(expectedJson).match(result);
	}

	@Test
	void testSearchPublishers() throws Exception {
		Pageable pageable = PageRequest.of(0, 20);
		List<BookSearchResponse> bookSearchResponses = Collections.singletonList(bookSearchResponse);
		Page<BookSearchResponse> page = new PageImpl<>(bookSearchResponses, pageable, bookSearchResponses.size());

		given(searchService.searchPublishers(anyString(), eq(pageable))).willReturn(page);

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/search/publisher")
				.param("query", "sample query")
				.param("page", "0")
				.param("size", "20")
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn();

		String expectedJson = objectMapper.writeValueAsString(page);
		MockMvcResultMatchers.content().json(expectedJson).match(result);
	}

	@Test
	void testSearchBooksByTag() throws Exception {
		Pageable pageable = PageRequest.of(0, 20);
		List<BookSearchResponse> bookSearchResponses = Collections.singletonList(bookSearchResponse);
		Page<BookSearchResponse> page = new PageImpl<>(bookSearchResponses, pageable, bookSearchResponses.size());

		given(searchService.searchBooksByTag(anyString(), eq(pageable))).willReturn(page);

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/search/tag")
				.param("query", "sample query")
				.param("page", "0")
				.param("size", "20")
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn();

		String expectedJson = objectMapper.writeValueAsString(page);
		MockMvcResultMatchers.content().json(expectedJson).match(result);
	}
}