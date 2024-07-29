package com.nhnacademy.bookstoreback.category.repository.impl;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;

import com.nhnacademy.bookstoreback.category.domain.dto.respnse.CategorySearchResult;
import com.nhnacademy.bookstoreback.category.domain.entity.Category;
import com.nhnacademy.bookstoreback.config.QuerydslTestConfig;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;


@DataJpaTest
@Import(QuerydslTestConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CustomCategoryRepositoryImplTest {

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private JPAQueryFactory jpaQueryFactory;

	private CustomCategoryRepositoryImpl customCategoryRepository;

	@BeforeEach
	void setUp() {
		customCategoryRepository = new CustomCategoryRepositoryImpl(entityManager);

		// Create and persist parent category
		Category parentCategory = Category.builder()
			.categoryName("ParentCategory")
			.build();
		entityManager.persist(parentCategory);

		// Create and persist categories
		Category category1 = Category.builder()
			.categoryName("Science Fiction")
			.parentCategory(parentCategory)
			.build();
		entityManager.persist(category1);

		Category category2 = Category.builder()
			.categoryName("Science")
			.parentCategory(parentCategory)
			.build();

		entityManager.persist(category2);

		// Persist some more categories for testing
		Category category3 = Category.builder()
			.categoryName("Fiction")
			.build();
		entityManager.persist(category3);

		Category category4 = Category.builder()
			.categoryName("Non-Fiction")
			.build();
		entityManager.persist(category4);




	}

	@Test
	void findCategoriesByPartialName_Custom() {
		// Given
		String name = "Science";

		// When
		List<CategorySearchResult> results = customCategoryRepository.findCategoriesByPartialName(name);

		// Then
		assertThat(results).hasSize(2);
		assertThat(results).extracting(CategorySearchResult::categoryId)
			.containsExactlyInAnyOrder(2L, 3L);
		assertThat(results).extracting(CategorySearchResult::categoryName)
			.containsExactlyInAnyOrder("Science Fiction", "Science");
	}

	@Test
	void testFindCategoriesByPartialNameWithNoMatchingResults() {
		// Given
		String name = "Nonexistent";

		// When
		List<CategorySearchResult> results = customCategoryRepository.findCategoriesByPartialName(name);

		// Then
		assertThat(results).isEmpty();
	}

	@Test
	void testFindCategoriesByPartialNameWithPartialMatch() {
		// Given
		String name = "Fic";

		// When
		List<CategorySearchResult> results = customCategoryRepository.findCategoriesByPartialName(name);

		// Then
		assertThat(results).hasSize(3);
		assertThat(results).extracting(CategorySearchResult::categoryName)
			.containsExactlyInAnyOrder("Science Fiction", "Fiction", "Non-Fiction");
	}
}