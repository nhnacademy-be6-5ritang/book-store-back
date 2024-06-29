package com.nhnacademy.bookstoreback.book.domain.mapper;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * HTTP 통신을 위한 RestTemplate Bean 설정
 *
 * @author 김기욱
 * @version 1.0
 */
@Configuration
public class AppConfig {

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}