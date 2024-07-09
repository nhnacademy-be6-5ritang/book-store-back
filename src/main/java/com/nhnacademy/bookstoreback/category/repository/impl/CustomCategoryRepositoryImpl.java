package com.nhnacademy.bookstoreback.category.repository.impl;

import static com.nhnacademy.bookstoreback.category.domain.entity.QCategory.*;

import java.util.List;
import java.util.stream.Collectors;

import com.nhnacademy.bookstoreback.category.domain.dto.respnse.CategorySearchResult;
import com.nhnacademy.bookstoreback.category.repository.CustomCategoryRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

public class CustomCategoryRepositoryImpl implements CustomCategoryRepository {


	private final JPAQueryFactory queryFactory;

	public CustomCategoryRepositoryImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}


	@Override
	public List<CategorySearchResult> findCategoriesByPartialName(String name) {
		return queryFactory
			.select(category)
			.from(category)
			.where(category.categoryName.toLowerCase().contains(name.toLowerCase()))
			.fetch()
			.stream()
			.map(c -> new CategorySearchResult(c.getCategoryId(), c.getCategoryName()))
			.collect(Collectors.toList());
	}
}
