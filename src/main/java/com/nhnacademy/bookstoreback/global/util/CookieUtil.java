package com.nhnacademy.bookstoreback.global.util;

import java.util.Arrays;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CookieUtil {
	public static boolean exists(HttpServletRequest request, String cookieName) {
		Cookie[] cookies = request.getCookies();

		if (cookies != null) {
			return Arrays.stream(cookies).anyMatch(cookie -> cookie.getName().equals(cookieName));
		}

		return false;
	}

	public static void addCookie(HttpServletResponse response, String cookieName, Object cookieValue, int maxAge) {
		Cookie cookie = new Cookie(cookieName, String.valueOf(cookieValue));
		cookie.setMaxAge(maxAge);
		cookie.setPath("/");
		cookie.setHttpOnly(true);
		response.addCookie(cookie);

	}

	public static Cookie getCookie(HttpServletRequest request, String cookieName) {
		Cookie[] cookies = request.getCookies();
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
