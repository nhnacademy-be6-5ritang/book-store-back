package com.nhnacademy.bookstoreback.category.repository;

import java.util.List;

import com.nhnacademy.bookstoreback.category.domain.dto.respnse.CategorySearchResult;

public interface CustomCategoryRepository {

	/**
	 * @author 이기훈
	 * @param  name 카테고리이름
	 *
	 * @return 카테고리검색 결과 반환하는 메소드
	 */
	List<CategorySearchResult> findCategoriesByPartialName(String name);
}
