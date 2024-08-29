package com.nhnacademy.bookstoreback.publisher.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookstoreback.publisher.domain.dto.respnse.PublisherDto;
import com.nhnacademy.bookstoreback.publisher.service.impl.PublisherServiceImpl;

@WebMvcTest(PublisherController.class)
class PublisherControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@MockBean
	private PublisherServiceImpl publisherService;

	@Autowired
	private ObjectMapper objectMapper;

	private PublisherDto publisherDto;

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		publisherDto = new PublisherDto(1L, "Test Publisher");
	}

	@Test
	void testGetPublishersWithPagination() throws Exception {
		when(publisherService.getPublishers(any())).thenReturn(Page.empty());

		mockMvc.perform(MockMvcRequestBuilders.get("/api/publishers")
				.param("page", "1")
				.param("size", "10")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());
	}

	@Test
	void testGetPublisher() throws Exception {
		when(publisherService.getPublisher(anyLong())).thenReturn(publisherDto);

		mockMvc.perform(MockMvcRequestBuilders.get("/api/publishers/1")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("$.publisherName").value("Test Publisher"));
	}

	@Test
	void testCreatePublisher() throws Exception {
		doNothing().when(publisherService).createPublisher(mock(PublisherDto.class));

		mockMvc.perform(MockMvcRequestBuilders.post("/api/publishers")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(publisherDto)))
			.andExpect(status().isCreated());
	}

	@Test
	void testUpdatePublisher() throws Exception {
		doNothing().when(publisherService).updatePublisher(anyLong(), any());

		mockMvc.perform(MockMvcRequestBuilders.put("/api/publishers/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(publisherDto)))
			.andExpect(status().isOk());
	}

	@Test
	void testDeletePublisher() throws Exception {
		doNothing().when(publisherService).deletePublisher(1L);

		mockMvc.perform(delete("/api/publishers/1")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());
	}
}
