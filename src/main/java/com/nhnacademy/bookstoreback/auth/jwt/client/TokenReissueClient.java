package com.nhnacademy.bookstoreback.auth.jwt.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.nhnacademy.bookstoreback.auth.jwt.dto.response.ReissueTokensResponse;

@FeignClient(name = "token-feign-client", url = "http://localhost:8070")
public interface TokenReissueClient {
	@PostMapping("/auth/reissue-with-refresh-token")
	ReissueTokensResponse reissueTokensWithRefreshToken(@RequestBody String refreshToken);
}
