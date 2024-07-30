package com.nhnacademy.bookstoreback.category.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.nhnacademy.bookstoreback.book.domain.entity.Book;
import com.nhnacademy.bookstoreback.category.domain.dto.request.CreateCategoryRequest;
import com.nhnacademy.bookstoreback.category.domain.dto.request.UpdateCategoryRequest;
import com.nhnacademy.bookstoreback.category.domain.dto.respnse.CategorySearchResult;
import com.nhnacademy.bookstoreback.category.domain.dto.respnse.GetCategoryResponse;
import com.nhnacademy.bookstoreback.category.domain.entity.BookCategory;
import com.nhnacademy.bookstoreback.category.domain.entity.Category;
import com.nhnacademy.bookstoreback.category.exception.CategoryAlreadyExistsException;
import com.nhnacademy.bookstoreback.category.exception.CategoryNotFoundException;
import com.nhnacademy.bookstoreback.category.repository.BookCategoryRepository;
import com.nhnacademy.bookstoreback.category.repository.CategoryRepository;
import com.nhnacademy.bookstoreback.category.service.impl.CategoryServiceImpl;

class CategoryServiceImplTest {

	@Mock
	private CategoryRepository categoryRepository;

	@Mock
	private BookCategoryRepository bookCategoryRepository;

	@InjectMocks
	private CategoryServiceImpl categoryService;

