package com.nhnacademy.bookstoreback.bookcart.service;

import java.util.List;

import com.nhnacademy.bookstoreback.auth.jwt.dto.CurrentUserDetails;
import com.nhnacademy.bookstoreback.bookcart.domain.dto.request.CreateBookCartRequest;
import com.nhnacademy.bookstoreback.bookcart.domain.dto.request.UpdateBookCartRequest;
import com.nhnacademy.bookstoreback.bookcart.domain.dto.response.GetBookCartResponse;
import com.nhnacademy.bookstoreback.bookcart.domain.entity.BookCart;

/**
 * @author 이경헌
 * 책 장바구니 관련 비즈니스 로직을 처리하는 서비스 인터페이스입니다.
 */
public interface BookCartService {

	/**
	 * 현재 사용자의 장바구니에서 책 목록을 가져옵니다.
	 *
	 * @param currentUser 현재 인증된 사용자의 정보를 담고 있는 객체입니다.
	 * @param cartId      사용자의 장바구니 ID 입니다.
	 * @return 장바구니에 담긴 책 목록을 반환합니다.
	 */
	List<GetBookCartResponse> getBookCartsByCartId(CurrentUserDetails currentUser, String cartId);

	/**
	 * 새로운 책을 장바구니에 추가합니다.
	 *
	 * @param currentUser 현재 인증된 사용자의 정보를 담고 있는 객체입니다.
	 * @param request     추가할 책의 정보를 담고 있는 요청 객체입니다.
	 * @param cartId      사용자의 장바구니 ID 입니다.
	 */
	void createBookCart(CurrentUserDetails currentUser, CreateBookCartRequest request, String cartId);

	/**
	 * 장바구니에 있는 특정 책의 수량을 업데이트합니다.
	 *
	 * @param bookId  업데이트할 책 항목의 ID 입니다.
	 * @param currentUser 현재 인증된 사용자의 정보를 담고 있는 객체입니다.
	 * @param request     업데이트할 책의 정보를 담고 있는 요청 객체입니다.
	 * @param cartId      사용자의 장바구니 ID 입니다.
	 */
	void updateBookCart(Long bookId, CurrentUserDetails currentUser, UpdateBookCartRequest request,
		String cartId);

	/**
	 * 장바구니에서 특정 책 항목을 삭제합니다.
	 *
	 * @param bookId  삭제할 책 항목의 ID 입니다.
	 * @param currentUser 현재 인증된 사용자의 정보를 담고 있는 객체입니다.
	 * @param cartId      사용자의 장바구니 ID 입니다.
	 */
	void deleteBookCart(Long bookId, CurrentUserDetails currentUser, String cartId);

	/**
	 * 현재 사용자의 새로운 장바구니를 설정하고, 설정된 장바구니를 HTTP 응답에 쿠키로 추가합니다.
	 *
	 * @param currentUser 현재 인증된 사용자의 정보를 담고 있는 객체입니다.
	 * @param cartId      사용자의 새로운 장바구니 ID 입니다.
	 * @return 설정된 장바구니 객체를 반환합니다.
	 */
	String setupCart(CurrentUserDetails currentUser, String cartId);

	<S extends BookCart> void saveWithTtl(S entity);
}
