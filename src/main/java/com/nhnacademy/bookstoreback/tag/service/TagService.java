package com.nhnacademy.bookstoreback.tag.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nhnacademy.bookstoreback.tag.domain.dto.respnse.TagDto;

/**
 * @author 김기욱, 이경헌
 * 태그 관리 서비스 인터페이스입니다.
 */
public interface TagService {

	/**
	 * 모든 태그를 조회합니다.
	 *
	 * @return 모든 태그의 목록
	 */
	List<TagDto> getTags();

	/**
	 * 페이징된 형식으로 모든 태그를 조회합니다.
	 *
	 * @param pageable 페이징 정보
	 * @return 페이징된 태그의 페이지
	 */
	Page<TagDto> getTags(Pageable pageable);

	/**
	 * 주어진 도서 ID에 해당하는 태그들을 조회합니다.
	 *
	 * @param bookId 도서 ID
	 * @return 해당 도서의 모든 태그의 목록
	 */
	List<TagDto> getTagsByBookId(Long bookId);

	/**
	 * 주어진 태그 ID에 해당하는 태그를 조회합니다.
	 *
	 * @param tagId 태그 ID
	 * @return 해당 태그의 정보
	 */
	TagDto getTag(Long tagId);

	/**
	 * 새로운 태그를 생성합니다.
	 *
	 * @param request 생성할 태그 정보 DTO
	 * @return 생성된 태그의 정보 DTO
	 */
	TagDto createTag(TagDto request);

	/**
	 * 주어진 태그 ID에 해당하는 태그 정보를 업데이트합니다.
	 *
	 * @param tagId   업데이트할 태그 ID
	 * @param request 업데이트할 태그 정보 DTO
	 * @return 업데이트된 태그의 정보 DTO
	 */
	TagDto updateTag(Long tagId, TagDto request);

	/**
	 * 주어진 태그 ID에 해당하는 태그를 삭제합니다.
	 *
	 * @param tagId 삭제할 태그의 ID
	 */
	void deleteTag(Long tagId);
}
