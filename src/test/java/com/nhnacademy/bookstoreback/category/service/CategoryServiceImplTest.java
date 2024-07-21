package com.nhnacademy.bookstoreback.category.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.nhnacademy.bookstoreback.category.domain.dto.request.CreateCategoryRequest;
import com.nhnacademy.bookstoreback.category.domain.dto.request.UpdateCategoryRequest;
import com.nhnacademy.bookstoreback.category.domain.dto.respnse.CategorySearchResult;
import com.nhnacademy.bookstoreback.category.domain.dto.respnse.CreateCategoryResponse;
import com.nhnacademy.bookstoreback.category.domain.dto.respnse.GetCategoryResponse;
import com.nhnacademy.bookstoreback.category.domain.dto.respnse.UpdateCategoryResponse;
import com.nhnacademy.bookstoreback.category.domain.entity.BookCategory;
import com.nhnacademy.bookstoreback.category.domain.entity.Category;
import com.nhnacademy.bookstoreback.category.exception.CategoryAlreadyExistsException;
import com.nhnacademy.bookstoreback.category.exception.CategoryNotFoundException;
import com.nhnacademy.bookstoreback.category.repository.BookCategoryRepository;
import com.nhnacademy.bookstoreback.category.repository.CategoryRepository;

class CategoryServiceImplTest {

	@Mock
	private CategoryRepository categoryRepository;

	@Mock
	private BookCategoryRepository bookCategoryRepository;

	@InjectMocks
	private CategoryServiceImpl categoryService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testGetCategories() {
		Category category = new Category("CategoryName", null);
		given(categoryRepository.findAll()).willReturn(List.of(category));

		List<GetCategoryResponse> response = categoryService.getCategories();

		assertEquals(1, response.size());
		assertEquals("CategoryName", response.get(0).categoryName());
	}

	@Test
	void testGetCategoriesWithPagination() {
		Pageable pageable = PageRequest.of(0, 10);
		Category category = new Category("CategoryName", null);
		Page<Category> categoryPage = new PageImpl<>(List.of(category), pageable, 1);
		given(categoryRepository.findAll(ArgumentMatchers.any(Pageable.class))).willReturn(categoryPage);

		Page<GetCategoryResponse> responsePage = categoryService.getCategories(pageable);

		assertEquals(1, responsePage.getTotalElements());
		assertEquals("CategoryName", responsePage.getContent().get(0).categoryName());
	}

	@Test
	void testGetCategoriesByBookId() {
		Category category = new Category("CategoryName", null);
		BookCategory bookCategory = new BookCategory(null, category);
		given(bookCategoryRepository.findAllByBookBookId(1L)).willReturn(List.of(bookCategory));

		List<GetCategoryResponse> response = categoryService.getCategoriesByBookId(1L);

		assertEquals(1, response.size());
		assertEquals("CategoryName", response.get(0).categoryName());
	}

	@Test
	void testGetCategory() {
		Category category = new Category("CategoryName", null);
		given(categoryRepository.findById(1L)).willReturn(Optional.of(category));

		GetCategoryResponse response = categoryService.getCategory(1L);

		assertEquals("CategoryName", response.categoryName());
	}

	@Test
	void testGetCategoryNotFound() {
		given(categoryRepository.findById(1L)).willReturn(Optional.empty());

		assertThrows(CategoryNotFoundException.class, () -> categoryService.getCategory(1L));
	}

	@Test
	void testCreateCategory() {
		CreateCategoryRequest request = new CreateCategoryRequest("NewCategory", null);
		Category category = new Category("NewCategory", null);
		given(categoryRepository.existsByCategoryName("NewCategory")).willReturn(false);
		given(categoryRepository.save(ArgumentMatchers.any(Category.class))).willReturn(category);

		CreateCategoryResponse response = categoryService.createCategory(request);

		assertEquals("NewCategory", response.categoryName());
	}

	@Test
	void testCreateCategoryAlreadyExists() {
		CreateCategoryRequest request = new CreateCategoryRequest("ExistingCategory", null);
		given(categoryRepository.existsByCategoryName("ExistingCategory")).willReturn(true);

		assertThrows(CategoryAlreadyExistsException.class, () -> categoryService.createCategory(request));
	}

	@Test
	void testUpdateCategory() {
		Category existingCategory = new Category("OldCategory", null);
		UpdateCategoryRequest request = new UpdateCategoryRequest("UpdatedCategory", null);
		given(categoryRepository.findById(1L)).willReturn(Optional.of(existingCategory));
		given(categoryRepository.findAllByCategoryNameNot("OldCategory")).willReturn(List.of());
		given(categoryRepository.save(ArgumentMatchers.any(Category.class))).willReturn(existingCategory);

		UpdateCategoryResponse response = categoryService.updateCategory(1L, request);

		assertEquals("UpdatedCategory", response.categoryName());
	}

	@Test
	void testUpdateCategoryNotFound() {
		UpdateCategoryRequest request = new UpdateCategoryRequest("UpdatedCategory", null);
		given(categoryRepository.findById(1L)).willReturn(Optional.empty());

		assertThrows(CategoryNotFoundException.class, () -> categoryService.updateCategory(1L, request));
	}

	@Test
	void testUpdateCategoryAlreadyExists() {
		Category existingCategory = new Category("OldCategory", null);
		UpdateCategoryRequest request = new UpdateCategoryRequest("ExistingCategory", null);
		given(categoryRepository.findById(1L)).willReturn(Optional.of(existingCategory));
		given(categoryRepository.findAllByCategoryNameNot("OldCategory")).willReturn(
			List.of(new Category("ExistingCategory", null)));

		assertThrows(CategoryAlreadyExistsException.class, () -> categoryService.updateCategory(1L, request));
	}

	@Test
	void testDeleteCategory() {
		given(categoryRepository.findById(1L)).willReturn(Optional.of(new Category("CategoryToDelete", null)));

		categoryService.deleteCategory(1L);

		verify(bookCategoryRepository).deleteALlByCategoryCategoryId(1L);
		verify(categoryRepository).deleteById(1L);
	}

	@Test
	void testFindOrCreateCategory() {
		Category parentCategory = new Category("ParentCategory", null);
		Category newCategory = new Category("NewCategory", parentCategory);
		given(categoryRepository.findByCategoryName("NewCategory")).willReturn(Optional.empty());
		given(categoryRepository.findById(1L)).willReturn(Optional.of(parentCategory));
		given(categoryRepository.save(ArgumentMatchers.any(Category.class))).willReturn(newCategory);

		Category result = categoryService.findOrCreateCategory("NewCategory", 1L);

		assertEquals("NewCategory", result.getCategoryName());
	}

	@Test
	void testFindOrCreateCategoryExisting() {
		Category existingCategory = new Category("ExistingCategory", null);
		given(categoryRepository.findByCategoryName("ExistingCategory")).willReturn(Optional.of(existingCategory));

		Category result = categoryService.findOrCreateCategory("ExistingCategory", null);

		assertEquals("ExistingCategory", result.getCategoryName());
	}

	@Test
	void testSearchCategories() {
		CategorySearchResult searchResult = new CategorySearchResult(1L, "SearchResult");
		given(categoryRepository.findCategoriesByPartialName("query")).willReturn(List.of(searchResult));

		List<CategorySearchResult> results = categoryService.searchCategories("query");

		assertEquals(1, results.size());
		assertEquals("SearchResult", results.get(0).categoryName());
	}
}