package com.nhnacademy.bookstoreback.cart.domain.dto.response;

import jakarta.servlet.http.Cookie;

public record CreateCartResponse(
	Cookie[] cookies) {
}
