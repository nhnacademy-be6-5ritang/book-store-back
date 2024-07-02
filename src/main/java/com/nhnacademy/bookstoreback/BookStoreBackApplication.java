package com.nhnacademy.bookstoreback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity
@EnableFeignClients
// @EnableDiscoveryClient
public class BookStoreBackApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookStoreBackApplication.class, args);
	}

}
