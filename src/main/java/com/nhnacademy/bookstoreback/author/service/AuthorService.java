package com.nhnacademy.bookstoreback.author.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nhnacademy.bookstoreback.author.domain.dto.respnse.AuthorDto;
import com.nhnacademy.bookstoreback.author.domain.entity.Author;

/**
 * AuthorService 인터페이스
 *
 * 작가 관련 서비스를 제공하는 인터페이스입니다.
 *
 * @version 1.0
 */
public interface AuthorService {

	/**
	 * 작가 이름 기반 도서 조회.
	 *
	 * @param authorName 작가 이름
	 * @return 작가가 존재하면 작가 정보, 없으면 null
	 */
	Author findOrCreateAuthor(String authorName);

	/**
	 * 모든 작가 조회.
	 *
	 * @return 모든 작가의 리스트
	 */
	List<AuthorDto> getAuthors();

	/**
	 * 페이징된 형식으로 모든 저자를 조회합니다.
	 *
	 * @param pageable 페이징 정보
	 * @return 페이징된 저자의 페이지
	 */
	Page<AuthorDto> getAuthors(Pageable pageable);

	/**
	 * 특정 작가 조회.
	 *
	 * @param authorId 작가 ID
	 * @return 작가 정보
	 */
	AuthorDto getAuthor(Long authorId);

	/**
	 * 작가 생성.
	 *
	 * @param request 생성할 작가 정보
	 * @return 생성된 작가 정보
	 */
	AuthorDto createAuthor(AuthorDto request);

	/**
	 * 작가 정보 수정.
	 *
	 * @param authorId 수정할 작가 ID
	 * @param request 수정할 작가 정보
	 * @return 수정된 작가 정보
	 */
	AuthorDto updateAuthor(Long authorId, AuthorDto request);

	/**
	 * 작가 삭제.
	 *
	 * @param authorId 삭제할 작가 ID
	 */
	void deleteAuthor(Long authorId);
}
