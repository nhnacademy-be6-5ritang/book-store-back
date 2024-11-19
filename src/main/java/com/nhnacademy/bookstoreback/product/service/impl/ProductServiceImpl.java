package com.nhnacademy.bookstoreback.product.service.impl;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
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
	@Cacheable(value = "bestSellerBooksCached", key = "'bestSellerBooks'")
	public List<GetProductSimpleResponse> getBestSellerBooks() {
		List<GetProductSimpleResponse> bestSellerBooks = productRepository.getBestSellerBooks();

		int requiredBooks = 10 - bestSellerBooks.size();
		if (requiredBooks > 0) {
			List<GetProductSimpleResponse> randomBooks = productRepository.getRandomBooks(requiredBooks);
			bestSellerBooks.addAll(randomBooks);
		}

		return bestSellerBooks;
	}

	@Transactional(readOnly = true)
	@Override
	@Cacheable(value = "likesBooksCached", key = "'likesBooks'")
	public List<GetProductSimpleResponse> getLikesBooks() {
		List<GetProductSimpleResponse> likesBooks = productRepository.getLikesBooks();

		int requiredBooks = 10 - likesBooks.size();
		if (requiredBooks > 0) {
			List<GetProductSimpleResponse> randomBooks = productRepository.getRandomBooks(requiredBooks);
			likesBooks.addAll(randomBooks);
		}

		return likesBooks;
	}

	@Transactional(readOnly = true)
	@Override
	@Cacheable(value = "newestBooksCached", key = "'newestBooks'")
	public List<GetProductSimpleResponse> getNewestBooks() {
		List<GetProductSimpleResponse> newestBooks = productRepository.getNewestBooks();

		int requiredBooks = 10 - newestBooks.size();
		if (requiredBooks > 0) {
			List<GetProductSimpleResponse> randomBooks = productRepository.getRandomBooks(requiredBooks);
			newestBooks.addAll(randomBooks);
		}

		return newestBooks;
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
