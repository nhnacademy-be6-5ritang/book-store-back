package com.nhnacademy.bookstoreback.category.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nhnacademy.bookstoreback.category.domain.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

	boolean existsByCategoryName(String categoryName);

	List<Category> findAllByCategoryNameNot(String categoryName);

	Optional<Category> findByCategoryName(String categoryName);




	/**
	 * @author 이기훈
	 * 쿠폰 테스트용 / 차후 querydsl로 바꿀예정
	 *
	 */

	@Query(value = "SELECT * FROM categories WHERE lower(category_name) LIKE lower(concat('%', :name, '%'))", nativeQuery = true)
	List<Category> findCategoriesByPartialName(@Param("name") String name);


}
