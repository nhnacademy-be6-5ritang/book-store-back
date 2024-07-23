package com.nhnacademy.bookstoreback.category.controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookstoreback.category.domain.dto.request.CreateCategoryRequest;
import com.nhnacademy.bookstoreback.category.domain.dto.request.UpdateCategoryRequest;
import com.nhnacademy.bookstoreback.category.domain.dto.respnse.CategorySearchResult;
import com.nhnacademy.bookstoreback.category.domain.dto.respnse.GetCategoryResponse;
import com.nhnacademy.bookstoreback.category.service.impl.CategoryServiceImpl;

class CategoryControllerTest {

	private MockMvc mockMvc;

	@Mock
	private CategoryServiceImpl categoryService;

	@InjectMocks
	private CategoryController categoryController;

	private ObjectMapper objectMapper = new ObjectMapper();

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();
	}

	@Test
	void testGetCategories() throws Exception {
		GetCategoryResponse response = new GetCategoryResponse(1L, "CategoryName", null);
		List<GetCategoryResponse> responseList = Collections.singletonList(response);

		given(categoryService.getCategories()).willReturn(responseList);

		mockMvc.perform(get("/api/categories")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].categoryId", is(1)))
			.andExpect(jsonPath("$[0].categoryName", is("CategoryName")));
	}

	// @Test
	// void testGetCategoriesWithPagination() throws Exception {
	// 	Pageable pageable = PageRequest.of(1, 10);
	// 	GetCategoryResponse response = new GetCategoryResponse(1L, "CategoryName", null);
	// 	Page<GetCategoryResponse> responsePage = new PageImpl<>(Collections.singletonList(response), pageable, 1);
	//
	// 	given(categoryService.getCategories(ArgumentMatchers.any(Pageable.class))).willReturn(responsePage);
	//
	// 	mockMvc.perform(get("/api/categories/page")
	// 			.param("page", "1")
	// 			.param("size", "10")
	// 			.contentType(MediaType.APPLICATION_JSON))
	// 		.andExpect(status().isOk())
	// 		.andExpect(jsonPath("$.content[0].categoryId", is(1)))
	// 		.andExpect(jsonPath("$.content[0].categoryName", is("CategoryName")));
	// }

	@Test
	void testGetCategoriesByBookId() throws Exception {
		GetCategoryResponse response = new GetCategoryResponse(1L, "CategoryName", null);
		List<GetCategoryResponse> responseList = Collections.singletonList(response);

		given(categoryService.getCategoriesByBookId(1L)).willReturn(responseList);

		mockMvc.perform(get("/api/categories/books/1")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].categoryId", is(1)))
			.andExpect(jsonPath("$[0].categoryName", is("CategoryName")));
	}

	@Test
	void testGetCategory() throws Exception {
		GetCategoryResponse response = new GetCategoryResponse(1L, "CategoryName", null);

		given(categoryService.getCategory(1L)).willReturn(response);

		mockMvc.perform(get("/api/categories/1")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.categoryId", is(1)))
			.andExpect(jsonPath("$.categoryName", is("CategoryName")));
	}

	@Test
	void testCreateCategory() throws Exception {
		CreateCategoryRequest request = new CreateCategoryRequest("NewCategory", null);

		mockMvc.perform(post("/api/categories")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isCreated());
	}

	@Test
	void testUpdateCategory() throws Exception {
		UpdateCategoryRequest request = new UpdateCategoryRequest("UpdatedCategory", null);

		mockMvc.perform(put("/api/categories/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk());
	}

	@Test
	void testDeleteCategory() throws Exception {
		mockMvc.perform(delete("/api/categories/1"))
			.andExpect(status().isOk());

		verify(categoryService).deleteCategory(1L);
	}

	@Test
	void testSearchCategories() throws Exception {
		CategorySearchResult result = new CategorySearchResult(1L, "CategoryName");
		List<CategorySearchResult> results = Collections.singletonList(result);

		given(categoryService.searchCategories("searchKey")).willReturn(results);

		mockMvc.perform(get("/api/categories/search/test")
				.param("key", "searchKey")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].categoryId", is(1)))
			.andExpect(jsonPath("$[0].categoryName", is("CategoryName")));
	}
}