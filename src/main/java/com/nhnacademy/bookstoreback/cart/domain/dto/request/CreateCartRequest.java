package com.nhnacademy.bookstoreback.cart.domain.dto.request;

import jakarta.servlet.http.Cookie;

public record CreateCartRequest(
	Cookie[] cookies) {
}
