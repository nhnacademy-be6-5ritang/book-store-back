package com.nhnacademy.bookstoreback.bookstatus.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookstoreback.bookstatus.domain.dto.respnse.BookStatusDto;
import com.nhnacademy.bookstoreback.bookstatus.service.impl.BookStatusServiceImpl;

@ExtendWith(MockitoExtension.class)
class BookStatusControllerTest {

	@Mock
	private BookStatusServiceImpl bookStatusService;

	@InjectMocks
	private BookStatusController bookStatusController;

	private MockMvc mockMvc;

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(bookStatusController).build();
	}

	@Test
	void testGetBookStatuses() throws Exception {
		List<BookStatusDto> bookStatusList = Arrays.asList(
			new BookStatusDto(1L, "ON_SALE"),
			new BookStatusDto(2L, "SOLD_OUT")
		);

		when(bookStatusService.getBookStatuses()).thenReturn(bookStatusList);

		mockMvc.perform(get("/api/book-statuses")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].bookStatusId").value(1L))
			.andExpect(jsonPath("$[0].bookStatusName").value("ON_SALE"))
			.andExpect(jsonPath("$[1].bookStatusId").value(2L))
			.andExpect(jsonPath("$[1].bookStatusName").value("SOLD_OUT"));
	}

	@Test
	void testGetBookStatus() throws Exception {
		BookStatusDto bookStatusDto = new BookStatusDto(1L, "ON_SALE");

		when(bookStatusService.getBookStatus(anyLong())).thenReturn(bookStatusDto);

		mockMvc.perform(get("/api/book-statuses/{bookStatusId}", 1L)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.bookStatusId").value(1L))
			.andExpect(jsonPath("$.bookStatusName").value("ON_SALE"));
	}

	@Test
	void testCreateBookStatus() throws Exception {
		BookStatusDto request = new BookStatusDto(null, "ON_SALE");

		doNothing().when(bookStatusService).createBookStatus(any(BookStatusDto.class));

		mockMvc.perform(post("/api/book-statuses")
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(request)))
			.andExpect(status().isCreated());
	}

	@Test
	void testUpdateBookStatus() throws Exception {
		BookStatusDto request = new BookStatusDto(null, "SOLD_OUT");

		doNothing().when(bookStatusService).updateBookStatus(anyLong(), any(BookStatusDto.class));

		mockMvc.perform(put("/api/book-statuses/{bookStatusId}", 1L)
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(request)))
			.andExpect(status().isOk());
	}

	@Test
	void testDeleteBookStatus() throws Exception {
		doNothing().when(bookStatusService).deleteBookStatus(anyLong());

		mockMvc.perform(delete("/api/book-statuses/{bookStatusId}", 1L)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());
	}
}
