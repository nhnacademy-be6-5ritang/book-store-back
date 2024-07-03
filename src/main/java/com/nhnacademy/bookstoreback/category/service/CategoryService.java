package com.nhnacademy.bookstoreback.category.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nhnacademy.bookstoreback.category.domain.dto.request.CreateCategoryRequest;
import com.nhnacademy.bookstoreback.category.domain.dto.request.UpdateCategoryRequest;
import com.nhnacademy.bookstoreback.category.domain.dto.respnse.CreateCategoryResponse;
import com.nhnacademy.bookstoreback.category.domain.dto.respnse.GetCategoryResponse;
import com.nhnacademy.bookstoreback.category.domain.dto.respnse.UpdateCategoryResponse;

/**
 * CategoryService 인터페이스
 * 카테고리 관련 서비스를 제공하는 인터페이스입니다.
 *
 * @version 1.0
 */
public interface CategoryService {

	/**
	 * 모든 카테고리 조회
	 *
	 * @return 카테고리 리스트
	 */
	List<GetCategoryResponse> getCategories();

	/**
	 * 페이징 처리된 카테고리 조회
	 *
	 * @param pageable 페이지 요청 정보
	 * @return 페이징 처리된 카테고리 리스트
	 */
	Page<GetCategoryResponse> getCategories(Pageable pageable);

	/**
	 * 특정 도서의 카테고리 조회
	 *
	 * @param bookId 도서 ID
	 * @return 도서의 카테고리 리스트
	 */
	List<GetCategoryResponse> getCategoriesByBookId(Long bookId);

	/**
	 * 카테고리 ID 기반 카테고리 조회
	 *
	 * @param categoryId 카테고리 ID
	 * @return 카테고리 정보
	 */
	GetCategoryResponse getCategory(Long categoryId);

	/**
	 * 새로운 카테고리 생성
	 *
	 * @param request 카테고리 생성 요청 정보
	 * @return 생성된 카테고리 정보
	 */
	CreateCategoryResponse createCategory(CreateCategoryRequest request);

	/**
	 * 카테고리 업데이트
	 *
	 * @param categoryId 카테고리 ID
	 * @param request 카테고리 업데이트 요청 정보
	 * @return 업데이트된 카테고리 정보
	 */
	UpdateCategoryResponse updateCategory(Long categoryId, UpdateCategoryRequest request);

	/**
	 * 카테고리 삭제
	 *
	 * @param categoryId 카테고리 ID
	 */
	void deleteCategory(Long categoryId);
}
