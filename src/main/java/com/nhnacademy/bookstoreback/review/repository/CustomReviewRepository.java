package com.nhnacademy.bookstoreback.review.repository;

/**
 * @author 이경헌
 * 쿼리dsl 적용 커스텀레포지토리
 */
public interface CustomReviewRepository {
	/**
	 * 주어진 도서 ID에 대한 리뷰의 평균 점수를 반환합니다.
	 *
	 * @param bookId 리뷰의 평균 점수를 계산할 도서의 ID
	 * @return 주어진 도서에 대한 리뷰의 평균 점수. 리뷰가 없는 경우 0을 반환할 수 있습니다.
	 */
	double getReviewsAverageScoreByBookId(Long bookId);
}
