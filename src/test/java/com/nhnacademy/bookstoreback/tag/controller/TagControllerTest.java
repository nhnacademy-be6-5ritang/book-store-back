package com.nhnacademy.bookstoreback.tag.controller;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookstoreback.tag.domain.dto.respnse.TagDto;
import com.nhnacademy.bookstoreback.tag.service.impl.TagServiceImpl;

@ExtendWith(MockitoExtension.class)
class TagControllerTest {

	@InjectMocks
	private TagController tagController;

	@Mock
	private TagServiceImpl tagService;

	private MockMvc mockMvc;
	private ObjectMapper objectMapper;

	private TagDto tagDto;

	@BeforeEach
	void setUp() {
		objectMapper = new ObjectMapper();
		mockMvc = MockMvcBuilders.standaloneSetup(tagController).build();
		tagDto = new TagDto(1L, "Sample Tag");
	}

	@Test
	void testGetTags() throws Exception {
		List<TagDto> tagDtos = Collections.singletonList(tagDto);
		given(tagService.getTags()).willReturn(tagDtos);

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/tags")
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn();

		String expectedJson = objectMapper.writeValueAsString(tagDtos);
		MockMvcResultMatchers.content().json(expectedJson).match(result);
	}

	// @Test
	// void testGetTagsPageable() throws Exception {
	// 	when(tagService.getTags(any())).thenReturn(Page.empty());
	//
	// 	mockMvc.perform(MockMvcRequestBuilders.get("/api/tags/page")
	// 			.param("page", "0")
	// 			.param("size", "10")
	// 			.accept(MediaType.APPLICATION_JSON))
	// 		.andExpect(status().isOk());
	// }

	@Test
	void testGetTagsByBookId() throws Exception {
		List<TagDto> tagDtos = Collections.singletonList(tagDto);
		given(tagService.getTagsByTagId(1L)).willReturn(tagDtos);

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/books/1/tags")
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn();

		String expectedJson = objectMapper.writeValueAsString(tagDtos);
		MockMvcResultMatchers.content().json(expectedJson).match(result);
	}

	@Test
	void testGetTag() throws Exception {
		given(tagService.getTag(1L)).willReturn(tagDto);

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/tags/1")
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn();

		String expectedJson = objectMapper.writeValueAsString(tagDto);
		MockMvcResultMatchers.content().json(expectedJson).match(result);
	}

	@Test
	void testCreateTag() throws Exception {
		TagDto requestTagDto = new TagDto(null, "New Tag");
		given(tagService.createTag(requestTagDto)).willReturn(tagDto);

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/tags")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requestTagDto)))
			.andExpect(status().isCreated())
			.andReturn();

		String expectedJson = objectMapper.writeValueAsString(tagDto);
		MockMvcResultMatchers.content().json(expectedJson).match(result);
	}

	@Test
	void testUpdateTag() throws Exception {
		TagDto requestTagDto = new TagDto(1L, "Updated Tag");
		given(tagService.updateTag(1L, requestTagDto)).willReturn(requestTagDto);

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/api/tags/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requestTagDto)))
			.andExpect(status().isOk())
			.andReturn();

		String expectedJson = objectMapper.writeValueAsString(requestTagDto);
		MockMvcResultMatchers.content().json(expectedJson).match(result);
	}

	@Test
	void testDeleteTag() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.delete("/api/tags/1"))
			.andExpect(status().isNoContent());

		then(tagService).should().deleteTag(1L);
	}
}
