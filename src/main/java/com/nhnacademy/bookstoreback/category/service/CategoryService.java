package com.nhnacademy.bookstoreback.category.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nhnacademy.bookstoreback.category.domain.dto.request.CreateCategoryRequest;
import com.nhnacademy.bookstoreback.category.domain.dto.request.UpdateCategoryRequest;
import com.nhnacademy.bookstoreback.category.domain.dto.respnse.CreateCategoryResponse;
import com.nhnacademy.bookstoreback.category.domain.dto.respnse.GetCategoryResponse;
import com.nhnacademy.bookstoreback.category.domain.dto.respnse.UpdateCategoryResponse;
import com.nhnacademy.bookstoreback.category.domain.entity.BookCategory;
import com.nhnacademy.bookstoreback.category.domain.entity.Category;
import com.nhnacademy.bookstoreback.category.repository.BookCategoryRepository;
import com.nhnacademy.bookstoreback.category.repository.CategoryRepository;
import com.nhnacademy.bookstoreback.global.exception.AlreadyExistsException;
import com.nhnacademy.bookstoreback.global.exception.NotFoundException;
import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {
	private final CategoryRepository categoryRepository;
	private final BookCategoryRepository bookCategoryRepository;

	@Transactional(readOnly = true)
	public List<GetCategoryResponse> getCategories() {
		return categoryRepository.findAll().stream().map(GetCategoryResponse::fromEntity).toList();
	}

	@Transactional(readOnly = true)
	public List<GetCategoryResponse> getCategoriesByBookId(Long bookId) {
		List<BookCategory> bookCategories = bookCategoryRepository.findAllByBookBookId(bookId);
		List<Category> categories = bookCategories.stream().map(BookCategory::getCategory).toList();
		return categories.stream().map(GetCategoryResponse::fromEntity).toList();
	}

	@Transactional(readOnly = true)
	public GetCategoryResponse getCategory(Long categoryId) {
		Category category = categoryRepository.findById(categoryId).orElseThrow(() -> {
			String errorMessage = String.format("해당 카테고리 '%d'는 존재하지 않는 카테고리 입니다.", categoryId);
			ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.NOT_FOUND, LocalDateTime.now());
			return new NotFoundException(errorStatus);
		});

		return GetCategoryResponse.fromEntity(category);
	}

	public CreateCategoryResponse createCategory(CreateCategoryRequest request) {
		if (categoryRepository.existsByCategoryName(request.categoryName())) {
			String errorMessage = String.format("해당 카테고리 '%s'는 이미 존재 하는 카테고리 입니다.", request.categoryName());
			ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.NOT_FOUND, LocalDateTime.now());
			throw new AlreadyExistsException(errorStatus);
		}

		Category parentCategory = null;
		if (request.parentCategoryId() != null) {
			parentCategory = categoryRepository.findById(request.parentCategoryId()).orElseThrow(() -> {
				String errorMessage = String.format("해당 카테고리 '%d'는 존재하지 않는 카테고리 입니다.", request.parentCategoryId());
				ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.NOT_FOUND, LocalDateTime.now());
				return new NotFoundException(errorStatus);
			});
		}

		return CreateCategoryResponse.fromEntity(categoryRepository.save(Category.toEntity(request, parentCategory)));
	}

	public UpdateCategoryResponse updateCategory(Long categoryId, UpdateCategoryRequest request) {
		Category category = categoryRepository.findById(categoryId).orElseThrow(() -> {
			String errorMessage = String.format("해당 카테고리 '%d'는 존재하지 않는 카테고리 입니다.", categoryId);
			ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.NOT_FOUND, LocalDateTime.now());
			return new NotFoundException(errorStatus);
		});

		List<Category> categories = categoryRepository.findAllByCategoryNameNot(category.getCategoryName());

		for (Category cat : categories) {
			if (cat.getCategoryName().equals(request.categoryName())) {
				String errorMessage = String.format("해당 카테고리 '%s'는 이미 존재 하는 카테고리 입니다.", request.categoryName());
				ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.NOT_FOUND, LocalDateTime.now());
				throw new AlreadyExistsException(errorStatus);
			}
		}

		// 상위 카테고리 요청이 null 인 경우와 상위 카테고리가 자신의 카테고리와 같을 경우 예외처리
		Category parentCategory = null;
		if (request.parentCategoryId() != null && !categoryId.equals(request.parentCategoryId())) {
			parentCategory = categoryRepository.findById(request.parentCategoryId()).orElseThrow(() -> {
				String errorMessage = String.format("해당 카테고리 '%d'는 존재하지 않는 카테고리 입니다.", categoryId);
				ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.NOT_FOUND, LocalDateTime.now());
				return new NotFoundException(errorStatus);
			});
		}

		category.updateCategoryName(request.categoryName(), parentCategory);

		categoryRepository.save(category);

		return UpdateCategoryResponse.fromEntity(category);
	}

	public void deleteCategory(Long categoryId) {
		categoryRepository.findById(categoryId).orElseThrow(() -> {
			String errorMessage = String.format("해당 카테고리 '%d'는 존재하지 않는 카테고리 입니다.", categoryId);
			ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.NOT_FOUND, LocalDateTime.now());
			return new NotFoundException(errorStatus);
		});

		categoryRepository.deleteById(categoryId);
	}
}
