package com.nhnacademy.bookstoreback.book.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookstoreback.book.domain.dto.request.CreateBookRequest;
import com.nhnacademy.bookstoreback.book.domain.dto.request.UpdateBookRequest;
import com.nhnacademy.bookstoreback.book.domain.dto.response.GetBookDetailResponse;
import com.nhnacademy.bookstoreback.book.service.impl.BookServiceImpl;

@WebMvcTest(BookController.class)
class BookControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private BookServiceImpl bookService;

	@BeforeEach
	void setup() {
		mockMvc = MockMvcBuilders
			.standaloneSetup(new BookController(bookService))
			.setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
			.build();
	}

	@Test
	void fetchAndSaveBooks_ShouldReturnOk() throws Exception {
		mockMvc.perform(post("/api/books/fetch/book-lists")
				.param("count", "10")
				.header("Authorization", "Bearer token"))
			.andExpect(status().isOk());
	}

	@Test
	void saveBookByIsbn_ShouldReturnOk() throws Exception {
		mockMvc.perform(post("/api/books/fetch")
				.param("isbn", "978-3-16-148410-0")
				.header("Authorization", "Bearer token"))
			.andExpect(status().isOk());
	}

	@Test
	void getNewestBooks_ShouldReturnOk() throws Exception {
		when(bookService.getNewestBooks()).thenReturn(List.of());
		mockMvc.perform(get("/api/books")
				.header("Authorization", "Bearer token"))
			.andExpect(status().isOk());
	}

	@Test
	void getOrderedBooks_ShouldReturnOk() throws Exception {
		when(bookService.getOrderedBooks()).thenReturn(List.of());
		mockMvc.perform(get("/api/books/ordered")
				.header("Authorization", "Bearer token"))
			.andExpect(status().isOk());
	}

	@Test
	void getLikesBooks_ShouldReturnOk() throws Exception {
		when(bookService.getLikesBooks()).thenReturn(List.of());
		mockMvc.perform(get("/api/books/likes")
				.header("Authorization", "Bearer token"))
			.andExpect(status().isOk());
	}

	// @Test
	// void getNewestBooksWithPagination_ShouldReturnOk() throws Exception {
	// 	when(bookService.findAllBooks(any(Pageable.class))).thenReturn(Page.empty());
	// 	mockMvc.perform(get("/api/books/page")
	// 			.param("page", "1")
	// 			.param("size", "10")
	// 			.header("Authorization", "Bearer token"))
	// 		.andExpect(status().isOk());
	// }

	@Test
	void findBookByIsbn_ShouldReturnOk() throws Exception {
		when(bookService.findBookByIsbn(any(String.class))).thenReturn(mock(GetBookDetailResponse.class));
		mockMvc.perform(get("/api/books/details/{isbn}", "978-3-16-148410-0")
				.header("Authorization", "Bearer token"))
			.andExpect(status().isOk());
	}

	@Test
	void getBook_ShouldReturnOk() throws Exception {
		when(bookService.getBook(anyLong())).thenReturn(mock(GetBookDetailResponse.class));
		mockMvc.perform(get("/api/books/{bookId}", 1L)
				.header("Authorization", "Bearer token"))
			.andExpect(status().isOk());
	}

	@Test
	void createBook_ShouldReturnCreated() throws Exception {
		CreateBookRequest request = new CreateBookRequest(
			"978-3-16-148410-0",
			List.of(1L),
			List.of(1L),
			"Book Title",
			"Author Name",
			"Publisher Name",
			new Date(),
			"Available",
			"Book Description",
			10,
			new BigDecimal("29.99"),
			new BigDecimal("10"),
			new BigDecimal("26.99")
		);

		mockMvc.perform(post("/api/books")
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(request))
				.header("Authorization", "Bearer token"))
			.andExpect(status().isCreated());
	}

	@Test
	void updateBookByBookId_ShouldReturnOk() throws Exception {
		UpdateBookRequest request = new UpdateBookRequest(
			"978-3-16-148410-0",
			List.of(1L),
			List.of(1L),
			"Book Title",
			"Author Name",
			"Publisher Name",
			new Date(),
			"Available",
			"Book Description",
			10,
			new BigDecimal("29.99"),
			new BigDecimal("10"),
			new BigDecimal("26.99")
		);

		mockMvc.perform(put("/api/books/{bookId}", 1L)
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(request))
				.header("Authorization", "Bearer token"))
			.andExpect(status().isOk());
	}

	@Test
	void deleteBook_ShouldReturnOk() throws Exception {
		mockMvc.perform(delete("/api/books/{bookId}", 1L)
				.header("Authorization", "Bearer token"))
			.andExpect(status().isOk());
	}

	@Test
	void updateQuantity_ShouldReturnOk() throws Exception {
		mockMvc.perform(put("/api/books/{bookId}/{quantity}", 1L, 5)
				.header("Authorization", "Bearer token"))
			.andExpect(status().isOk());
	}

	@Test
	void searchBooks_ShouldReturnOk() throws Exception {
		when(bookService.searchBooks(any(String.class))).thenReturn(List.of());
		mockMvc.perform(get("/api/books/search/test")
				.param("key", "searchTerm")
				.header("Authorization", "Bearer token"))
			.andExpect(status().isOk());
	}

	// @Test
	// void findAllBooksByCategoryName_ShouldReturnOk() throws Exception {
	// 	when(bookService.findAllBooksByCategoryName(any(Pageable.class), any(String.class))).thenReturn(Page.empty());
	// 	mockMvc.perform(get("/api/books/page/category")
	// 			.param("page", "1")
	// 			.param("size", "20")
	// 			.param("categoryName", "CategoryName")
	// 			.header("Authorization", "Bearer token"))
	// 		.andExpect(status().isOk());
	// }

	@Test
	void findAllBooksByCategoryName_ShouldReturnOk() throws Exception {
		// Given
		Pageable pageable = PageRequest.of(1, 20);
		GetBookDetailResponse bookResponse = GetBookDetailResponse.builder()
			.bookId(1L)
			.authorName("Author Name")
			.publisherName("Publisher Name")
			.bookStatusName("Available")
			.bookTitle("Book Title")
			.bookDescription("Book Description")
			.bookQuantity(10)
			.bookPublishDate(new Date())
			.bookIsbn("978-3-16-148410-0")
			.bookPrice(new BigDecimal("29.99"))
			.bookSalePrice(new BigDecimal("24.99"))
			.bookSalePercent(new BigDecimal("10"))
			.bookImageUrl("http://example.com/image.jpg")
			.build();
		Page<GetBookDetailResponse> page = new PageImpl<>(Collections.singletonList(bookResponse), pageable, 1);

		when(bookService.findAllBooksByCategoryName(pageable, "CategoryName")).thenReturn(page);
		
		mockMvc.perform(get("/api/books/page/category")
				.param("page", "1")
				.param("size", "20")
				.param("categoryName", "CategoryName")
				.header("Authorization", "Bearer token"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.content[0].bookId").value(1L))
			.andExpect(jsonPath("$.content[0].authorName").value("Author Name"))
			.andExpect(jsonPath("$.content[0].publisherName").value("Publisher Name"))
			.andExpect(jsonPath("$.content[0].bookStatusName").value("Available"))
			.andExpect(jsonPath("$.content[0].bookTitle").value("Book Title"))
			.andExpect(jsonPath("$.content[0].bookDescription").value("Book Description"))
			.andExpect(jsonPath("$.content[0].bookQuantity").value(10))
			.andExpect(jsonPath("$.content[0].bookPublishDate").isNotEmpty())
			.andExpect(jsonPath("$.content[0].bookIsbn").value("978-3-16-148410-0"))
			.andExpect(jsonPath("$.content[0].bookPrice").value(new BigDecimal("29.99").toString()))
			.andExpect(jsonPath("$.content[0].bookSalePrice").value(new BigDecimal("24.99").toString()))
			.andExpect(jsonPath("$.content[0].bookSalePercent").value(new BigDecimal("10").toString()))
			.andExpect(jsonPath("$.content[0].bookImageUrl").value("http://example.com/image.jpg"))
			.andExpect(jsonPath("$.number").value(1))
			.andExpect(jsonPath("$.size").value(20));

		verify(bookService).findAllBooksByCategoryName(pageable, "CategoryName");
	}
}
