package com.nhnacademy.bookstoreback.author.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookstoreback.author.domain.dto.respnse.AuthorDto;
import com.nhnacademy.bookstoreback.author.service.impl.AuthorServiceImpl;

class AuthorControllerTest {
	@Mock
	private AuthorServiceImpl authorService;

	@InjectMocks
	private AuthorController authorController;

	private MockMvc mockMvc;
	private ObjectMapper objectMapper;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(authorController).build();
		objectMapper = new ObjectMapper();
	}

	@Test
	void testGetAuthor() throws Exception {
		AuthorDto authorDto = new AuthorDto(1L, "Author Name");
		when(authorService.getAuthor(anyLong())).thenReturn(authorDto);

		mockMvc.perform(MockMvcRequestBuilders.get("/api/authors/1"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("$.authorId").value(1))
			.andExpect(MockMvcResultMatchers.jsonPath("$.authorName").value("Author Name"));

		verify(authorService).getAuthor(anyLong());
	}

	@Test
	void testCreateAuthor() throws Exception {
		AuthorDto authorDto = new AuthorDto(null, "New Author");
		mockMvc.perform(MockMvcRequestBuilders.post("/api/authors")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(authorDto)))
			.andExpect(MockMvcResultMatchers.status().isCreated());

		verify(authorService).createAuthor(any(AuthorDto.class));
	}

	@Test
	void testUpdateAuthor() throws Exception {
		AuthorDto authorDto = new AuthorDto(1L, "Updated Author");
		doNothing().when(authorService).updateAuthor(anyLong(), any(AuthorDto.class));

		mockMvc.perform(MockMvcRequestBuilders.put("/api/authors/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(authorDto)))
			.andExpect(MockMvcResultMatchers.status().isOk());

		verify(authorService).updateAuthor(anyLong(), any(AuthorDto.class));
	}

	@Test
	void testDeleteAuthor() throws Exception {
		doNothing().when(authorService).deleteAuthor(anyLong());

		mockMvc.perform(MockMvcRequestBuilders.delete("/api/authors/1"))
			.andExpect(MockMvcResultMatchers.status().isOk());

		verify(authorService).deleteAuthor(anyLong());
	}
}
