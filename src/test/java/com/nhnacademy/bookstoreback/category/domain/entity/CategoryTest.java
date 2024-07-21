package com.nhnacademy.bookstoreback.category.domain.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Constructor;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.nhnacademy.bookstoreback.category.domain.dto.request.CreateCategoryRequest;

class CategoryTest {

	@Test
	void testNoArgsConstructorIsProtected() {
		try {
			Constructor<Category> constructor = Category.class.getDeclaredConstructor();
			assertTrue(java.lang.reflect.Modifier.isProtected(constructor.getModifiers()),
				"Default constructor is not protected");
		} catch (NoSuchMethodException e) {
			fail("No default constructor found");
		}
	}

	@Test
	void testCategoryCreationWithBuilder() {
		String categoryName = "Fiction";
		Category parentCategory = new Category();

		Category category = Category.builder()
			.categoryName(categoryName)
			.parentCategory(parentCategory)
			.build();

		assertNull(category.getCategoryId());
		assertNotNull(category);
		assertEquals(categoryName, category.getCategoryName());
		assertEquals(parentCategory, category.getParentCategory());
	}

	@Test
	void testCategoryCreationWithStaticMethod() {
		String categoryName = "Non-Fiction";
		Category parentCategory = new Category();

		CreateCategoryRequest request = Mockito.mock(CreateCategoryRequest.class);
		Mockito.when(request.categoryName()).thenReturn(categoryName);

		Category category = Category.toEntity(request, parentCategory);

		assertNotNull(category);
		assertEquals(categoryName, category.getCategoryName());
		assertEquals(parentCategory, category.getParentCategory());
	}

	@Test
	void testUpdateCategoryName() {
		String initialName = "Science";
		String updatedName = "Science Fiction";
		Category parentCategory = new Category();
		Category category = Category.builder()
			.categoryName(initialName)
			.parentCategory(parentCategory)
			.build();

		Category newParentCategory = new Category();
		category.updateCategoryName(updatedName, newParentCategory);

		assertEquals(updatedName, category.getCategoryName());
		assertEquals(newParentCategory, category.getParentCategory());
	}
}
