package com.nhnacademy.bookstoreback.product.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nhnacademy.bookstoreback.product.dto.response.GetProductResponse;
import com.nhnacademy.bookstoreback.product.dto.response.GetProductSimpleResponse;
import com.nhnacademy.bookstoreback.product.exception.ProductNotFoundException;
import com.nhnacademy.bookstoreback.product.repository.ProductRepository;
import com.nhnacademy.bookstoreback.product.service.ProductService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
	private final ProductRepository productRepository;

	@Transactional(readOnly = true)
	@Override
	public List<GetProductSimpleResponse> getBestSellerBooks() {
		return productRepository.getBestSellerBooks();
	}

	@Transactional(readOnly = true)
	@Override
	public List<GetProductSimpleResponse> getLikesBooks() {
		return productRepository.getLikesBooks();
	}

	@Transactional(readOnly = true)
	@Override
	public List<GetProductSimpleResponse> getNewestBooks() {
		return productRepository.getNewestBooks();
	}

	@Transactional(readOnly = true)
	@Override
	public Page<GetProductSimpleResponse> getBooksByCategory(Pageable pageable, String categoryName) {
		int page = Math.max(pageable.getPageNumber() - 1, 0);
		int pageSize = pageable.getPageSize();
		Sort sort = pageable.getSort();

		return productRepository.getBooksByCategory(PageRequest.of(page, pageSize, sort), categoryName);
	}

	@Transactional(readOnly = true)
	@Override
	public GetProductResponse getProduct(Long bookId) {
		return productRepository.getProduct(bookId).orElseThrow(() -> new ProductNotFoundException(bookId));
	}

}
