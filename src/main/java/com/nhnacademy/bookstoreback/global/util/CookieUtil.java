package com.nhnacademy.bookstoreback.global.util;

import java.util.Arrays;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 서블릿 환경에서 쿠키를 처리하기 위한 유틸리티 클래스입니다.
 */
public class CookieUtil {

	/**
	 * 요청된 HttpServletRequest 객체에서 지정된 이름의 쿠키가 존재하는지 확인합니다.
	 *
	 * @param req        요청을 나타내는 HttpServletRequest 객체입니다.
	 * @param cookieName 확인할 쿠키의 이름입니다.
	 * @return 쿠키가 존재하면 true, 그렇지 않으면 false를 반환합니다.
	 */
	public static boolean exists(HttpServletRequest req, String cookieName) {
		Cookie[] cookies = req.getCookies();

		if (cookies != null) {
			return Arrays.stream(cookies).anyMatch(cookie -> cookie.getName().equals(cookieName));
		}

		return false;
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

	/**
	 * 요청된 HttpServletRequest 객체에서 지정된 이름의 쿠키를 검색합니다.
	 *
	 * @param req        요청을 나타내는 HttpServletRequest 객체입니다.
	 * @param cookieName 검색할 쿠키의 이름입니다.
	 * @return 찾은 쿠키를 반환하거나, 존재하지 않으면 null을 반환합니다.
	 */
	public static Cookie getCookie(HttpServletRequest req, String cookieName) {
		Cookie[] cookies = req.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(cookieName)) {
					return cookie;
				}
			}
		}
		return null;
	}
}
