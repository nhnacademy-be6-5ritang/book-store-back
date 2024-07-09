package com.nhnacademy.bookstoreback.auth.jwt.dto.request;

import lombok.Builder;

@Builder
public record ReissueTokenRequest(
	String refreshToken
) {
}
