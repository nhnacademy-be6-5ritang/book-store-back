package com.nhnacademy.bookstoreback.auth.jwt.dto.response;

import lombok.Builder;

@Builder
public record ReissueTokensResponse(
	String accessToken,
	String refreshToken
) {
}
