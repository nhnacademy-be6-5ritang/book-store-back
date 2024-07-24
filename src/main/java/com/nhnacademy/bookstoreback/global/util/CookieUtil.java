package com.nhnacademy.bookstoreback.global.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

/**
 * @author 이경헌
 * 서블릿 환경에서 쿠키를 처리하기 위한 유틸리티 클래스입니다.
 */
public class CookieUtil {
	// 인스턴스화를 방지하기 위한 private 생성자
	private CookieUtil() {
	}

	/**
	 * 지정된 이름, 값 및 최대 수명으로 응답에 새 쿠키를 추가합니다.
	 *
	 * @param resp        응답을 나타내는 HttpServletResponse 객체입니다.
	 * @param cookieName  추가할 쿠키의 이름입니다.
	 * @param cookieValue 추가할 쿠키의 값입니다.
	 * @param maxAge      쿠키의 최대 수명(초)입니다. 음수 값은 쿠키를 삭제해야 함을 나타냅니다.
	 */
	public static void addCookie(HttpServletResponse resp, String cookieName, Object cookieValue, int maxAge) {
		Cookie cookie = new Cookie(cookieName, String.valueOf(cookieValue));
		cookie.setMaxAge(maxAge);
		cookie.setPath("/");
		cookie.setHttpOnly(true);
		resp.addCookie(cookie);
	}
}
