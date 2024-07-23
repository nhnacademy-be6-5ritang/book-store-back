package com.nhnacademy.bookstoreback.publisher.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nhnacademy.bookstoreback.publisher.domain.dto.respnse.PublisherDto;
import com.nhnacademy.bookstoreback.publisher.domain.entity.Publisher;

/**
 * 출판사 관리 서비스 인터페이스입니다.
 */
public interface PublisherService {

	/**
	 * 출판사 이름을 기반으로 출판사를 조회하거나 새로 생성합니다.
	 *
	 * @param publisherName 출판사 이름
	 * @return 해당 출판사 정보
	 */
	Publisher findOrCreatePublisher(String publisherName);

	/**
	 * 모든 출판사를 조회합니다.
	 *
	 * @return 모든 출판사 리스트
	 */
	List<PublisherDto> getPublishers();

	/**
	 * 페이징된 형식으로 모든 출판사를 조회합니다.
	 *
	 * @param pageable 페이징 정보
	 * @return 페이징된 출판사의 페이지
	 */
	Page<PublisherDto> getPublishers(Pageable pageable);

	/**
	 * 주어진 출판사 ID에 해당하는 출판사를 조회합니다.
	 *
	 * @param publisherId 출판사 ID
	 * @return 해당 출판사 정보
	 */
	PublisherDto getPublisher(Long publisherId);

	/**
	 * 새로운 출판사를 생성합니다.
	 *
	 * @param request 생성할 출판사 정보
	 */
	void createPublisher(PublisherDto request);

	/**
	 * 주어진 출판사 ID에 해당하는 출판사 정보를 업데이트합니다.
	 *
	 * @param publisherId 업데이트할 출판사 ID
	 * @param request 업데이트할 출판사 정보
	 */
	void updatePublisher(Long publisherId, PublisherDto request);

	/**
	 * 주어진 출판사 ID에 해당하는 출판사를 삭제합니다.
	 *
	 * @param publisherId 삭제할 출판사 ID
	 */
	void deletePublisher(Long publisherId);
}