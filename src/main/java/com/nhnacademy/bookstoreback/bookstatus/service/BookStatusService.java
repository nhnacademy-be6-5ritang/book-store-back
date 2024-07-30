package com.nhnacademy.bookstoreback.bookstatus.service;

import java.util.List;
import java.util.Optional;

import com.nhnacademy.bookstoreback.bookstatus.domain.dto.respnse.BookStatusDto;
import com.nhnacademy.bookstoreback.bookstatus.domain.entity.BookStatus;

/**
 * @author 김기욱, 이경헌
 * 도서 상태 관련 서비스를 제공하는 인터페이스입니다.
 */
public interface BookStatusService {

	/**
	 * 도서 상태 이름 기반 도서 조회
	 *
	 * @param bookStatusName 도서 상태 이름
	 * @return 도서 상태 (Optional로 반환)
	 */
	Optional<BookStatus> findByBookStatusName(String bookStatusName);

	/**
	 * 도서 상태 생성 또는 조회
	 *
	 * @param bookStatusName 도서 상태 이름
	 * @return 도서 상태가 존재하면 도서 상태 정보, 없으면 생성된 도서 상태 정보
	 */
	BookStatus findOrCreateBookStatus(String bookStatusName);

	/**
	 * 모든 도서 상태 조회
	 *
	 * @return 도서 상태 리스트
	 */
	List<BookStatusDto> getBookStatuses();

	/**
	 * 도서 상태 ID 기반 도서 상태 조회
	 *
	 * @param bookStatusId 도서 상태 ID
	 * @return 도서 상태 정보
	 */
	BookStatusDto getBookStatus(Long bookStatusId);

	/**
	 * 새로운 도서 상태 생성
	 *
	 * @param request 도서 상태 정보
	 */
	void createBookStatus(BookStatusDto request);

	/**
	 * 도서 상태 업데이트
	 *
	 * @param bookStatusId 도서 상태 ID
	 * @param request 도서 상태 정보
	 */
	void updateBookStatus(Long bookStatusId, BookStatusDto request);

	/**
	 * 도서 상태 삭제
	 *
	 * @param bookStatusId 도서 상태 ID
	 */
	void deleteBookStatus(Long bookStatusId);
}
