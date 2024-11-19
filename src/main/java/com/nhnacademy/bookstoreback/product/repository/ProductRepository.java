package com.nhnacademy.bookstoreback.product.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nhnacademy.bookstoreback.product.dto.response.GetProductResponse;
import com.nhnacademy.bookstoreback.product.dto.response.GetProductSimpleResponse;

/**
 * @author 이경헌
 * 상품을 위한 커스텀 레포지토리 인터페이스입니다.
 */
public interface ProductRepository {

	/**
	 * 베스트셀러 상품 10개의 목록을 조회합니다.
	 *
	 * @return 주문된 책들의 세부 정보를 담고 있는 {@link GetProductSimpleResponse} 객체의 리스트를 반환합니다.
	 */
	List<GetProductSimpleResponse> getBestSellerBooks();

	/**
	 * 위시리스트 상품 상위 10개의 목록을 조회합니다.
	 *
	 * @return 좋아요가 눌린 책들의 세부 정보를 담고 있는 {@link GetProductSimpleResponse} 객체의 리스트를 반환합니다.
	 */
	List<GetProductSimpleResponse> getLikesBooks();

	/**
	 * 신간 상품 10개 조회
	 *
	 * @return 신간 도서 리스트를 포함하는 List 객체
	 */
	List<GetProductSimpleResponse> getNewestBooks();

	/**
	 * 10권 목록 중 빈 권수 만큼 목록을 조회합니다.
	 *
	 * @param limit    조회할 도서의 수
	 * @return 무작위 도서 {@link GetProductSimpleResponse} 객체의 리스트를 반환합니다.
	 */
	List<GetProductSimpleResponse> getRandomBooks(int limit);

	/**
	 * 지정된 카테고리에 해당하는 상품 목록을 조회합니다.
	 *
	 * @param pageable     페이지 정보와 정렬 기준을 포함하는 객체
	 * @param categoryName 조회할 책의 카테고리 이름
	 * @return 지정된 카테고리에 속하는 책들의 {@link GetProductSimpleResponse} 객체 목록입니다.
	 */
	Page<GetProductSimpleResponse> getBooksByCategory(Pageable pageable, String categoryName);

	/**
	 * 특정 상품을 조회합니다.
	 *
	 * @param bookId 조회할 도서의 ID
	 * @return 상품의 상세 정보
	 */
	Optional<GetProductResponse> getProduct(Long bookId);

}
