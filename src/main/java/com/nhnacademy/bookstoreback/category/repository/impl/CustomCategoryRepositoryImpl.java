package com.nhnacademy.bookstoreback.category.repository.impl;

import static com.nhnacademy.bookstoreback.category.domain.entity.QCategory.*;

import java.util.List;

import com.nhnacademy.bookstoreback.category.domain.dto.respnse.CategorySearchResult;
import com.nhnacademy.bookstoreback.category.repository.CustomCategoryRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

public class CustomCategoryRepositoryImpl implements CustomCategoryRepository {
	private final JPAQueryFactory queryFactory;

	public CustomCategoryRepositoryImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}

	/**
	 *{@inheritDoc}
	 */
	@Override
	public List<CategorySearchResult> findCategoriesByPartialName(String name) {
		return queryFactory
			.select(category.categoryId, category.categoryName)
			.from(category)
			.where(category.categoryName.toLowerCase().contains(name.toLowerCase()))
			.fetch()
			.stream()
			.map(c -> new CategorySearchResult(c.get(category.categoryId), c.get(category.categoryName)))
			.toList();
	}
}
