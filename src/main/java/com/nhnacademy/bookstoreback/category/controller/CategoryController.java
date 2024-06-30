package com.nhnacademy.bookstoreback.category.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nhnacademy.bookstoreback.category.domain.dto.request.CreateCategoryRequest;
import com.nhnacademy.bookstoreback.category.domain.dto.request.UpdateCategoryRequest;
import com.nhnacademy.bookstoreback.category.domain.dto.respnse.CreateCategoryResponse;
import com.nhnacademy.bookstoreback.category.domain.dto.respnse.GetCategoryResponse;
import com.nhnacademy.bookstoreback.category.domain.dto.respnse.UpdateCategoryResponse;
import com.nhnacademy.bookstoreback.category.service.CategoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {
	private final CategoryService categoryService;

	@GetMapping
	public ResponseEntity<List<GetCategoryResponse>> getCategories() {
		return ResponseEntity.status(HttpStatus.OK).body(categoryService.getCategories());
	}

	@GetMapping("/{categoryId}")
	public ResponseEntity<GetCategoryResponse> getCategory(@PathVariable Long categoryId) {
		return ResponseEntity.status(HttpStatus.OK).body(categoryService.getCategory(categoryId));
	}

	@PostMapping
	public ResponseEntity<CreateCategoryResponse> createCategory(
		@RequestBody CreateCategoryRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.createCategory(request));
	}

	@PutMapping("/{categoryId}")
	public ResponseEntity<UpdateCategoryResponse> updateCategory(@PathVariable Long categoryId,
		@RequestBody UpdateCategoryRequest request) {
		return ResponseEntity.status(HttpStatus.OK).body(categoryService.updateCategory(categoryId, request));
	}

	@DeleteMapping("/{categoryId}")
	public ResponseEntity<Void> deleteCategory(@PathVariable Long categoryId) {
		categoryService.deleteCategory(categoryId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
