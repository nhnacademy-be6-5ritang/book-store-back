package com.nhnacademy.bookstoreback.category.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nhnacademy.bookstoreback.category.domain.dto.request.CreateCategoryRequest;
import com.nhnacademy.bookstoreback.category.domain.dto.request.UpdateCategoryRequest;
import com.nhnacademy.bookstoreback.category.domain.dto.respnse.CreateCategoryResponse;
import com.nhnacademy.bookstoreback.category.domain.dto.respnse.GetCategoryResponse;
import com.nhnacademy.bookstoreback.category.domain.dto.respnse.UpdateCategoryResponse;
import com.nhnacademy.bookstoreback.category.domain.entity.BookCategory;
import com.nhnacademy.bookstoreback.category.domain.entity.Category;
import com.nhnacademy.bookstoreback.category.exception.CategoryAlreadyExistsException;
import com.nhnacademy.bookstoreback.category.exception.CategoryNotFoundException;
import com.nhnacademy.bookstoreback.category.repository.BookCategoryRepository;
import com.nhnacademy.bookstoreback.category.repository.CategoryRepository;
import com.nhnacademy.bookstoreback.category.service.CategoryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {
	private final CategoryRepository categoryRepository;
	private final BookCategoryRepository bookCategoryRepository;

	@Transactional(readOnly = true)
	@Override
	public List<GetCategoryResponse> getCategories() {
		return categoryRepository.findAll().stream().map(GetCategoryResponse::fromEntity).toList();
	}

	@Transactional(readOnly = true)
	@Override
	public Page<GetCategoryResponse> getCategories(Pageable pageable) {
		int page = pageable.getPageNumber() - 1;
		int pageSize = pageable.getPageSize();

		return categoryRepository.findAll(
				PageRequest.of(page, pageSize, Sort.by(Sort.Direction.ASC, "categoryId")))
			.map(GetCategoryResponse::fromEntity);
	}

	@Transactional(readOnly = true)
	@Override
	public List<GetCategoryResponse> getCategoriesByBookId(Long bookId) {
		List<BookCategory> bookCategories = bookCategoryRepository.findAllByBookBookId(bookId);
		List<Category> categories = bookCategories.stream().map(BookCategory::getCategory).toList();
		return categories.stream().map(GetCategoryResponse::fromEntity).toList();
	}

	@Transactional(readOnly = true)
	@Override
	public GetCategoryResponse getCategory(Long categoryId) {
		Category category = categoryRepository.findById(categoryId)
			.orElseThrow(() -> new CategoryNotFoundException(categoryId));
		return GetCategoryResponse.fromEntity(category);
	}

	@Override
	public CreateCategoryResponse createCategory(CreateCategoryRequest request) {
		if (categoryRepository.existsByCategoryName(request.categoryName())) {
			throw new CategoryAlreadyExistsException(request.categoryName());
		}

		Category parentCategory = null;
		if (request.parentCategoryId() != null) {
			parentCategory = categoryRepository.findById(request.parentCategoryId())
				.orElseThrow(() -> new CategoryNotFoundException(request.parentCategoryId()));
		}
		return CreateCategoryResponse.fromEntity(categoryRepository.save(Category.toEntity(request, parentCategory)));
	}

	@Override
	public UpdateCategoryResponse updateCategory(Long categoryId, UpdateCategoryRequest request) {
		Category category = categoryRepository.findById(categoryId)
			.orElseThrow(() -> new CategoryNotFoundException(categoryId));

		List<Category> categories = categoryRepository.findAllByCategoryNameNot(category.getCategoryName());

		for (Category cat : categories) {
			if (cat.getCategoryName().equals(request.categoryName())) {
				throw new CategoryAlreadyExistsException(request.categoryName());
			}
		}

		// 상위 카테고리 요청이 null 인 경우와 상위 카테고리가 자신의 카테고리와 같을 경우 예외처리
		Category parentCategory = null;
		if (request.parentCategoryId() != null && !categoryId.equals(request.parentCategoryId())) {
			parentCategory = categoryRepository.findById(request.parentCategoryId())
				.orElseThrow(() -> new CategoryNotFoundException(categoryId));
		}
		category.updateCategoryName(request.categoryName(), parentCategory);
		return UpdateCategoryResponse.fromEntity(category);
	}

	@Override
	public void deleteCategory(Long categoryId) {
		categoryRepository.findById(categoryId).orElseThrow(() -> new CategoryNotFoundException(categoryId));
		bookCategoryRepository.deleteALlByCategoryCategoryId(categoryId);
		categoryRepository.deleteById(categoryId);
	}

	@Transactional
	public Category findOrCreateCategory(String categoryName, Long parentCategoryId) {
		return categoryRepository.findByCategoryName(categoryName)
			.orElseGet(() -> {
				Category parentCategory = categoryRepository.findById(parentCategoryId)
					.orElseThrow(() -> new CategoryNotFoundException(parentCategoryId));
				Category newCategory = Category.builder()
					.categoryName(categoryName)
					.parentCategory(parentCategory)
					.build();
				return categoryRepository.save(newCategory);
			});
	}
}
