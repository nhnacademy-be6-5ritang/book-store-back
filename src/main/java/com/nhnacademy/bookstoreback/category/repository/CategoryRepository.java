package com.nhnacademy.bookstoreback.category.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nhnacademy.bookstoreback.category.domain.entity.Category;

/**
 * @author 김기욱, 이경헌
 * Category 엔티티를 관리하는 Spring Data JPA 리포지토리입니다.
 */
public interface CategoryRepository extends JpaRepository<Category, Long>, CustomCategoryRepository {

	/**
	 * 주어진 카테고리 이름이 존재하는지 확인합니다.
	 *
	 * @param categoryName 확인할 카테고리 이름
	 * @return 카테고리 이름이 존재하면 true, 그렇지 않으면 false
	 */
	boolean existsByCategoryName(String categoryName);

	/**
	 * 주어진 카테고리 이름을 제외한 모든 카테고리를 조회합니다.
	 *
	 * @param categoryName 제외할 카테고리 이름
	 * @return 주어진 카테고리 이름을 제외한 모든 카테고리의 리스트
	 */
	List<Category> findAllByCategoryNameNot(String categoryName);

	/**
	 * 주어진 카테고리 이름에 해당하는 카테고리를 조회합니다.
	 *
	 * @param categoryName 조회할 카테고리 이름
	 * @return 주어진 카테고리 이름에 해당하는 카테고리, 존재하지 않을 경우 Optional.empty()
	 */
	Optional<Category> findByCategoryName(String categoryName);
}
