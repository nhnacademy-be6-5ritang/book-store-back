package com.nhnacademy.bookstoreback.wishlist.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nhnacademy.bookstoreback.wishlist.domain.entity.WishList;

/**
 * @author 이경헌
 * WishList 엔티티를 위한 레포지토리 인터페이스입니다.
 */
public interface WishListRepository extends JpaRepository<WishList, Long> {
	/**
	 * 특정 사용자에 대한 모든 wish list 를 조회합니다.
	 *
	 * @param userId wish list 를 조회할 사용자의 ID
	 * @return 주어진 사용자 ID와 연관된 {@link WishList} 객체의 목록
	 */
	List<WishList> findAllByUserId(Long userId);

	/**
	 * 특정 사용자와 책에 대한 wish list 항목의 존재 여부를 확인합니다.
	 *
	 * @param userId 확인할 사용자의 ID
	 * @param bookId 확인할 책의 ID
	 * @return 주어진 사용자와 책에 대해 wish list 항목이 존재하면 {@code true}, 그렇지 않으면 {@code false}
	 */
	boolean existsByUserIdAndBookBookId(Long userId, Long bookId);

	/**
	 * 주어진 책 ID에 연관된 모든 wish list 항목을 삭제합니다.
	 *
	 * @param bookId 삭제할 wish list 항목들과 연관된 책의 ID
	 */
	void deleteAllByBookBookId(Long bookId);
}
