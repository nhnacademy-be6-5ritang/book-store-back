package com.nhnacademy.bookstoreback.category.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nhnacademy.bookstoreback.category.domain.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

	boolean existsByCategoryName(String categoryName);

	List<Category> findAllByCategoryNameNot(String categoryName);
}