	private Category category;
	private Category parentCategory;
	private BookCategory bookCategory;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		parentCategory = Category.builder()
			.categoryName("Parent Category")
			.build();
		category = Category.builder()
			.categoryName("Fiction")
			.parentCategory(parentCategory)
			.build();
		bookCategory = new BookCategory(mock(Book.class), category);
	}

	@Test
	void testGetCategories() {
		when(categoryRepository.findAll()).thenReturn(List.of(category));
		List<GetCategoryResponse> categories = categoryService.getCategories();
		assertNotNull(categories);
		assertEquals(1, categories.size());
		assertEquals("Fiction", categories.get(0).categoryName());
		assertEquals("Parent Category", categories.get(0).parentCategoryName());
	}

	@Test
	void testGetCategoriesWithPagination() {
		Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "categoryId"));
		Page<Category> page = new PageImpl<>(List.of(category), pageable, 1);

		when(categoryRepository.findAll(any(Pageable.class))).thenReturn(page);

		Page<GetCategoryResponse> result = categoryService.getCategories(pageable);

		assertNotNull(result);
		assertEquals(1, result.getTotalElements());
		assertEquals(1, result.getContent().size());
		assertEquals("Fiction", result.getContent().get(0).categoryName());
	}

	@Test
	void testGetCategoriesByBookId() {
		when(bookCategoryRepository.findAllByBookBookId(anyLong())).thenReturn(List.of(bookCategory));

		List<GetCategoryResponse> categories = categoryService.getCategoriesByBookId(1L);

		assertNotNull(categories);
		assertEquals(1, categories.size());
		assertEquals("Fiction", categories.get(0).categoryName());
	}

	@Test
	void testGetCategory() {
		Long categoryId = 1L;
		when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
		GetCategoryResponse response = categoryService.getCategory(categoryId);
		assertNotNull(response);
		assertEquals("Fiction", response.categoryName());
		assertEquals("Parent Category", response.parentCategoryName());
	}

	@Test
	void testCreateCategory() {
		CreateCategoryRequest request = new CreateCategoryRequest("Science", null);
		when(categoryRepository.existsByCategoryName("Science")).thenReturn(false);
		when(categoryRepository.save(any(Category.class))).thenReturn(category);

		categoryService.createCategory(request);

		verify(categoryRepository).save(any(Category.class));
	}

	@Test
	void testCreateCategoryWhenAlreadyExists() {
		CreateCategoryRequest request = new CreateCategoryRequest("Fiction", null);
		when(categoryRepository.existsByCategoryName("Fiction")).thenReturn(true);

		assertThrows(CategoryAlreadyExistsException.class, () -> categoryService.createCategory(request));
	}

	@Test
	void updateCategory_ShouldThrowCategoryNotFoundException_WhenCategoryNotExists() {
		Long categoryId = 1L;
		UpdateCategoryRequest request = new UpdateCategoryRequest("New Name", null);

		when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

		assertThrows(CategoryNotFoundException.class, () ->
			categoryService.updateCategory(categoryId, request)
		);
	}

	@Test
	void updateCategory_ShouldThrowCategoryAlreadyExistsException_WhenCategoryNameIsDuplicate() {
		Long categoryId = 1L;
		Category category = new Category("Existing Name", null);

		UpdateCategoryRequest request = new UpdateCategoryRequest("Existing Name", null);

		when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
		when(categoryRepository.findAllByCategoryNameNot("Existing Name"))
			.thenReturn(List.of(new Category("Existing Name", null)));

		assertThrows(CategoryAlreadyExistsException.class, () ->
			categoryService.updateCategory(categoryId, request)
		);
	}

	@Test
	void updateCategory_ShouldThrowCategoryNotFoundException_WhenParentCategoryNotExists() {
		Long categoryId = 1L;
		Long parentCategoryId = 2L;
		Category category = new Category("Old Name", null);

		UpdateCategoryRequest request = new UpdateCategoryRequest("New Name", parentCategoryId);

		when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
		when(categoryRepository.findAllByCategoryNameNot("Old Name")).thenReturn(List.of());
		when(categoryRepository.findById(parentCategoryId)).thenReturn(Optional.empty());

		assertThrows(CategoryNotFoundException.class, () ->
			categoryService.updateCategory(categoryId, request)
		);
	}

	@Test
	void updateCategory_ShouldNotThrowException_WhenParentCategoryIsSameAsCurrentCategory() {
		Long categoryId = 1L;
		Category category = new Category("Old Name", null);

		UpdateCategoryRequest request = new UpdateCategoryRequest("New Name", categoryId);

		when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
		when(categoryRepository.findAllByCategoryNameNot("Old Name")).thenReturn(List.of());

		// 예외가 발생하지 않는지 확인
		categoryService.updateCategory(categoryId, request);
	}

	@Test
	void testUpdateCategoryWhenNameAlreadyExists() {
		Long categoryId = 1L;
		UpdateCategoryRequest request = new UpdateCategoryRequest("Fiction", null);
		when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
		when(categoryRepository.findAllByCategoryNameNot(anyString())).thenReturn(List.of(category));

		assertThrows(CategoryAlreadyExistsException.class, () -> categoryService.updateCategory(categoryId, request));
	}

	@Test
	void testDeleteCategory() {
		Long categoryId = 1L;
		when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

		categoryService.deleteCategory(categoryId);

		verify(bookCategoryRepository).deleteALlByCategoryCategoryId(categoryId);
		verify(categoryRepository).deleteById(categoryId);
	}

	@Test
	void testFindOrCreateCategory() {
		String categoryName = "Drama";
		Long parentCategoryId = 2L;
		when(categoryRepository.findByCategoryName(categoryName)).thenReturn(Optional.empty());
		when(categoryRepository.findById(parentCategoryId)).thenReturn(Optional.of(parentCategory));
		when(categoryRepository.save(any(Category.class))).thenReturn(category);

		Category foundOrCreatedCategory = categoryService.findOrCreateCategory(categoryName, parentCategoryId);

		assertNotNull(foundOrCreatedCategory);
		assertEquals("Fiction", foundOrCreatedCategory.getCategoryName());
		assertEquals(parentCategory, foundOrCreatedCategory.getParentCategory());
	}

	@Test
	void testSearchCategories() {
		String query = "Fiction";
		when(categoryRepository.findCategoriesByPartialName(query)).thenReturn(List.of(
			new CategorySearchResult(1L, "Fiction")
		));

		List<CategorySearchResult> results = categoryService.searchCategories(query);

		assertNotNull(results);
		assertEquals(1, results.size());
		assertEquals("Fiction", results.get(0).categoryName());
	}
}
